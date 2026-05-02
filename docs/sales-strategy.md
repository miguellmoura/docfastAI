# DocFast AI — Estratégia de Vendas

## Funil completo

```
Google Ads / SEO
      ↓
Landing page específica por intenção
      ↓
Upload de arquivo (anônimo, sem cadastro)
      ↓
Resultado entregue em < 15s   ← MOMENTO AHA
      ↓
   [opt-in voluntário: "Salvar no e-mail"]
      ↓
Tentativa de 2ª análise → GATE DE E-MAIL (magic link)
      ↓
Resultado da 2ª
      ↓
Tentativa de 3ª análise → PAYWALL
      ↓
Compra (PIX em 1 toque)
      ↓
E-mail de boas-vindas + magic link
      ↓
Uso recorrente → renovação
      ↓
E-mail de win-back se inativo 7d
```

## Aquisição

### Google Ads (canal principal — 80% do orçamento inicial)

**Orçamento sugerido início:** R$30-50/dia (R$900-1.500/mês).
**Meta CPA:** ≤ R$15.

**Campanhas separadas por intenção:**

| Campanha | Queries-alvo | Landing |
|----------|--------------|---------|
| Resumir PDF | "resumir pdf", "resumo de pdf online", "resumir pdf grátis", "ia pra resumir pdf" | `/resumir-pdf` |
| OCR Imagem | "extrair texto de imagem", "tirar texto de foto", "ocr online", "transcrever foto em texto" | `/extrair-texto-imagem` |
| OCR Documento | "tirar texto de foto de documento", "ler texto de print", "passar foto pra texto" | `/ocr-foto-documento` |

**Regra:** headline do anúncio = query + benefício temporal.
Ex: query "resumir pdf online" → headline `Resumir PDF Online em 10s | Grátis na 1ª vez`.

**Extensões obrigatórias:**
- Sitelinks: "Como funciona", "Preços", "Privacidade"
- Snippets: "Sem cadastro · PIX · PDF até 100 páginas"

### SEO (canal de fundo — começar mês 2)

Posts-alvo (1 por semana):
1. "Como resumir um PDF longo em segundos"
2. "5 formas de extrair texto de uma imagem"
3. "Como ler um contrato de aluguel rápido"
4. "OCR online: vale a pena? Comparativo"
5. "Como estudar para a prova com PDF gigante"

CTA em todo post: bloco de upload embutido.

### Orgânico secundário (custo zero)
- TikTok / Reels: vídeos de 15s "PDF de 80 páginas resumido em 10s" — gravação de tela.
- Quora / Reddit BR: responder dúvidas com link no fim.
- Indicação: "1 indicação = 5 créditos grátis" (mês 2).

---

## Landing pages — estrutura padrão

```
[Hero]
H1: Resumir PDF Online em 10 Segundos
Sub: Envie seu PDF, receba os pontos principais prontos para copiar. Grátis na 1ª vez.
[Área de upload — botão grande]
[Selo: ✅ Sem cadastro · ✅ PIX · ✅ Seguro]

[Prova social]
"+12.430 documentos resumidos esta semana"
3 logos / depoimentos curtos (mesmo que mockados nas 2 primeiras semanas)

[Como funciona — 3 passos com ícone]
1. Envie seu PDF
2. Aguarde 10 segundos
3. Copie ou baixe o resumo

[FAQ enxuta — 5 perguntas]
- É seguro? Meu arquivo fica salvo?
- Funciona com PDF escaneado?
- Quantas páginas?
- Quanto custa depois da grátis?
- Como faço para pagar?

[CTA final repetido — área de upload de novo]
```

---

## Captura de e-mail (Smart Gate — antes da 2ª análise, não da 1ª)

**Por que aqui e não antes da 1ª:**
- Gate antes da 1ª análise derruba 30-50% da conversão de topo. Inaceitável num produto cuja prova é entregar valor em 10s.
- A 1ª análise gera o **"aha"**. Quem teve "aha" tem >70% de chance de deixar e-mail na 2ª.
- Anti-abuso real: cookie + IP + rate limit + Turnstile invisível — já filtra 80% dos abusos.

**Copy do gate (antes da 2ª análise):**
> ### Você já viu como funciona.
> Para mais análises grátis, deixe seu melhor e-mail.
> Sem spam. Sem senha. Login por link mágico.
>
> [seu@email.com]   [Receber link →]

**Opt-in voluntário no resultado da 1ª análise (custo zero, ganho alto):**
> 💾 Quer salvar este resumo? Mandamos pro seu e-mail.
> [seu@email.com]   [Salvar]

---

## Paywall (antes da 3ª análise)

**Quando aparece:** quando usuário já fez 2 análises gratuitas (1 anônima + 1 com e-mail) e tenta a 3ª.

**Estrutura:**
```
✨ Gostou? Continue resumindo sem limites.

✅ +12.430 documentos resumidos esta semana
✅ Pague com PIX em 1 toque

┌─────────────────────────────────────────┐
│  [LIGHT]                                │
│  R$9 avulso                             │
│  15 análises · validade 90 dias         │
│  [Comprar com PIX]                      │
├─────────────────────────────────────────┤
│  🔥 PRO  ← MAIS POPULAR                 │
│  R$19/mês                               │
│  80 análises · PDFs até 100 págs        │
│  Histórico de 30 dias                   │
│  [Assinar agora]                        │
├─────────────────────────────────────────┤
│  [POWER]                                │
│  R$39/mês                               │
│  300 análises · PDFs até 200 págs       │
│  Para uso intenso                       │
│  [Assinar agora]                        │
└─────────────────────────────────────────┘

🔒 Cancele quando quiser. Sem fidelidade.
```

**Regras de UX:**
- Plano do meio (Pro) com **cor diferente** e selo "Mais popular".
- Sem campo "Cupom" visível (some na desistência).
- Botão fechar discreto (X no canto).
- Não bloquear scroll do resultado anterior — ele continua atrás, gerando desejo.

---

## Reativação (e-mail)

| Gatilho | Quando | Assunto |
|---------|--------|---------|
| Abandono de paywall | +6h | "Seu resumo te esperando — leve por R$9" |
| Sem retorno após 1ª análise | +3d | "Outro PDF para resumir hoje?" |
| Win-back inativo | +7d | "5 créditos por nossa conta. Volte e use." |
| Win-back inativo | +14d | "Última chance: cupom 30% off no Pro" |

---

## Métricas para acompanhar diariamente

| Métrica | Onde | Alvo |
|---------|------|------|
| Visitantes | GA4 | crescer 10%/sem |
| Conversão visitante→análise concluída | PostHog | ≥ 70% |
| Conversão 1ª análise→e-mail | PostHog | ≥ 50% |
| Análises grátis | PostHog | volume |
| Conversão paywall→checkout | PostHog | ≥ 8% |
| Conversão checkout→pago | PostHog + MP | ≥ 60% |
| CPA Google Ads | Google Ads | ≤ R$15 |
| Receita do dia | dashboard MP | crescente |
| Custo IA do dia | OpenAI dashboard | ≤ 30% da receita |

---

## Anúncios — variações para testar (A/B)

**Resumir PDF:**
- A: `Resumir PDF Online em 10s | Grátis na 1ª Vez`
- B: `PDF Gigante? Resuma em 10 Segundos com IA`
- C: `Não Tem Tempo de Ler? Resumimos seu PDF`

**OCR Imagem:**
- A: `Tirar Texto de Foto em 5s | Sem Instalar Nada`
- B: `Foto de Documento → Texto Pronto pra Copiar`
- C: `Não Digite Mais. Tire Foto, Receba o Texto.`

**Descrição padrão:**
> Envie seu arquivo, receba o resultado pronto pra copiar. Pagamento por PIX. Cancele quando quiser.
