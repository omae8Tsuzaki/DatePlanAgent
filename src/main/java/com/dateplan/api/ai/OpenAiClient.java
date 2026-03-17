package com.dateplan.api.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * <p>OpenAI APIを使用するAiClient実装クラス。</p>
 */
@Component
public class OpenAiClient implements AiClient {
    private final OpenAIClient client;

    public OpenAiClient(@Value("${openai.api.key}") String apiKey) {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
    }

    /**
     * <p>システムプロンプトとユーザープロンプトを受け取り、OpenAI APIを呼び出して応答を非同期で返す。</p>
     *
     * @param systemPrompt システムプロンプト
     * @param userPrompt ユーザープロンプト
     * @return AIの応答テキストを含むCompletableFuture
     */
    @Override
    public CompletableFuture<String> chat(String systemPrompt, String userPrompt) {
        return CompletableFuture.supplyAsync(() -> {
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model("gpt-5-nano-2025-08-07")
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
