# DocFast AI — Modelo de Negócio

## Pitch em 1 frase
Ferramenta web que **resume PDFs e extrai texto de imagens em segundos**, sem cadastro inicial, focada em pessoas comuns que recebem documentos no dia a dia.

## Promessa de valor
> "Subiu o arquivo. Recebeu o que importa. Pronto."

Tempo da home ao resultado: **menos de 10 segundos**.

## ICP (Cliente Ideal)

### Persona 1 — Estudante apertado
- 17-28 anos, fundamental/superior em curso.
- Dor: apostila/PDF de 50 páginas na véspera da prova.
- Disposição a pagar: R$5-20/mês se resolver "salvar a prova".
- Canal: Google ("resumir pdf grátis"), TikTok, Instagram.

### Persona 2 — Vendedor / corretor
- 25-50 anos, recebe contratos, propostas, fichas no WhatsApp.
- Dor: PDF/foto chegou, precisa entender e responder rápido.
- Disposição a pagar: R$20-40/mês — produto paga sozinho com 1 venda fechada mais rápido.
- Canal: Google ("extrair texto de foto"), grupos de WhatsApp, indicação.

### Persona 3 — Administrativo / freelancer / autônomo
- 25-55 anos, lida com boletos, contratos, notas, comprovantes.
- Dor: digitação manual, leitura cansativa de documentos longos.
- Disposição a pagar: R$10-30/mês.
- Canal: Google ("ocr online", "transcrever foto em texto").

## Modelo de monetização

### Free tier — Smart Gate (sem fricção no 1º contato)
- **1ª análise: 100% anônima.** Sem login, sem e-mail. Pessoa sobe arquivo, recebe resultado em <15s. Esse é o momento "aha" — não sacrificar por nada.
- **No resultado:** botão opt-in suave "💾 Salvar este resumo no meu e-mail" (voluntário).
- **2ª análise (mesma sessão, cookie+IP):** gate de e-mail obrigatório com magic link.
- **3ª análise:** paywall.
- Limites no anônimo (1ª e 2ª):
  - PDF até **5 páginas** e **5 MB**
  - Imagem até **5 MB**
- Anti-abuso:
  - Cookie + IP + fingerprint leve.
  - Rate limit por IP (3 análises anônimas/dia).
  - Captcha invisível (Cloudflare Turnstile) na 2ª análise.

### Crédito avulso
- **R$9 — 15 análises** (validade 90 dias).
- Posicionamento: "experimentar sem assinar".

### Plano Pro (carro-chefe)
- **R$19/mês — 80 análises**
- PDF até 100 páginas, 20 MB
- Histórico de 30 dias
- **Marcar como "Mais popular"** no paywall.

### Plano Power
- **R$39/mês — 300 análises**
- PDF até 200 páginas, 30 MB
- Histórico ilimitado
- Para vendedor/administrativo de uso pesado.

### Anual (lançar no mês 2, não no MVP)
- Pro Anual: R$149/ano (≈ R$12,40/mês — 35% off)
- Power Anual: R$349/ano

## Métricas-alvo (norte do produto)

| KPI | Meta MVP (mês 1) | Meta mês 3 |
|-----|------------------|------------|
| Conversão visitante → 1ª análise concluída | ≥ 70% | ≥ 85% |
| Conversão 1ª análise → e-mail capturado (opt-in + 2ª análise) | ≥ 50% | ≥ 65% |
| Conversão free → pago (em 7 dias) | ≥ 4% | ≥ 7% |
| CAC (Google Ads) | ≤ R$15 | ≤ R$10 |
| ARPU mensal | ≥ R$18 | ≥ R$22 |
| Margem bruta (após custo IA) | ≥ 70% | ≥ 80% |
| Churn mensal | ≤ 15% | ≤ 8% |
| LTV | ≥ R$60 | ≥ R$120 |

LTV/CAC alvo: **≥ 3x**.

## Estrutura de custos (estimativa por análise)

| Item | Custo médio |
|------|-------------|
| LLM resumo (gpt-4o-mini, ~3k tokens in/out) | R$0,02–0,08 |
| LLM resumo PDF longo (chunking gpt-4o) | R$0,15–0,40 |
| OCR Tesseract.js (client) | R$0 |
| OCR fallback Google Vision | R$0,03 |
| Storage temporário | desprezível |
| **Custo médio por análise** | **R$0,10** |

→ Margem confortável em qualquer plano.

## Vantagem competitiva

- **Foco brutal:** ninguém faz "só resumo de PDF + OCR" com UX de 10 segundos. Concorrentes (iLovePDF, Smallpdf) tentam fazer 30 coisas e enterram a função.
- **Português nativo:** prompts e UI 100% PT-BR, resultado adequado ao contexto brasileiro (boleto, CPF, contrato de aluguel).
- **PIX:** pagamento em 1 toque, sem cartão.
- **Sem login no primeiro contato** (depois do e-mail, é mágico).

## O que NÃO somos
- ❌ Editor de PDF
- ❌ Conversor de formatos
- ❌ Junta/divide PDF
- ❌ Chatbot genérico ("converse com seu PDF")
- ❌ Plataforma para empresas (ainda)
- ❌ App mobile nativo (PWA depois)
