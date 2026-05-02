# DocFast AI — Instruções do Projeto

Este repositório é o SaaS **DocFast AI**: ferramenta minimalista de **resumo de PDFs** e **OCR/extração de texto de imagens** com IA, para pessoas comuns.

## Princípios inegociáveis

1. **Lucro rápido > engenharia bonita.** Toda decisão técnica passa pelo filtro: "isso aproxima ou afasta a 1ª venda?"
2. **MVP é MVP.** Não inventar features fora do escopo travado em [docs/scope.md](../docs/scope.md). Ideias novas vão pro backlog, não pro código.
3. **Margem é sagrada.** Custo de IA é o COGS principal. Sempre que possível: cache por hash de arquivo, truncamento inteligente, modelos baratos para tarefas simples.
4. **Mobile-first sempre.** Maioria do tráfego vem de Google Ads no celular. Se quebra no mobile, está quebrado.
5. **Tempo até "aha" < 10 segundos.** Da home ao resultado entregue.

## Contexto de negócio (leitura obrigatória ao iniciar tarefa)

- [docs/business.md](../docs/business.md) — modelo, preços, ICP, métricas-alvo
- [docs/scope.md](../docs/scope.md) — escopo do MVP (deadline 30/07)
- [docs/features.md](../docs/features.md) — lista de features e estado
- [docs/sales-strategy.md](../docs/sales-strategy.md) — funil, ads, paywall
- [docs/brand-voice.md](../docs/brand-voice.md) — tom de voz do produto

## Stack travada (default — só mude se houver motivo MUITO forte)

### Frontend

- **Angular 17+** standalone components + signals + novo control flow (`@if`, `@for`)
- **TypeScript strict** + Tailwind CSS
- Estado via signals + serviços `providedIn: 'root'` (sem NgRx no MVP)
- Reactive Forms

### Backend

- **Spring Boot 3.x** + **Java 21** (records, virtual threads)
- Spring Web + Spring Data JPA + Spring Security (JWT custom) + Spring Validation
- Maven, Flyway para migrations
- PDF: **Apache PDFBox 3**
- IA: **OpenAI Java SDK** (`gpt-4o-mini` default; `gpt-4o` p/ chunking de PDF longo)
- OCR: **Tesseract.js no client** + fallback **Google Vision** no backend
- Pagamento: **Mercado Pago SDK Java** (Checkout Pro PIX/cartão + Preapproval)
- E-mail: **Resend** via `WebClient`
- Storage: **S3** (ou R2/Supabase Storage) com TTL de 1h
- Cache/Rate limit: **Redis** + **Bucket4j**

### Outros

- **DB:** PostgreSQL 16 (Neon ou Supabase)
- **Auth:** Magic link por e-mail (Resend + JWT em cookie httpOnly + SameSite=Lax). Sem senha.
- **Hospedagem:** Front em Vercel/Cloudflare Pages; Back em Railway/Render/Fly.io (Docker)
- **Analytics:** PostHog (funil) + Google Analytics 4 (Ads conversion)

## Regras de código

- **TS:** strict, sem `any` sem comentário justificando. **Java:** evite `Object` cru.
- Validação no backend com **Bean Validation** (`@Valid`, `@NotNull`, `@Size`, `@Pattern`) em toda fronteira (controller, webhook).
- Validators do Angular no front são UX, não segurança.
- Sem segredo em código. Use `application.yml` com `${VAR}` + `.env` local + secrets no host.
- Componentes Angular: 1 responsabilidade. Se passa de ~150 linhas, quebra.
- Endpoints Spring: 1 responsabilidade. Service grosso é cheiro de design ruim — quebre por feature.
- Não criar abstração antes da 3ª duplicação.
- Erros de usuário em PT-BR claro (use `ProblemDetail` RFC 7807), sem stack trace na UI.

## Segurança (não negociar)

- Validar MIME real do arquivo (não confiar em extensão).
- Tamanho máximo aplicado no servidor, não só no cliente.
- Bloquear: `.exe`, `.zip`, `.bat`, `.sh`, `.js`, `.html`, qualquer coisa fora de `pdf/jpg/jpeg/png/webp`.
- Apagar arquivo do storage logo após processar.
- Rate limit por IP + por usuário (ex: Upstash Redis).
- Sem PII no log.

## Tom ao falar com o usuário (UI, e-mails, erros)

- Português do Brasil, informal mas profissional. "Você", nunca "tu".
- Frases curtas. Verbo no imperativo nos botões: "Enviar PDF", "Copiar resultado".
- Zero jargão técnico. Não use: "API", "endpoint", "token", "OCR" (use "leitura de imagem"), "parsing".
- Erros sempre com **próximo passo**: "Arquivo muito grande. Envie um PDF de até 10 MB."

## Ao receber uma tarefa

1. Leia o escopo em [docs/scope.md](../docs/scope.md) — está dentro? Se não, pare e questione.
2. Leia [docs/brand-voice.md](../docs/brand-voice.md) se a tarefa envolve texto visível.
3. Implemente o caminho mais curto que entrega valor.
4. Não adicione testes, docstrings ou refactor "de brinde". Só o pedido.
5. Atualize [docs/features.md](../docs/features.md) se mudou estado de feature.
