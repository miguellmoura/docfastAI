# DocFast AI — Escopo do MVP

**Deadline:** 30/07
**Objetivo único:** validar se pessoas pagam por resumo de PDF e OCR de imagem.

## Regra de ouro
Se uma feature **não move** uma destas 3 métricas, fica fora:
1. Visitante → e-mail capturado
2. E-mail → 1ª análise entregue
3. Análise grátis → 1ª compra

---

## ✅ DENTRO do MVP

### Frontend (Angular 17+ standalone)
- [ ] Landing page única, mobile-first, com upload no hero
- [ ] Tela de arquivo carregado (preview + ações)
- [ ] Tela de processando (estados: lendo → extraindo → gerando)
- [ ] Tela de resultado (card com copiar/baixar TXT/nova análise)
  - [ ] Smart Gate: 1ª análise anônima; modal de e-mail antes da 2ª
  - [ ] Modal de paywall antes da 3ª análise
  - [ ] Botão "Salvar resumo no meu e-mail" no resultado (opt-in voluntário)
- [ ] Página `/checkout/sucesso` e `/checkout/cancelado`
- [ ] Dashboard mínimo do usuário (`/conta`): saldo de análises, plano atual, histórico últimos 7 dias

### Backend (Spring Boot 3 + Java 21)
- [ ] Upload validado (MIME real via Apache Tika, tamanho, extensão whitelist)
- [ ] Extração de texto de PDF (Apache PDFBox 3)
- [ ] OCR de imagem (Tesseract.js no client → fallback Google Vision se confiança < 60%)
- [ ] Resumo via OpenAI (`gpt-4o-mini` < 8k tokens; `gpt-4o` com chunking acima)
- [ ] Cache de resultado por hash SHA-256 do arquivo (24h)
- [ ] Rate limit por IP + por usuário (Upstash Redis)
- [ ] Magic link auth (Resend + JWT cookie httpOnly)
- [ ] Sistema de créditos (1 = 1 análise) + débito atômico
- [ ] Webhook Mercado Pago (créditos avulsos + assinatura mensal)

### Operacional
- [ ] PostHog: eventos de funil (`upload_started`, `email_captured`, `analysis_completed`, `paywall_shown`, `checkout_started`, `purchase_completed`)
- [ ] GA4: conversion tag em `purchase_completed`
- [ ] E-mail transacional: confirmação de compra + magic link (Resend)
- [ ] Política de privacidade + termos de uso (página estática)

---

## ❌ FORA do MVP (backlog)

- Conversão entre formatos (PDF → Word, etc.)
- Juntar / dividir / comprimir PDF
- Editor de PDF
- "Converse com seu PDF" (chat)
- App mobile nativo
- Plano anual (lança no mês 2)
- Plano enterprise / faturamento por NF
- Equipes / contas multi-usuário
- API pública
- Integração Google Drive / Dropbox / WhatsApp
- Histórico permanente / pasta de favoritos
- Tradução, reescrita, formatação avançada
- Edição inline do resultado
- Múltiplos arquivos por análise
- Dark mode

---

## Critério de "pronto" do MVP

✅ Pessoa real:
1. Chega via Google Ads no celular.
2. Sobe um PDF (sem cadastro).
3. Recebe resumo em < 15s.
4. Sobe um 2º arquivo → cadastra e-mail (magic link).
5. Recebe resumo do 2º.
6. Tenta um 3º → vê paywall.
7. Paga R$9 (PIX) ou assina R$19/mês.
8. Recebe e-mail de confirmação.
9. Volta no dia seguinte com magic link e usa créditos.

Sem crash, sem confusão, sem suporte humano.
