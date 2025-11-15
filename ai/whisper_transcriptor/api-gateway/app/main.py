from fastapi import FastAPI, UploadFile, File, HTTPException, Header, Depends
from fastapi.responses import JSONResponse
from .settings import settings

import httpx

app = FastAPI(title="Transcription API Gateway", version="1.0.0")

async def check_auth(authorization: str | None = Header(default=None)):
   if settings.api_key:
      if not authorization or not authorization.startswith("Bearer "):
         raise HTTPException(status_code=401, detail="Missing bearer token")
      token = authorization.removeprefix("Bearer ")
      if token != settings.api_key:
         raise HTTPException(status_code=403, detail="Invalid token")

@app.get("/health")
async def health():
   async with httpx.AsyncClient(timeout=30) as client:
      r = await client.get(f"{settings.service_url}/health")
      return {"gateway": "ok", "model": r.json()}

async def _forward(file: UploadFile, path: str):
   files = {
      "file": (
         file.filename, 
         await file.read(),
         file.content_type or "application/octet-stream"
      )
   }
   async with httpx.AsyncClient(timeout=300) as client:
      r = await client.post(f"{settings.service_url}{path}", files=files)
   if r.status_code != 200:
      raise HTTPException(status_code=r.status_code, detail=r.text)
   return JSONResponse(r.json())

# Rotas públicas fixas
@app.post("/transcribe/small")
async def transcribe_small(file: UploadFile = File(...), _=Depends(check_auth)):
   return await _forward(file, "/transcribe-small")

@app.post("/transcribe/medium")
async def transcribe_medium(file: UploadFile = File(...), _=Depends(check_auth)):
   return await _forward(file, "/transcribe-medium")

@app.post("/transcribe/large-v3")
async def transcribe_large_v3(file: UploadFile = File(...), _=Depends(check_auth)):
   return await _forward(file, "/transcribe-large-v3")

@app.post("/transcribe/large-v3-turbo")
async def transcribe_large_v3_turbo(file: UploadFile = File(...), _=Depends(check_auth)):
   return await _forward(file, "/transcribe-large-v3-turbo")

# Rota genérica opcional
@app.post("/transcribe/{model}")
async def transcribe_model(model: str, file: UploadFile = File(...), _=Depends(check_auth)):
   if model not in settings.allowed_models:
      raise HTTPException(status_code=404, detail=f"Model '{model}' not allowed")
   return await _forward(file, f"/transcribe/{model}")
