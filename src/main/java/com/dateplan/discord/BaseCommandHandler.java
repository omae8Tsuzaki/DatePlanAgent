package com.dateplan.discord;

import com.dateplan.agent.DatePlanAgent;
import com.dateplan.entity.DatePlan;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.function.Consumer;

/**
 * <p>Discord のコマンドハンドラの基底クラス。</p>
 */
public abstract class BaseCommandHandler extends ListenerAdapter {

    // Discord の文字数制限
    private static final int MESSAGE_LIMIT = 2000;

    protected final DatePlanAgent agent;

    protected BaseCommandHandler(DatePlanAgent agent) {
        this.agent = agent;
    }

    /**
     * <p>デートプランを Discord のメッセージ形式に整形する。</p>
     *
     * @param plan デートプランのエンティティ
     * @return Discord のメッセージ形式に整形されたデートプランのテキスト
     */
    protected String formatPlan(DatePlan plan) {
        StringBuilder sb = new StringBuilder();
        sb.append("**").append(plan.request().date()).append(" ").append(plan.request().area()).append(" のデートプラン**\n\n");
        sb.append(plan.planText());
        return sb.toString();
    }

    /**
     * <p>メッセージを送信する。</p>
     * <p>Discord のメッセージ上限を考慮して、長いメッセージは分割して送信する。</p>
     *
     * @param message 送信するメッセージのテキスト
     * @param sender  メッセージの送信処理を行う {@link Consumer}
     */
    protected void sendLongMessage(String message, Consumer<String> sender) {
        if (message.length() <= MESSAGE_LIMIT) {
            sender.accept(message);
        } else {
            int start = 0;
            while (start < message.length()) {
                int end = Math.min(start + MESSAGE_LIMIT, message.length());
                sender.accept(message.substring(start, end));
                start = end;
            }
        }
    }
}
