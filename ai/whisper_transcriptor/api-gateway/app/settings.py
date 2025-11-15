from pydantic import BaseModel
import os
from typing import List

class Settings(BaseModel):
    # Evita conflito do Pydantic com "model_"
    # (Se preferir manter model_url, pode usar protected_namespaces=())
    service_url: str = os.getenv("MODEL_URL", "http://whisper-service:8080")
    api_key: str | None = os.getenv("API_KEY")
    allowed_models: List[str] = [
        m.strip() for m in os.getenv("ALLOWED_MODELS", "small,medium,large-v3,large-v3-turbo").split(",") if m.strip()
    ]

settings = Settings()
