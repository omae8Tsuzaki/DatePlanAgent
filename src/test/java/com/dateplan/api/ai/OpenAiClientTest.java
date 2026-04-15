package com.dateplan.api.ai;

import com.dateplan.AppConfig;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link OpenAiClient}のテストクラス。</p>
 *
 * <h4>対象：{@link AiClient#chat(String, String, String)}メソッド。</h4>
 * <ul>
 *     <li>{@link #chatSuccess01()} 正常系: システムプロンプトとユーザープロンプトを使用してAIの応答が正しく返されることを確認する。</li>
 * </ul>
 */
public class OpenAiClientTest {

    /**
     * <p>正常系: システムプロンプトとユーザープロンプトを使用してAIの応答が正しく返されることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void chatSuccess01() throws Exception {

        //
        // 事前準備
        //

        // スタブ
        AppConfig appConfig = new AppConfig("dummy-discord-token", "dummy-openai-key", "dummy-hotpepper-key", "dummy-model-id");
        AiClient openAiClient = new OpenAiClientStub(appConfig);

        String systemPrompt = "あなたはデートプランナーのAIアシスタントです。";
        String userPrompt = "2024年6月15日に東京でデートプランを提案してください。天気は晴れで、レストランはイタリアンがいいです。";
        String modelId = "dummy-model-id";

        //
        // 実行
        //

        CompletableFuture<String> openAiFuture =  openAiClient.chat(systemPrompt, userPrompt, modelId);
        String result = openAiFuture.get();

        //
        // 検証
        //

        assertEquals(
                "2024年6月15日に東京でのデートプランを提案します。天気は晴れですが、家から出たくありませんね。夜は家で鍋を作りましょう。"
                ,  result
        );
    }
}
