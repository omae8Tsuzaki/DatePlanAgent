package com.dateplan;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    private final String discordToken;
    private final String openAiApiKey;
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

    public String getDiscordToken() {
        return discordToken;
    }

    public String getOpenAiApiKey() {
        return openAiApiKey;
    }

    public String getHotPepperApiKey() {
        return hotPepperApiKey;
    }
}
