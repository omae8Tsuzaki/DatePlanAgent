package com.dateplan.discord;

import com.dateplan.agent.DatePlanAgent;
import com.dateplan.entity.DatePlan;
import com.dateplan.entity.DatePlanRequest;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Discordのスラッシュコマンドを処理するクラス。</p>
 */
public class SlashCommandHandler extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SlashCommandHandler.class);
    private final DatePlanAgent agent;

    public SlashCommandHandler(DatePlanAgent agent) {
        this.agent = agent;
    }

    /**
     * <p>スラッシュコマンド「/dateplan」を処理する。</p>
     * <p>ユーザーが「/dateplan date:<日付> area:<エリア>」という形式でコマンドを送信した際に、デートプランを生成して返信する。</p>
     *
     * @param event スラッシュコマンドのイベント
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("dateplan")) {
            return;
        }

        // Discordのスラッシュコマンドからオプションを取得
        String date = event.getOption("date") != null ? event.getOption("date").getAsString() : "";
        String area = event.getOption("area") != null ? event.getOption("area").getAsString() : "";

        if (date.isBlank() || area.isBlank()) {
            event.reply("日付とエリアを両方指定してください。\n例: `/dateplan date:2026-03-15 area:渋谷`")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        logger.info("Slash command received: date={}, area={}", date, area);

        // 即座に「考え中...」を返答
        event.deferReply().queue();

        DatePlanRequest request = new DatePlanRequest(date, area);
        agent.generatePlan(request)
                .thenAccept(plan -> {
                    String message = formatPlan(plan);
                    sendLongMessage(event, message);
                })
                .exceptionally(e -> {
                    logger.error("Failed to generate plan", e);
                    event.getHook().sendMessage("エラーが発生しました: " + e.getMessage()).queue();
                    return null;
                });
    }

    /**
     * <p>デートプランを Discord のメッセージ形式に整形する。</p>
     *
     * @param plan デートプランのエンティティ
     * @return Discordのメッセージ形式に整形されたデートプランのテキスト
     */
    private String formatPlan(DatePlan plan) {
        StringBuilder sb = new StringBuilder();
        sb.append("**").append(plan.request().date()).append(" ").append(plan.request().area()).append(" のデートプラン**\n\n");
        sb.append(plan.planText());
        return sb.toString();
    }

    /**
     * <p>メッセージを送信する。</p>
     * <p>Discordのメッセージ上限（2000文字）を考慮して、長いメッセージは分割して送信する。</p>
     *
     * @param event SlashCommandInteractionEventのフックを使用してメッセージを送信
     * @param message 送信するメッセージのテキスト
     */
    private void sendLongMessage(SlashCommandInteractionEvent event, String message) {
        // Discordのメッセージ上限は2000文字
        if (message.length() <= 2000) {
            event.getHook().sendMessage(message).queue();
        } else {
            // 長いメッセージは分割して送信
            int start = 0;
            boolean first = true;
            while (start < message.length()) {
                int end = Math.min(start + 2000, message.length());
                String chunk = message.substring(start, end);
                if (first) {
                    event.getHook().sendMessage(chunk).queue();
                    first = false;
                } else {
                    event.getHook().sendMessage(chunk).queue();
                }
                start = end;
            }
        }
    }
}
