package com.dateplan.api.ai;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link OpenAiClient}のテストクラス。</p>
 *
 * <h4>対象：{@link OpenAiClient#chat(String, String)}メソッド。</h4>
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
        AiClient openAiClient = new OpenAiClientStab("dummy-api-key");

        String systemPrompt = "あなたはデートプランナーのAIアシスタントです。";
        String userPrompt = "2024年6月15日に東京でデートプランを提案してください。天気は晴れで、レストランはイタリアンがいいです。";

        //
        // 実行
        //

        CompletableFuture<String> openAiFuture =  openAiClient.chat(systemPrompt, userPrompt);
        String result = openAiFuture.get();

        //
        // 検証
        //

        assertEquals(
                "2024年6月15日に東京でのデートプランを提案します。天気は晴れですが家から出たくありません。夜は家で鍋を作りましょう。"
                ,  result
        );
    }
}
