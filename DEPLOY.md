# DocFast AI - Guia de Deploy

## Pré-requisitos

- Conta no GitHub com o repositório do projeto
- Conta no Vercel (https://vercel.com) - grátis
- Conta no Railway (https://railway.app) - grátis inicial

## 1. Deploy do Backend (Railway)

### 1.1. Criar conta e projeto

1. Acesse https://railway.app
2. Login com GitHub
3. Clique "New Project"
4. Escolha "Deploy from GitHub repo"
5. Selecione o repositório `docfastAI`

### 1.2. Adicionar PostgreSQL

1. No projeto, clique "+ New"
2. Escolha "Database" → "Add PostgreSQL"
3. Aguarde provisionamento (~1 min)

### 1.3. Configurar variáveis de ambiente

No serviço do backend, vá em "Variables" e adicione:

```
SPRING_PROFILES_ACTIVE=prod
OPENAI_API_KEY=sk-proj-luONVa...
MERCADOPAGO_ACCESS_TOKEN=APP_USR-...
FRONTEND_URL=https://docfast.vercel.app
CORS_ALLOWED_ORIGIN=https://docfast.vercel.app
DATABASE_URL=${{Postgres.DATABASE_URL}}
```

### 1.4. Deploy

- Railway detecta o Dockerfile automaticamente
- Deploy acontece em ~3-5 minutos
- URL gerada: `https://docfast-api.up.railway.app` (exemplo)

## 2. Deploy do Frontend (Vercel)

### 2.1. Criar projeto

1. Acesse https://vercel.com
2. Login com GitHub
3. Clique "Add New..." → "Project"
4. Import repositório `docfastAI`

### 2.2. Configurar build

- Framework Preset: **Other** (ou Angular se disponível)
- Root Directory: `apps/web`
- Build Command: `npm run build`
- Output Directory: `dist/docfast-web/browser`

### 2.3. Variáveis de ambiente

Em "Environment Variables":

```
NODE_ENV=production
```

### 2.4. Deploy

- Deploy automático acontece
- URL gerada: `https://docfast.vercel.app` (exemplo)

## 3. Atualizar configurações

### 3.1. Backend: atualizar FRONTEND_URL

No Railway, edite a variável:

```
FRONTEND_URL=https://docfast.vercel.app
CORS_ALLOWED_ORIGIN=https://docfast.vercel.app
```

### 3.2. Frontend: atualizar API_URL

Edite `apps/web/src/environments/environment.prod.ts`:

```typescript
export const environment = {
  production: true,
  apiUrl: "https://docfast-api.up.railway.app",
};
```

Commit e push → Vercel redeploy automático

### 3.3. Webhook do Mercado Pago

No código, atualize em `CheckoutService.java`:

```java
.notificationUrl("https://docfast-api.up.railway.app/api/webhooks/mercadopago")
```

## 4. Domínio customizado (opcional)

### 4.1. Comprar domínio

- Registro.br: `docfast.ai` (~R$40/ano)

### 4.2. Configurar DNS

No Vercel:

1. Project Settings → Domains
2. Add `docfast.ai`
3. Copie os nameservers
4. No Registro.br, aponte DNS para Vercel

No Railway:

1. Settings → Networking → Custom Domain
2. Add `api.docfast.ai`
3. Adicione CNAME no DNS apontando pra Railway

## 5. Validar deploy

### Checklist:

- [ ] Frontend carrega em `https://docfast.vercel.app`
- [ ] Upload de PDF funciona
- [ ] Backend responde em `https://docfast-api.up.railway.app/actuator/health`
- [ ] Smart Gate (2ª análise) captura email
- [ ] Paywall (3ª análise) redireciona pro Mercado Pago
- [ ] H2 Console: `https://docfast-api.up.railway.app/h2-console` (verificar dados salvos)

## 6. Custos esperados

### Tier grátis (até ~100 usuários/dia):

- Vercel: R$0
- Railway: R$0 (primeiro mês)
- PostgreSQL Railway: R$0 (até 500MB)

### Após tier grátis:

- Railway: ~$5-7/mês (R$25-35)
- Neon PostgreSQL: $19/mês (R$95) se passar de 0.5GB
- **Total: R$25-130/mês**

## 7. Próximos passos

Após validar o funil:

1. Adicionar Redis (Upstash grátis até 10k req/dia)
2. Configurar rate limiting (Bucket4j + Redis)
3. Adicionar PostHog analytics
4. Configurar GA4 conversion tracking
5. Habilitar domínio customizado
