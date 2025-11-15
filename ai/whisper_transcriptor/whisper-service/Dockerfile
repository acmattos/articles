FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt ./

RUN apt-get update && apt-get install -y --no-install-recommends \
    ffmpeg curl \
    && rm -rf /var/lib/apt/lists/*

RUN pip install --no-cache-dir -r requirements.txt

COPY app ./app

# Env específicas do serviço do modelo
ENV WHISPER_MODELS="small,medium,large-v3,large-v3-turbo" \
    WHISPER_DEVICE=auto \
    WHISPER_COMPUTE_TYPE=int8 \
    WHISPER_BEAM_SIZE=5 \
    WHISPER_VAD_FILTER=true

EXPOSE 8080

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8080"]
