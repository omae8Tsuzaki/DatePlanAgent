package com.dateplan;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>テスト用の設定値を格納するクラス。</p>
 * <p>使用例:</p>
 * <pre>
 * AppConfig testConfig = new TestAppConfig().create();
 * </pre>
 */
public class TestAppConfig {

    private final Properties props;

    public TestAppConfig() {
        props = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/application-test.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>テスト用の AppConfig インスタンスを作成する。</p>
     *
     * @return AppConfig インスタンス
     */
    public AppConfig create() {
        return new AppConfig(
                getDiscordToken(),
                getOpenAiApiKey(),
                getHotPepperApiKey(),
                getModelId()
        );
    }

    public String getDiscordToken() {
        return props.getProperty("discord.token");
    }

    public String getOpenAiApiKey() {
        return props.getProperty("openai.api.key");
    }

    public String getHotPepperApiKey() {
        return props.getProperty("hotpepper.api.key");
    }

    public String getModelId() {
        return props.getProperty("openai.model.id");
    }
}
