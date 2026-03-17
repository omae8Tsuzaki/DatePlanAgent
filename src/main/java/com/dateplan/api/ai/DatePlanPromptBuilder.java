package com.dateplan.api.ai;

import com.dateplan.entity.DatePlanRequest;
import com.dateplan.entity.Restaurant;
import com.dateplan.entity.WeatherInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>デートプラン生成用のプロンプトを構築するクラス。</p>
 *
 * <p>プロバイダーに依存せず、システムプロンプトとユーザープロンプトを生成する責任を持ちます。</p>
 */
@Component
public class DatePlanPromptBuilder {

    // 環境に依存せず、改行コードを統一するための定数
    private static final String LS = System.lineSeparator();

    /**
     * <p>システムプロンプトを構築する。</p>
     *
     * @return システムプロンプト
     */
    public String buildSystemPrompt() {
        return """
                あなたはデートプランナーのAIアシスタントです。
                ユーザーから提供される日付、エリア、天気予報、レストラン情報をもとに、
                魅力的で実用的なデートプランを日本語で作成してください。

                以下のルールに従ってください：
                - 時間軸に沿ったプランを作成（午前〜夜まで）
                - 天気に応じた活動を提案（雨なら屋内中心など）
                - 提供されたレストラン情報から適切なお店を組み込む
                - 移動手段や所要時間も考慮する
                - 絵文字を適度に使い、読みやすく楽しい文章にする
                - 2000文字以内に収める
                """;
    }

    /**
     * <p>ユーザープロンプトを構築する。</p>
     *
     * @param request デートプランリクエスト
     * @param weather 天気情報
     * @param restaurants レストラン情報のリスト
     * @return ユーザープロンプト
     */
    public String buildUserPrompt(DatePlanRequest request, WeatherInfo weather, List<Restaurant> restaurants) {
        StringBuilder sb = new StringBuilder();
        sb.append("## デートプラン作成リクエスト").append(LS);
        sb.append(LS);
        sb.append("**日付**: ").append(request.date()).append(LS);
        sb.append("**エリア**: ").append(request.area()).append(LS);
        sb.append(LS);
        sb.append("## 天気予報").append(LS);
        sb.append("**対象地域**: ").append(weather.targetArea()).append(LS);
        sb.append("**予報**: ").append(weather.text()).append(LS);
        sb.append(LS);
        sb.append("## 周辺レストラン情報").append(LS);
        if (restaurants.isEmpty()) {
            sb.append("レストラン情報は取得できませんでした。一般的なおすすめを提案してください。").append(LS);
        } else {
            for (int i = 0; i < restaurants.size(); i++) {
                Restaurant r = restaurants.get(i);
                sb.append(i + 1).append(". **").append(r.name()).append("**").append(LS);
                sb.append("   - ジャンル: ").append(r.genre()).append(LS);
                sb.append("   - 予算: ").append(r.budget()).append(LS);
                sb.append("   - アクセス: ").append(r.access()).append(LS);
            }
        }

        sb.append(LS).append("上記の情報をもとに、素敵なデートプランを作成してください。");
        return sb.toString();
    }
}
