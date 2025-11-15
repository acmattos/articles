from fastapi import FastAPI, File, UploadFile, HTTPException, APIRouter
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from typing import Optional, Dict
from .settings import settings
from faster_whisper import WhisperModel
import tempfile
import os

app = FastAPI(title="Whisper Service", version="1.0.0")

# Registry of loaded models
models: Dict[str, WhisperModel] = {}

def _resolve_device() -> str:
    device = os.getenv("WHISPER_DEVICE", "auto")
    if device == "auto":
        return "cuda" if os.getenv("CUDA_VISIBLE_DEVICES") not in (None, "") else "cpu"
    return device

@app.on_event("startup")
def load_models() -> None:
    device = _resolve_device()
    for name in settings.models:
        repo_or_size = settings.aliases.get(name, name)
        models[name] = WhisperModel(repo_or_size, device=device, compute_type=settings.compute_type)

@app.get("/health")
def health():
    return {"status": "ok", "loaded_models": list(models.keys())}

class TranscriptionResult(BaseModel):
    text: str
    language: Optional[str] = None

async def _run_transcription(file: UploadFile, model_key: str):
    if model_key not in models:
        raise HTTPException(status_code=404, detail=f"Model '{model_key}' not loaded")

    # Basic validation
    if not file.content_type or not file.content_type.startswith("audio"):
        allowed_ext = (".mp3", ".wav", ".m4a", ".mp4", ".webm", ".ogg")
        if not any((file.filename or "").endswith(ext) for ext in allowed_ext):
            raise HTTPException(status_code=400, detail="Unsupported media type. Provide an audio file.")

    tmp_path = None
    try:
        content = await file.read()
        if not content:
            raise HTTPException(status_code=400, detail="Empty file upload")

        with tempfile.NamedTemporaryFile(delete=False) as tmp:
            tmp.write(content)
            tmp_path = tmp.name

        segments, info = models[model_key].transcribe(
            tmp_path,
            beam_size=settings.beam_size,
            vad_filter=settings.vad_filter,
            language=settings.language,
        )
        text = " ".join(seg.text.strip() for seg in segments).strip()
        return JSONResponse(TranscriptionResult(text=text, language=info.language).model_dump())
    finally:
        if tmp_path:
            try:
                os.remove(tmp_path)
            except Exception:
                pass

router = APIRouter()

# Fixed endpoints for convenience
@router.post("/transcribe-small")
async def transcribe_small(file: UploadFile = File(...)):
    return await _run_transcription(file, "small")

@router.post("/transcribe-medium")
async def transcribe_medium(file: UploadFile = File(...)):
    return await _run_transcription(file, "medium")

@router.post("/transcribe-large-v3")
async def transcribe_large_v3(file: UploadFile = File(...)):
    return await _run_transcription(file, "large-v3")

@router.post("/transcribe-large-v3-turbo")
async def transcribe_large_v3_turbo(file: UploadFile = File(...)):
    return await _run_transcription(file, "large-v3-turbo")

# Generic endpoint: /transcribe/{model}
@router.post("/transcribe/{model}")
async def transcribe_model(model: str, file: UploadFile = File(...)):
    return await _run_transcription(file, model)

app.include_router(router)
