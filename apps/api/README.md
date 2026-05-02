# DocFast AI - Backend API

Spring Boot 3.3 + Java 21

## Setup local

1. **Copie `.env.example` para `.env`** e ajuste as variáveis:
   ```bash
   cp .env.example .env
   ```

2. **PostgreSQL local** (via Docker):
   ```bash
   docker run --name docfast-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=docfast -p 5432:5432 -d postgres:16
   ```

3. **Redis local** (via Docker):
   ```bash
   docker run --name docfast-redis -p 6379:6379 -d redis:7
   ```

4. **Rode a API**:
   ```bash
   ./mvnw spring-boot:run
   ```

API disponível em: `http://localhost:8080`

## Endpoints

### `POST /api/analysis`
Envia um arquivo (PDF ou imagem) e recebe resumo.

**Request** (multipart/form-data):
- `file`: arquivo (PDF, JPG, PNG, WEBP, max 10MB)
- `type`: `SUMMARIZE` (default) ou `EXTRACT_KEY_POINTS`

**Response** (JSON):
```json
{
  "result": "...",
  "extractedChars": 12345,
  "estimatedCostBrl": 0.0017
}
```

**Teste com curl**:
```bash
curl -X POST http://localhost:8080/api/analysis \
  -F "file=@documento.pdf" \
  -F "type=SUMMARIZE"
```

## Variáveis de ambiente

Ver `.env.example` para lista completa. Principais:

- `OPENAI_API_KEY` (**obrigatório**)
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `REDIS_HOST`, `REDIS_PORT`
- `CORS_ALLOWED_ORIGIN` (frontend)
