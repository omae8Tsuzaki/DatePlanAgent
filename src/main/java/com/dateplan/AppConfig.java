package com.dateplan;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>アプリケーションの設定を管理するクラス。</p>
 */
@Component
public class AppConfig {

    /**
     * <p>Discord Botのトークン。</p>
     */
    private final String discordToken;

    /**
     * <p>OpenAI APIキー。</p>
     */
    private final String openAiApiKey;

    /**
     * <p>Hot Pepper APIキー。</p>
     */
    private final String hotPepperApiKey;

    public AppConfig(
            @Value("${discord.token}") String discordToken,
            @Value("${openai.api.key}") String openAiApiKey,
            @Value("${hotpepper.api.key}") String hotPepperApiKey) {
        this.discordToken = discordToken;
        this.openAiApiKey = openAiApiKey;
        this.hotPepperApiKey = hotPepperApiKey;
    }

    @PostConstruct
    void validate() {
        if (discordToken == null || discordToken.isBlank()) {
            throw new IllegalStateException("discord.token is not set");
        }
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            throw new IllegalStateException("openai.api.key is not set");
        }
        if (hotPepperApiKey == null || hotPepperApiKey.isBlank()) {
            throw new IllegalStateException("hotpepper.api.key is not set");
        }
    }

    /**
     * <p>Discord Botのトークンを取得する。</p>
     *
     * @return Discord Botのトークン
     */
    public String getDiscordToken() {
        return discordToken;
    }

    /**
     * <p>OpenAI APIキーを取得する。</p>
     *
     * @return OpenAI APIキー
     */
    public String getOpenAiApiKey() {
        return openAiApiKey;
    }

    /**
     * <p>Hot Pepper APIキーを取得する。</p>
     *
     * @return Hot Pepper APIキー
     */
    public String getHotPepperApiKey() {
        return hotPepperApiKey;
    }
}
