package com.docfast.ai;

import com.docfast.common.exception.ProcessingException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

    private final OpenAiService openAiService;
    private final String defaultModel;
    private final int maxTokens;

    public AiService(
            @Value("${app.openai.api-key}") String apiKey,
            @Value("${app.openai.default-model}") String defaultModel,
            @Value("${app.openai.max-tokens}") int maxTokens) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(90));
        this.defaultModel = defaultModel;
        this.maxTokens = maxTokens;
    }

    /**
     * Gera um resumo estruturado do texto extraído de PDF/imagem.
     */
    public String summarize(String extractedText) {
        if (extractedText == null || extractedText.isBlank()) {
            throw new ProcessingException("Nenhum texto foi encontrado no arquivo.");
        }

        // Trunca se necessário (limite seguro: ~12k chars = ~3k tokens)
        String input = extractedText.length() > 12000
                ? extractedText.substring(0, 12000) + "\n\n[...texto truncado...]"
                : extractedText;

        var systemPrompt = """
                Você é um professor particular paciente e didático. Sua tarefa é pegar qualquer documento \
                — mesmo que seja técnico, acadêmico ou especializado — e explicar o conteúdo de forma \
                clara para uma pessoa leiga.

                Regras obrigatórias:
                1. Explique O QUE o documento diz, não apenas o assunto. Se fala de mecânica quântica, \
                   explique os conceitos apresentados. Se é um contrato, explique o que cada cláusula significa. \
                   Se é um relatório, explique as conclusões e o que elas implicam.
                2. Use linguagem simples. Substitua jargão técnico por analogias do dia a dia sempre que possível.
                3. Estruture assim:
                   📌 **O que é este documento**
                   Uma frase direta sobre o tipo e propósito.

                   💡 **O que você precisa saber**
                   Explique os conceitos ou informações principais em linguagem simples. \
                   Use bullet points curtos, mas com substância — não apenas títulos vazios.

                   ✅ **Conclusão ou próximo passo**
                   O que fica de mais importante? O que o leitor deve fazer ou entender após ler?

                4. Se o documento for muito técnico, diga "Em termos simples:" antes de explicar cada conceito difícil.
                5. Nunca diga "Este documento aborda..." ou "O texto trata de...". Vá direto ao ponto.
                6. Use português do Brasil, informal e direto.
                """;

        var userPrompt = "Explique este documento para mim:\n\n" + input;

        try {
            var request = ChatCompletionRequest.builder()
                    .model(defaultModel)
                    .messages(List.of(
                            new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt),
                            new ChatMessage(ChatMessageRole.USER.value(), userPrompt)))
                    .maxTokens(maxTokens)
                    .temperature(0.3)
                    .build();

            var response = openAiService.createChatCompletion(request);
            var summary = response.getChoices().get(0).getMessage().getContent();

            log.info("Resumo gerado com sucesso. Tokens usados: {}", response.getUsage().getTotalTokens());
            return summary.trim();

        } catch (Exception e) {
            log.error("Erro ao chamar OpenAI: {}", e.getMessage(), e);
            throw new ProcessingException("Não foi possível gerar o resumo. Tente novamente.", e);
        }
    }
}
