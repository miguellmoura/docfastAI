# DocFast AI — Features (estado atual)

Legenda: 🟢 pronto · 🟡 em andamento · ⚪ não iniciado · 🔵 backlog (pós-MVP)

## MVP (deadline 30/07)

### Upload e validação
- 🟢 Upload drag-and-drop (PDF, JPG, PNG, WEBP)
- 🟢 Validação de MIME real no servidor
- 🟢 Limite de tamanho (10 MB no MVP)
- 🟢 Mensagem de erro amigável para arquivo inválido
- ⚪ Detecção de PDF escaneado (sem texto extraível) → sugerir OCR

### Processamento
- 🟢 Extração de texto de PDF (Apache PDFBox 3)
- ⚪ OCR client-side com Tesseract.js (PT-BR)
- ⚪ Fallback OCR Google Vision quando confiança < 60%
- 🟢 Resumo com OpenAI `gpt-4o-mini` (Java SDK)
- ⚪ Chunking + consolidação para PDFs longos com `gpt-4o`
- ⚪ Cache por hash SHA-256 (24h, Redis via Spring Data Redis)
- 🟡 Tela de progresso com 3 estados visíveis (spinner único por enquanto)

### Resultado
- 🟢 Card de resultado formatado
- 🟡 Botão copiar (UI pronta, falta clipboard API)
- ⚪ Botão baixar `.txt`
- 🟢 Botão "nova análise"

### Auth e conta
- 🟢 Smart Gate: 1ª anônima · 2ª pede e-mail · 3ª mostra paywall (localStorage + POST /api/leads)
- ⚪ Captcha invisível (Cloudflare Turnstile) na 2ª análise
- ⚪ Magic link via Resend (Spring Boot + WebClient)
- ⚪ JWT em cookie httpOnly + SameSite=Lax + Secure
- ⚪ Página `/conta` (saldo, plano, histórico 7 dias)
- ⚪ Botão opt-in "Salvar resumo no e-mail" no resultado

### Monetização
- 🟢 Paywall antes da 3ª análise (plano LIGHT R$9 avulso via Checkout Pro)
- 🟢 Checkout Mercado Pago (Checkout Pro PIX/cartão integrado)
- 🟡 Webhook MP processando pagamentos (HMAC implementado, falta token real pra testar)
- ⚪ Planos PRO/POWER recorrentes (adicionar Preapproval API após validar LIGHT)
- ⚪ Sistema de créditos (requer auth primeiro)
- ⚪ Crédito avulso R$9 / 15 análises (Mercado Pago Checkout Pro)
- ⚪ Plano Pro R$19/mês / 80 análises (Preapproval MP)
- ⚪ Plano Power R$39/mês / 300 análises (Preapproval MP)
- ⚪ Webhook MP (HMAC) → débito/recarga atômicos no Postgres
- ⚪ Reset mensal de créditos do plano (job agendado)

### Operacional
- ⚪ PostHog (eventos de funil)
- ⚪ GA4 + tag de conversão
- ⚪ E-mail transacional (compra + magic link)
- ⚪ Rate limit por IP e por usuário
- ⚪ Página de privacidade + termos
- ⚪ Página de FAQ (5-7 perguntas)

---

## Pós-MVP (backlog priorizado)

### Mês 2 — aumentar conversão e LTV
- 🔵 Plano anual (35% off)
- 🔵 Cupom de desconto para abandono de checkout
- 🔵 E-mail de win-back (3, 7, 14 dias após paywall sem compra)
- 🔵 "Sugestão de uso" no resultado ("transformar em checklist", "extrair datas")
- 🔵 PWA instalável
- 🔵 Indique e ganhe (1 indicação = 5 créditos)

### Mês 3 — expansão de uso
- 🔵 Múltiplos arquivos na mesma análise
- 🔵 Histórico permanente + busca
- 🔵 Compartilhar resultado por link
- 🔵 Exportar `.docx` e `.pdf` (resultado formatado)
- 🔵 Integração Google Drive / Dropbox

### Mês 4+ — escalar
- 🔵 API pública (B2B leve)
- 🔵 Conta de equipe (2-5 usuários)
- 🔵 Bot WhatsApp (envia arquivo, recebe resumo)
- 🔵 App mobile nativo (se PWA não converter)
