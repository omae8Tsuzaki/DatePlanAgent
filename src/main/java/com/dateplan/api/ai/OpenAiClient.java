package com.dateplan.api.ai;

import com.dateplan.AppConfig;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * <p>OpenAI API を使用する AiClient 実装クラス。</p>
 */
@Component
public class OpenAiClient implements AiClient {
    private final OpenAIClient client;

    public OpenAiClient(AppConfig appConfig) {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(appConfig.getOpenAiApiKey())
                .build();
    }

    /**
     * <p>システムプロンプトとユーザープロンプトを受け取り、OpenAI API を呼び出して応答を非同期で返す。</p>
     *
     * @param systemPrompt システムプロンプト
     * @param userPrompt   ユーザープロンプト
     * @param modelId 使用するモデル ID
     * @return AIの応答テキストを含む CompletableFuture
     */
    @Override
    public CompletableFuture<String> chat(String systemPrompt, String userPrompt, String modelId) {
        return CompletableFuture.supplyAsync(() -> {
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model(modelId)
                    .addSystemMessage(systemPrompt)
                    .addUserMessage(userPrompt)
                    .build();

            ChatCompletion completion = client.chat().completions().create(params);

            return completion.choices().stream()
                    .findFirst()
                    .flatMap(choice -> choice.message().content())
                    .orElse("プランの生成に失敗しました。");
        });
    }
}
