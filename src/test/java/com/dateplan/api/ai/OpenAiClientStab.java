package com.dateplan.api.ai;

import java.util.concurrent.CompletableFuture;

public class OpenAiClientStab extends OpenAiClient implements AiClient {

    public OpenAiClientStab(String apiKey) {
        super(apiKey);
    }

    public CompletableFuture<String> chat(String systemPrompt, String userPrompt) {
        // スタブ実装: 常に固定の応答を返す
        return CompletableFuture.completedFuture(
                "2024年6月15日に東京でのデートプランを提案します。" +
                        "天気は晴れですが家から出たくありません。" +
                        "夜は家で鍋を作りましょう。"
        );
    }
}
