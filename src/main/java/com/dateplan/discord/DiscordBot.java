package com.dateplan.discord;

import com.dateplan.AppConfig;
import com.dateplan.agent.DatePlanAgent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DiscordBot implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DiscordBot.class);

    private final AppConfig config;
    private final DatePlanAgent agent;

    public DiscordBot(AppConfig config, DatePlanAgent agent) {
        this.config = config;
        this.agent = agent;
    }

    @Override
    public void run(String... args) throws Exception {
        JDA jda = buildJda(config, agent);
        registerCommands(jda);
        logger.info("DatePlanAgent Bot is ready!");
    }

    /**
     * <p>JDAを構築する。</p>
     *
     * @param config アプリケーション設定
     * @param agent DatePlanAgentのインスタンス
     * @return 構築されたJDAのインスタンス
     * @throws InterruptedException JDAの起動に失敗した場合
     */
    JDA buildJda(AppConfig config, DatePlanAgent agent) throws InterruptedException {
        JDA jda = JDABuilder.createLight(config.getDiscordToken(),
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(
                        new SlashCommandHandler(agent),
                        new MessageCommandHandler(agent)
                )
                .build();
        jda.awaitReady();
        return jda;
    }

    void registerCommands(JDA jda) {
        jda.updateCommands().addCommands(
                Commands.slash("dateplan", "デートプランを生成します")
                        .addOption(OptionType.STRING, "date", "日付（例: 2026-03-15）", true)
                        .addOption(OptionType.STRING, "area", "エリア（例: 渋谷）", true)
        ).queue();
    }
}
