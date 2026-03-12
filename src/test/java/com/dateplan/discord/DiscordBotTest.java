package com.dateplan.discord;

import com.dateplan.AppConfig;
import com.dateplan.agent.DatePlanAgent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <p>{@link DiscordBot}のテストクラス。</p>
 */
public class DiscordBotTest {

    private static final String TEST_TOKEN = "test-token";

    @Test
    public void buildJda_正常にJDAインスタンスを返す() throws InterruptedException {

        //
        // 事前準備
        //

        AppConfig config = mock(AppConfig.class);
        when(config.getDiscordToken()).thenReturn(TEST_TOKEN);

        DatePlanAgent agent = mock(DatePlanAgent.class);
        JDA jda = mock(JDA.class);
        JDABuilder builder = mock(JDABuilder.class);

        when(builder.addEventListeners(any(), any())).thenReturn(builder);
        when(builder.build()).thenReturn(jda);
        when(jda.awaitReady()).thenReturn(jda);

        try (MockedStatic<JDABuilder> mockedStatic = mockStatic(JDABuilder.class)) {
            mockedStatic.when(() -> JDABuilder.createLight(eq(TEST_TOKEN), any(), any()))
                    .thenReturn(builder);

            //
            // 実行
            //
            DiscordBot bot = new DiscordBot(config, agent);
            JDA result = bot.buildJda(config, agent);

            //
            // 検証
            //
            assertNotNull(result);
            assertSame(jda, result);
            verify(builder).addEventListeners(any(SlashCommandHandler.class), any(MessageCommandHandler.class));
            verify(builder).build();
            verify(jda).awaitReady();
        }
    }

    @Test
    public void buildJda_configのトークンがJDABuilderに渡される() throws InterruptedException {

        //
        // 事前準備
        //

        AppConfig config = mock(AppConfig.class);
        when(config.getDiscordToken()).thenReturn(TEST_TOKEN);

        DatePlanAgent agent = mock(DatePlanAgent.class);
        JDA jda = mock(JDA.class);
        JDABuilder builder = mock(JDABuilder.class);

        when(builder.addEventListeners(any(), any())).thenReturn(builder);
        when(builder.build()).thenReturn(jda);
        when(jda.awaitReady()).thenReturn(jda);

        try (MockedStatic<JDABuilder> mockedStatic = mockStatic(JDABuilder.class)) {
            mockedStatic.when(() -> JDABuilder.createLight(anyString(), any(), any()))
                    .thenReturn(builder);

            //
            // 実行
            //
            DiscordBot bot = new DiscordBot(config, agent);
            bot.buildJda(config, agent);

            //
            // 検証
            //
            mockedStatic.verify(() -> JDABuilder.createLight(eq(TEST_TOKEN), any(), any()));
        }
    }

    @Test
    public void buildJda_awaitReadyのInterruptedExceptionが伝播する() throws InterruptedException {

        //
        // 事前準備
        //

        AppConfig config = mock(AppConfig.class);
        when(config.getDiscordToken()).thenReturn(TEST_TOKEN);

        DatePlanAgent agent = mock(DatePlanAgent.class);
        JDA jda = mock(JDA.class);
        JDABuilder builder = mock(JDABuilder.class);

        when(builder.addEventListeners(any(), any())).thenReturn(builder);
        when(builder.build()).thenReturn(jda);
        when(jda.awaitReady()).thenThrow(new InterruptedException("interrupted"));

        try (MockedStatic<JDABuilder> mockedStatic = mockStatic(JDABuilder.class)) {
            mockedStatic.when(() -> JDABuilder.createLight(anyString(), any(), any()))
                    .thenReturn(builder);

            //
            // 実行・検証
            //
            DiscordBot bot = new DiscordBot(config, agent);
            assertThrows(InterruptedException.class, () -> bot.buildJda(config, agent));
        }
    }
}
