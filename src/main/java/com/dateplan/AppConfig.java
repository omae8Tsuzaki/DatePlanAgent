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
     * <p>Discord Bot のトークン。</p>
     */
    private final String discordToken;

    /**
     * <p>OpenAI API キー。</p>
     */
    private final String openAiApiKey;

    /**
     * <p>Hot Pepper API キー。</p>
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

    /**
     * <p>アプリケーション起動時に設定値の検証を行う。</p>
     */
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
     * <p>Discord Bot のトークンを取得する。</p>
     *
     * @return Discord Bot のトークン
     */
    public String getDiscordToken() {
        return discordToken;
    }

    /**
     * <p>OpenAI API キーを取得する。</p>
     *
     * @return OpenAI API キー
     */
    public String getOpenAiApiKey() {
        return openAiApiKey;
    }

    /**
     * <p>Hot Pepper API キーを取得する。</p>
     *
     * @return Hot Pepper API キー
     */
    public String getHotPepperApiKey() {
        return hotPepperApiKey;
    }
}
