FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt ./

RUN pip install --no-cache-dir -r requirements.txt

COPY app ./app

ENV MODEL_URL=http://whisper-service:8080 \
    API_KEY= \
   ALLOWED_MODELS=small,medium,large-v3,large-v3-turbo

EXPOSE 8081

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8081"]