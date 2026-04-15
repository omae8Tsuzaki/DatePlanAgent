package com.dateplan.api.ai;

import java.util.concurrent.CompletableFuture;

/**
 * <p>AI API クライアントの共通インターフェース。</p>
 */
public interface AiClient {

    /**
     * <p>システムプロンプトとユーザープロンプトを受け取り、AIの応答を非同期で返す。</p>
     *
     * @param systemPrompt システムプロンプト
     * @param userPrompt   ユーザープロンプト
     * @param modelId 使用するAIモデルのID
     * @return AIの応答テキストを含むCompletableFuture
     */
    CompletableFuture<String> chat(String systemPrompt, String userPrompt, String modelId);
}
