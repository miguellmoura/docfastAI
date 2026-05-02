# DocFast AI

Resuma PDFs e extraia texto de imagens em segundos. Sem complicação.

> **Status:** pré-MVP · **Deadline MVP:** 30/07

---

## Comece por aqui

| Documento | Pra quê |
|-----------|---------|
| [docs/business.md](docs/business.md) | Modelo, preços, ICP, métricas-alvo |
| [docs/scope.md](docs/scope.md) | O que está dentro e fora do MVP |
| [docs/features.md](docs/features.md) | Lista de features e estado atual |
| [docs/sales-strategy.md](docs/sales-strategy.md) | Funil, ads, paywall, e-mails |
| [docs/brand-voice.md](docs/brand-voice.md) | Tom de voz e copy padrão |
| [.github/copilot-instructions.md](.github/copilot-instructions.md) | Regras técnicas e princípios |

## Agentes Copilot disponíveis

| Agente | Quando usar |
|--------|-------------|
| **DocFast Strategist** ([.github/agents/docfast-strategist.agent.md](.github/agents/docfast-strategist.agent.md)) | Decisões de negócio, preço, copy de venda, priorização, ads, ROI |
| **DocFast Engineer** ([.github/agents/docfast-engineer.agent.md](.github/agents/docfast-engineer.agent.md)) | Implementação técnica, bug, integração, refactor mínimo |

## Prompts úteis

- `/landing-copy` ([.github/prompts/landing-copy.prompt.md](.github/prompts/landing-copy.prompt.md)) — gera 3 variações de copy de landing por persona/intenção.

---

## Stack (travada para o MVP)

### Frontend
- **Angular 17+** standalone + signals + control flow novo
- **TypeScript strict** + Tailwind CSS
- Reactive Forms, sem NgRx

### Backend
- **Spring Boot 3** + **Java 21** (records, virtual threads)
- Spring Web + Data JPA + Security + Validation
- Maven + Flyway
- **Apache PDFBox 3** (extração de PDF)
- **OpenAI Java SDK** (`gpt-4o-mini` / `gpt-4o`)
- **Tesseract.js** (client) → **Google Vision** (fallback servidor)
- **Mercado Pago SDK Java** (Checkout Pro + Preapproval)
- **Resend** via WebClient (e-mail)

### Infra
- **DB:** PostgreSQL 16 (Neon)
- **Storage:** S3/R2 com TTL 1h
- **Cache/Rate limit:** Redis (Upstash) + Bucket4j
- **Front:** Vercel ou Cloudflare Pages
- **Back:** Railway/Render/Fly.io (Docker)
- **Analytics:** PostHog + GA4

## Estrutura proposta

```
docfastAI/
├── apps/
│   ├── web/    ← Angular standalone
│   └── api/    ← Spring Boot 3
├── docs/
└── .github/
```

## Próximos passos imediatos

1. `ng new web --standalone --style=css --routing` em `apps/web`.
2. `spring init` (start.spring.io) para `apps/api` com: Web, JPA, Security, Validation, Flyway, PostgreSQL Driver, Lombok.
3. Schema inicial Flyway: `users`, `credits`, `analyses`, `subscriptions`.
4. Landing `/` com upload **anônimo** + endpoint `/api/analyses` retornando resumo (cache por hash).
5. Smart Gate: cookie de sessão no front; 2ª análise gera modal de e-mail; 3ª gera paywall.
6. Integrar Mercado Pago (Checkout Pro PIX) + webhook.
7. Deploy: front na Vercel, back no Railway, DB no Neon, ligar Google Ads.
