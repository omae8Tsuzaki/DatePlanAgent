package com.dateplan.discord;

import com.dateplan.agent.DatePlanAgent;
import com.dateplan.entity.DatePlan;
import com.dateplan.entity.DatePlanRequest;
import com.dateplan.util.ValidateUtil;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Discordのメッセージコマンドを処理するクラス。</p>
 * <p>ユーザーが「!dateplan <日付> <エリア>」という形式でメッセージを送信した際に、デートプランを生成して返信します。</p>
 */
public class MessageCommandHandler extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageCommandHandler.class);
    private static final String PREFIX = "!dateplan";
    private final DatePlanAgent agent;

    public MessageCommandHandler(DatePlanAgent agent) {
        this.agent = agent;
    }

    /**
     * <p>メッセージコマンド「!dateplan」を処理する。</p>
     * <p>ユーザーが「!dateplan <日付> <エリア>」という形式でコマンドを送信した際に、デートプランを生成して返信します。</p>
     *
     * @param event メッセージ受信のイベント
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String content = event.getMessage().getContentRaw().trim();
        if (!content.startsWith(PREFIX)) {
            return;
        }

        String args = content.substring(PREFIX.length()).trim();
        String[] parts = args.split("\\s+", 2);

        if (parts.length < 2) {
            event.getChannel().sendMessage(
                    "使い方: `!dateplan <日付> <エリア>`\n例: `!dateplan 2026-03-15 渋谷`"
            ).queue();
            return;
        }

        String date = parts[0];
        String area = parts[1];

        // 入力値チェック
        // 日付
        if (!ValidateUtil.validateDate(date, ValidateUtil.YYYY_MM_DD_FORMATTER)) {
            event.getChannel().sendMessage("日付の形式が正しくありません。例: `2026-03-15`").queue();
            return;
        }

        LOGGER.info("Message command received: date={}, area={}", date, area);

        MessageChannel channel = event.getChannel();

        // 「考え中...」メッセージを送信
        channel.sendMessage("考え中... しばらくお待ちください。").queue();

        DatePlanRequest request = new DatePlanRequest(date, area, null, null, null);
        agent.generatePlan(request)
                .thenAccept(plan -> {
                    String message = formatPlan(plan);
                    sendLongMessage(channel, message);
                })
                .exceptionally(e -> {
                    LOGGER.error("Failed to generate plan", e);
                    channel.sendMessage("エラーが発生しました: " + e.getMessage()).queue();
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
     *
     * @param channel メッセージを送信するチャンネル
     * @param message 送信するメッセージのテキスト
     */
    private void sendLongMessage(MessageChannel channel, String message) {
        if (message.length() <= 2000) {
            channel.sendMessage(message).queue();
        } else {
            int start = 0;
            while (start < message.length()) {
                int end = Math.min(start + 2000, message.length());
                String chunk = message.substring(start, end);
                channel.sendMessage(chunk).queue();
                start = end;
            }
        }
    }
}
