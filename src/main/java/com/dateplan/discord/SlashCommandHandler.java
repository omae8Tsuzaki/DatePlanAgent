package com.dateplan.discord;

import com.dateplan.agent.DatePlanAgent;
import com.dateplan.entity.DatePlanRequest;
import com.dateplan.util.ValidateUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Discord のスラッシュコマンドを処理するクラス。</p>
 */
public class SlashCommandHandler extends BaseCommandHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlashCommandHandler.class);

    public SlashCommandHandler(DatePlanAgent agent) {
        super(agent);
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
        String genre = event.getOption("genre") != null ? event.getOption("genre").getAsString() : null;
        String timeOfDay = event.getOption("time-of-day") != null ? event.getOption("time-of-day").getAsString() : null;
        String transportation = event.getOption("transportation") != null ? event.getOption("transportation").getAsString() : null;

        // 入力値チェック
        // 必須項目の空白チェック
        if (date.isBlank() || area.isBlank()) {
            event.reply("日付とエリアを両方指定してください。\n例: `/dateplan date:2026-03-15 area:渋谷`")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        // 日付形式チェック
        if (!ValidateUtil.validateDate(date, ValidateUtil.YYYY_MM_DD_FORMATTER)) {
            event.reply("日付の形式が正しくありません。例: `2026-03-15`")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        LOGGER.info("Slash command received: date={}, area={}, genre={}, time-of-day={}, transportation={}", date, area, genre, timeOfDay, transportation);

        // 即座に「考え中...」を返答
        event.deferReply().queue();

        DatePlanRequest request = new DatePlanRequest(date, area, genre, timeOfDay, transportation);
        agent.generatePlan(request)
                .thenAccept(plan -> {
                    String message = formatPlan(plan);
                    sendLongMessage(message, chunk -> event.getHook().sendMessage(chunk).queue());
                })
                .exceptionally(e -> {
                    LOGGER.error("Failed to generate plan", e);
                    event.getHook().sendMessage("エラーが発生しました: " + e.getMessage()).queue();
                    return null;
                });
    }

}
