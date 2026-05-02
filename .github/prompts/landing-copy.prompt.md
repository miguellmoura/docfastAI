---
description: "Gera 3 variações de copy de landing page DocFast AI focadas numa persona/intenção de busca específica. Use para criar landings novas ou A/B testar headline, sub e CTA."
mode: agent
---

# Landing Copy Generator

Gere **3 variações completas de copy de landing page** para o DocFast AI, otimizadas para a seguinte intenção/persona:

**Intenção / persona:** ${input:intencao:Ex: estudante buscando "resumir pdf grátis" ou vendedor buscando "tirar texto de foto"}

## Antes de gerar
1. Leia [docs/business.md](../../docs/business.md) para entender preço, ICP e proposta.
2. Leia [docs/brand-voice.md](../../docs/brand-voice.md) para tom, glossário e modelos.
3. Leia [docs/sales-strategy.md](../../docs/sales-strategy.md) para estrutura padrão de landing.

## Cada variação deve conter

```
### Variação [A/B/C] — [tese curta da abordagem, ex: "Dor do tempo perdido"]

**H1 (≤ 8 palavras):**
**Subtítulo (≤ 18 palavras):**
**CTA principal (1-3 palavras):**
**Selo abaixo do CTA (3 itens curtos com ✅):**

**Bloco "Como funciona" (3 passos, ≤ 5 palavras cada):**
1.
2.
3.

**Headline da prova social (1 frase com número):**

**FAQ (5 perguntas, foco em objeções dessa persona):**
1. P: ... R: ...
...

**Headline do CTA final (≤ 10 palavras):**

**Tese / por que funciona pra essa persona (1 parágrafo curto):**
```

## Regras
- Português do Brasil, "você", sem jargão técnico.
- Cada variação tem **uma tese diferente** (ex: A=tempo, B=medo de perder, C=facilidade).
- Headline reflete a query/intenção do usuário (alto Quality Score em Ads).
- Nunca prometer o que o produto não faz no MVP.
- Preços e limites devem bater com [docs/business.md](../../docs/business.md).

## Saída final
Após as 3 variações, recomende **qual variação testar primeiro** e por quê (1 parágrafo, baseado na persona).
