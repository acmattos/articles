# Whisper API Audio Transcription Made Simple with Docker

This case study demonstrates how to build a production-ready audio transcription
service using the **Whisper API** and **FastAPI**, all containerized with 
**Docker**.  
By the end of this guide, you’ll have a fully functional microservice capable of
transcribing audio and video files in multiple formats through a simple HTTP API.

The service is split into two components:

- A **Whisper Service** powered by `faster-whisper`, responsible for loading 
  models and performing the transcription.  
- An **API Gateway**, which provides authentication, consistent endpoints, and 
  request forwarding to the model service.

With this setup, you can deploy a self-hosted, scalable transcription system in 
just a few minutes: perfect for teams integrating 
**ASR (Automatic Speech Recognition)** into AI or media pipelines.

### What You’ll Learn

- How the Whisper API architecture is structured with two FastAPI services.  
- How to transcribe audio and video through simple REST endpoints.  
- How to containerize and orchestrate everything with Docker Compose.  
- How to tune parameters for speed, accuracy, and GPU usage.

```json
{"section": "introduction", "version": "1.1.0"}
```

---

## Architecture at a Glance

The project follows a **modular microservices architecture**, separating 
concerns between the API gateway and the transcription engine. This design 
simplifies scaling, monitoring, and maintenance.

### Components Overview

**Whisper Service:**  
Runs the `faster-whisper` models locally. Each model (small, medium, large-v3, 
turbo) can be preloaded on startup.  
It handles the heavy lifting: loading weights, managing devices (CPU/GPU), and 
performing the actual speech-to-text.

**API Gateway:**  
Acts as a proxy for client requests. It validates bearer tokens, checks allowed 
models, and forwards files to the Whisper Service.  
This ensures clients interact with a unified API surface, independent of the 
underlying model details.

**Docker Compose:**  
Orchestrates both containers, manages networking, exposes ports (8080 for 
Whisper, 8081 for Gateway), and persists model cache volumes to avoid repeated 
downloads.

### Request Flow

1. The user sends an audio file (e.g., `.mp3`, `.wav`, `.mp4`) to the Gateway 
   via an endpoint like `/transcribe/small`.  
2. The Gateway validates the API key, then forwards the file to the internal 
   Whisper Service.  
3. The Whisper Service transcribes the file using the requested model and 
   returns the JSON response.  
4. The Gateway sends the result back to the client.

### Health and Monitoring

Both services expose `/health` endpoints for readiness checks:

- `http://localhost:8080/health` → Whisper Service status and loaded models.  
- `http://localhost:8081/health` → API Gateway + underlying model health.

These endpoints make it easy to integrate with monitoring tools like Prometheus,
Grafana, or Docker healthchecks.

```ini
ARCH_COMPONENTS=api-gateway,whisper-service
NETWORK=docker-compose
```

---

## Project Structure

The project is organized into two main folders (one for the API Gateway and 
another for the Whisper Service): both fully containerized. At the root, a 
`docker-compose.yml` file defines how these two services interact.

Here’s the full layout:

```ini
whisper_transcriptor/
├── api-gateway/
│   ├── app/
│   │   ├── main.py
│   │   └── settings.py
│   ├── Dockerfile
│   └── requirements.txt
├── whisper-service/
│   ├── app/
│   │   ├── main.py
│   │   └── settings.py
│   ├── Dockerfile
│   └── requirements.txt
└── docker-compose.yml
```

Each component runs as an independent FastAPI app, communicating over the 
internal Docker network.

### Key Files

- **main.py:** defines routes, including `/transcribe*` and `/health`.  
- **settings.py:** configuration via Pydantic and environment variables.  
- **Dockerfile:** container build instructions for each service.  
- **requirements.txt:** Python dependencies for each environment.  
- **docker-compose.yml:** orchestration of services, volumes, ports, and 
  healthchecks.

```json
{"section": "project-structure", "containers": ["api-gateway", "whisper-service"]}
```

---

## Whisper Service Deep Dive

The **Whisper Service** loads models with `faster-whisper` and handles all 
transcription requests. It is lightweight, self-contained, and GPU-aware.  
The `faster-whisper` project is a highly optimized CTranslate2-based 
reimplementation of Whisper, providing better speed and memory efficiency.

### 1) Service Initialization

```ini
# app.main (excerpt)
@app.on_event("startup")
def load_models() -> None:
  device = _resolve_device()
  for name in settings.models:
    repo_or_size = settings.aliases.get(name, name)
    models[name] = WhisperModel(
      repo_or_size,
      device=device,
      compute_type=settings.compute_type
    )
```

`_resolve_device()` automatically picks the best available device:
- If CUDA is available → uses GPU (`cuda`).  
- Otherwise → falls back to CPU.

### 2) Environment Configuration

Tune service behavior with environment variables (defaults shown):

```json
{
  "WHISPER_MODELS": "small,medium,large-v3,large-v3-turbo",
  "WHISPER_DEVICE": "auto",
  "WHISPER_COMPUTE_TYPE": "int8",
  "WHISPER_BEAM_SIZE": "5",
  "WHISPER_VAD_FILTER": "true",
  "WHISPER_ALIASES": {
    "large-v3-turbo": "mobiuslabsgmbh/faster-whisper-large-v3-turbo"
  }
}
```

### 3) Transcription Workflow

```ini
# app.main (excerpt)
segments, info = models[model_key].transcribe(
  tmp_path,
  beam_size=settings.beam_size,
  vad_filter=settings.vad_filter,
  language=settings.language
)
text = " ".join(seg.text.strip() for seg in segments).strip()
response = {"text": text, "language": info.language}
```

This design keeps the service **stateless** and horizontally scalable.

### 4) Endpoints

Predefined routes for convenience:
- `/transcribe-small`
- `/transcribe-medium`
- `/transcribe-large-v3`
- `/transcribe-large-v3-turbo`

Generic route for flexibility:
- `/transcribe/{model}`

### 5) Health Endpoint

```ini
# app.main (excerpt)
@app.get("/health")
def health():
  return {"status": "ok", "loaded_models": list(models.keys())}
```

### 6) Resource Management

Temporary files are deleted after processing. Model artifacts are cached on a 
shared Docker volume to avoid repeated downloads across restarts.

```ini
SERVICE_PORT=8080
CACHE_VOLUME=models-cache
LIBRARY=faster-whisper
PRELOADED=small,medium,large-v3,large-v3-turbo
```

---

## API Gateway Deep Dive

The **API Gateway** provides a unified, secure entry point for clients to
interact with the transcription service.  
It acts as a bridge between external requests and the internal Whisper models, 
ensuring consistent authentication and controlled access.

### 1) Purpose and Design

Handles:
- **Authentication** via Bearer tokens.  
- **Model whitelisting** to control which models can be accessed.  
- **Request forwarding** with file streaming to the internal service.  
- **Error handling** and consistent HTTP responses.

### 2) Core Structure

```ini
api-gateway/
└── app/
    ├── main.py
    └── settings.py
```

- **main.py:** FastAPI routes (`/transcribe/*`, `/health`) + forwarding logic 
  using `httpx`.  
- **settings.py:** Loads `API_KEY`, `MODEL_URL`, `ALLOWED_MODELS` via Pydantic.

### 3) Authentication Flow

Every transcription request must include a Bearer token in the `Authorization`
header.  
The gateway compares it to the configured `API_KEY`.

```ini
# app.main (excerpt)
async def check_auth(authorization: str | None = Header(default=None)):
  if settings.api_key:
    if not authorization or not authorization.startswith("Bearer "):
      raise HTTPException(status_code=401, detail="Missing bearer token")
    token = authorization.removeprefix("Bearer ")
    if token != settings.api_key:
      raise HTTPException(status_code=403, detail="Invalid token")
```

### 4) Forwarding Logic

```ini
# app.main (excerpt)
async def _forward(file: UploadFile, path: str):
  files = {
    "file": (file.filename, await file.read(), file.content_type or "application/octet-stream")
  }
  async with httpx.AsyncClient(timeout=300) as client:
    r = await client.post(f"{settings.service_url}{path}", files=files)
  if r.status_code != 200:
    raise HTTPException(status_code=r.status_code, detail=r.text)
  return JSONResponse(r.json())
```

### 5) Routes Overview

- `/transcribe/small`  
- `/transcribe/medium`  
- `/transcribe/large-v3`  
- `/transcribe/large-v3-turbo`  
- `/transcribe/{model}` (generic, only if in `ALLOWED_MODELS`)  
- `/health`

### 6) Settings and Environment Variables

```json
{
  "MODEL_URL": "http://whisper-service:8080",
  "API_KEY": "change-me",
  "ALLOWED_MODELS": "small,medium,large-v3,large-v3-turbo"
}
```

```ini
API_GATEWAY_PORT=8081
DEPENDENCY=httpx
FRAMEWORK=fastapi
```

---

## Docker & Compose

Both services (the API Gateway and the Whisper Service) are orchestrated 
through **Docker Compose**, enabling quick deployment and isolated execution.  
To understand how Compose orchestrates multi-container applications, see the 
official docs.

### 1) Compose Overview

```ini
services:
  whisper-service:
    build: ./whisper-service
    image: local/whisper-service:1.1.0
    environment:
      WHISPER_MODELS: "small,medium,large-v3,large-v3-turbo"
      WHISPER_DEVICE: "auto"
      WHISPER_COMPUTE_TYPE: "int8"
      WHISPER_BEAM_SIZE: "5"
      WHISPER_VAD_FILTER: "true"
    ports:
      - "8080:8080"
    volumes:
      - models-cache:/root/.cache
    healthcheck:
      test: ["CMD", "curl", "-fsS", "http://localhost:8080/health"]
      interval: 30s
      timeout: 10s
      retries: 30
      start_period: 300s

  api-gateway:
    build: ./api-gateway
    image: local/transcription-gateway:1.1.0
    environment:
      MODEL_URL: "http://whisper-service:8080"
      API_KEY: "change-me"
      ALLOWED_MODELS: "small,medium,large-v3,large-v3-turbo"
    ports:
      - "8081:8081"
    depends_on:
      whisper-service:
        condition: service_healthy

volumes:
  models-cache:
```

### 2) Running the Services

```ini
docker compose up --build
```

Once both containers are healthy:
- **Gateway:** http://localhost:8081  
- **Whisper Service:** http://localhost:8080

### 3) Health Verification

```ini
curl http://localhost:8081/health
curl http://localhost:8080/health
```

Expected response:

```json
{"gateway": "ok", "model": {"status": "ok", "loaded_models": ["small","medium","large-v3","large-v3-turbo"]}}
```

### 4) Persistent Model Cache

The `models-cache` volume ensures that once models are downloaded, subsequent 
startups are faster: ideal for production deployments and CI/CD pipelines.

### 5) Common Deployment Notes

```ini
# Rebuild from scratch
docker compose build --no-cache
# Detach from console
docker compose up -d
# Tail logs
docker compose logs -f
EXPOSED_PORTS=8080,8081
VOLUME=models-cache
```

---

## Trying It Out (cURL and Python Client)

With both containers running, you can test the transcription endpoints via 
**cURL** or a simple **Python** script.

### 1) Testing with cURL

```ini
curl -X POST "http://localhost:8081/transcribe/small" \
  -H "Authorization: Bearer change-me" \
  -F "file=@sample.mp3"
```

Expected output:

```json
{"text": "Hello and welcome to our demo on building a transcription API using 
Whisper and FastAPI.","language":"en"}
```

Another model:

```ini
curl -X POST "http://localhost:8081/transcribe/large-v3-turbo" \
  -H "Authorization: Bearer change-me" \
  -F "file=@meeting_audio.wav"
```

### 2) Testing with Python

```ini
import requests

API_URL = "http://localhost:8081/transcribe/small"
TOKEN = "change-me"

with open("meeting.mp3", "rb") as f:
  files = {"file": ("meeting.mp3", f, "audio/mpeg")}
  headers = {"Authorization": f"Bearer {TOKEN}"}
  response = requests.post(API_URL, headers=headers, files=files)

print(response.status_code)
print(response.json())
```

Sample response:

```json
{"text": "Good morning everyone, let's start our meeting.","language":"en"}
```

### 3) Handling Errors

Check for: missing/invalid token (`401/403`), unsupported media type (`400`), 
model not allowed (`404`), or timeouts (`5xx`).  
All responses include clear JSON-formatted error messages.

```json
{"detail": "Invalid token"}
```

---

## Handling Videos and Common Formats

The system supports a wide range of audio and video formats out of the box, 
thanks to **FFmpeg** (installed in the Whisper Service container).

### 1) Supported Formats

- **Audio:** `.mp3`, `.wav`, `.m4a`, `.ogg`, `.webm`  
- **Video:** `.mp4`, `.mov`, `.mkv`, `.webm`  

The pipeline extracts the audio track before running the model: **no manual 
pre-processing required**.

### 2) Converting Unsupported Formats

```ini
ffmpeg -i input.mov -ar 16000 -ac 1 -c:a pcm_s16le output.wav
```

### 3) Working with Large Files

Prefer `large-v3` or `large-v3-turbo` for higher accuracy; ensure enough 
memory/GPU; optionally limit upload size in Uvicorn:

```ini
CMD ["uvicorn","app.main:app","--host","0.0.0.0","--port","8081","--limit-max-request-size","200"]
```

### 4) Language Detection and Multilingual Transcription

```ini
WHISPER_LANGUAGE=pt
```

### 5) Example: Transcribing a Video File

```ini
curl -X POST "http://localhost:8081/transcribe/large-v3-turbo" \
  -H "Authorization: Bearer change-me" \
  -F "file=@webinar.mp4"
```

Expected response:

```json
{"text":"Today we're exploring how to build transcription systems using Whisper 
and Docker.","language":"en"}
```

### 6) Tip: Speed vs Accuracy

| Model           | Speed | Accuracy | Ideal Use Case                     |
|-----------------|:-----:|:--------:|------------------------------------|
| small           |   ⚡   |   🟡     | Real-time or lightweight envs      |
| medium          |  ⚖️   |   🟢     | General purpose                    |
| large-v3        |  🐢   |   🟢🟢   | Long or noisy recordings           |
| large-v3-turbo  |  ⚡⚡   |   🟢     | Near real-time apps (GPU)          |

```ini
MODEL_CHOICE=large-v3-turbo
USE_CASE=near-real-time-transcription
```

---

## Tuning for Accuracy & Speed

These adjustments let you optimize performance from lightweight edge deployments 
to GPU-backed servers.

### 1) Choosing the Right Model

```ini
# Example choosing a model
curl -X POST "http://localhost:8081/transcribe/large-v3-turbo" \
  -H "Authorization: Bearer change-me" \
  -F "file=@podcast.mp3"
```

### 2) Compute Type and Device

```ini
# GPU-optimized example
WHISPER_DEVICE=cuda
WHISPER_COMPUTE_TYPE=float16
```

### 3) Beam Search and VAD

```ini
WHISPER_BEAM_SIZE=8
WHISPER_VAD_FILTER=true
```

### 4) Language Parameter

```ini
WHISPER_LANGUAGE=en
```

### 5) Measuring Performance

```ini
time curl -X POST "http://localhost:8081/transcribe/small" \
  -H "Authorization: Bearer change-me" \
  -F "file=@audio.wav"
```

---

## Security & Operational Tips

### 1) API Security

Use the Gateway as the only public entry point. Protect it with authentication, 
rate limiting, and HTTPS.

```ini
API_KEY=my-secret-token
```

Best practices: rotate keys, store secrets via env/secret manager, and use a 
reverse proxy (e.g., Nginx/Traefik).

### 2) Resource Limits

```ini
# Compose-style hint (conceptual)
deploy:
  resources:
    limits:
      cpus: "2.0"
      memory: "8G"
```

### 3) Timeout Management

```ini
# In the API Gateway
httpx.AsyncClient(timeout=360)
```

### 4) Monitoring and Healthchecks

```ini
curl http://localhost:8081/health
curl http://localhost:8080/health
```

### 5) Logging and Observability

```ini
LOG_LEVEL=info
ENABLE_TRACING=true
```

### 6) Backup and Recovery

Preserve the `models-cache` volume to avoid re-downloading models after 
restarts; snapshot for faster redeployments.

---

## Troubleshooting

### 1) Slow First Request

Models are downloaded/loaded into memory on the first run; subsequent runs are 
fast thanks to caching.

```ini
docker compose up
# first startup downloads models to /root/.cache
```

### 2) CUDA or GPU Not Detected

```ini
sudo apt install nvidia-container-toolkit
docker run --gpus all local/whisper-service:1.1.0
```

### 3) Out of Memory (OOM)

```ini
WHISPER_COMPUTE_TYPE=int8
```

### 4) Invalid Token or 403 Errors

```ini
-H "Authorization: Bearer change-me"
```

### 5) 415 Unsupported Media Type

```ini
ffmpeg -i input.mov -ar 16000 -ac 1 -c:a pcm_s16le output.wav
```

### 6) Empty Transcriptions

Check input volume/duration; validate with `ffprobe`; try another model.

### 7) Gateway Timeout or 5xx Errors

Increase Gateway timeout or use a smaller model.

### 8) Healthcheck Fails in Compose

Increase `start_period` or retries.

```ini
healthcheck:
  test: ["CMD", "curl", "-fsS", "http://localhost:8080/health"]
  interval: 30s
  timeout: 10s
  retries: 30
  start_period: 600s
```

---

## What’s Next

- **Asynchronous Transcription:** queue jobs, return `job_id`, process in 
  background, poll for results (e.g., Celery + Redis).  
- **Batch Processing:** upload and process multiple files concurrently.  
- **Timestamps/Subtitles:** output segments with timestamps; generate `.srt`/`.vtt`.  
- **Speaker Diarization:** separate speakers (e.g., `pyannote.audio`).  
- **Multilingual Translation:** integrate models like NLLB or M2M100.  
- **Real-Time Streaming:** WebSockets or SSE for partial results.  
- **Cloud Deployment:** AWS ECS, GCP Cloud Run, Azure Container Apps.  
- **Observability:** Prometheus/Grafana dashboards for throughput and latency.

---

## Conclusion

Building a transcription system with Whisper, FastAPI, and Docker isn’t just a 
proof of concept: it’s a blueprint for scalable, real-world AI infrastructure. 
Through this architecture, you’ve combined modular microservices and efficient 
GPU utilization into a cohesive solution that’s both robust and maintainable.

Beyond technical achievement, this approach shows how open-source tools enable 
teams to deploy production-grade speech recognition pipelines without relying 
on external APIs or costly proprietary models. With a few configuration tweaks, 
the same foundation can serve countless use cases: from podcast indexing and 
meeting transcription to multilingual content processing.

As you extend this project, consider adding real-time streaming, translation 
layers, and cloud deployment automation. These next steps will transform your 
transcription API into a powerful, intelligent, and fully self-hosted audio 
intelligence platform ready for modern AI-driven applications.
