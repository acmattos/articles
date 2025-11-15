from pydantic import BaseModel
from typing import List, Dict, Optional
import os, json

class Settings(BaseModel):
    models: List[str] = [m.strip() for m in os.getenv("WHISPER_MODELS", "small,medium,large-v3").split(",") if m.strip()]
    compute_type: str = os.getenv("WHISPER_COMPUTE_TYPE", "int8")
    beam_size: int = int(os.getenv("WHISPER_BEAM_SIZE", "5"))
    vad_filter: bool = os.getenv("WHISPER_VAD_FILTER", "true").lower() == "true"
    language: Optional[str] = os.getenv("WHISPER_LANGUAGE")

    # NEW: alias -> HF repo id (CT2)
    aliases: Dict[str, str] = {}
    def __init__(self, **data):
        super().__init__(**data)
        raw = os.getenv("WHISPER_ALIASES", "")
        if raw:
            try:
                self.aliases = json.loads(raw)
            except Exception:
                self.aliases = {}

settings = Settings()
