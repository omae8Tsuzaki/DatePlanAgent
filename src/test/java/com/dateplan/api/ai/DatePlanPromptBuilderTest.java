package com.dateplan.api.ai;

import com.dateplan.entity.DatePlanRequest;
import com.dateplan.entity.Restaurant;
import com.dateplan.entity.WeatherInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link DatePlanPromptBuilder}のテストクラス。</p>
 *
 * <h4>対象：{@link DatePlanPromptBuilder#buildSystemPrompt()}メソッド</h4>
 * <ul>
 *     <li>{@link #buildSystemPromptSuccess01()} 正常系: システムプロンプトが正しく構築されることを確認する。</li>
 * </ul>
 *
 * <h4>対象：{@link DatePlanPromptBuilder#buildUserPrompt(DatePlanRequest, WeatherInfo, List)}メソッド</h4>
 * <ul>
 *     <li>{@link #buildUserPromptSuccess01()} 正常系: ユーザープロンプトが正しく構築されることを確認する。</li>
 * </ul>
 */
public class DatePlanPromptBuilderTest {

    // 環境に依存せず、改行コードを統一するための定数
    private static final String LS = System.lineSeparator();

    /**
     * <p>正常系: システムプロンプトが正しく構築されることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void buildSystemPromptSuccess01() throws Exception {

        //
        // 事前準備
        //

        DatePlanPromptBuilder promptBuilder = new DatePlanPromptBuilder();

        //
        // 実行
        //

        String result = promptBuilder.buildSystemPrompt();

        //
        // 検証
        //

        assertEquals(
                """
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
                """,
                result);
    }

    /**
     * <p>正常系: ユーザープロンプトが正しく構築されることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void buildUserPromptSuccess01() throws Exception {

        //
        // 事前準備
        //

        DatePlanPromptBuilder promptBuilder = new DatePlanPromptBuilder();

        // ダミーデータの作成
        DatePlanRequest request = new DatePlanRequest("2024-07-01", "渋谷");
        WeatherInfo weather = new WeatherInfo("渋谷", "晴れ", "2024-07-01T08:00:00Z", "晴れのち曇り");
        List<Restaurant> restaurants = List.of(
                new Restaurant("レストランA", "東京都渋谷区1-1-1", "イタリアン", "5000", "渋谷駅から徒歩1分", "https://example.com/a", "イタリアン"),
                new Restaurant("レストランB", "東京都渋谷区2-2-2", "フレンチ", "1000" , "渋谷駅からタクシーで１分", "https://example.com/b", "フレンチ")
        );

        //
        // 実行
        //

        String result = promptBuilder.buildUserPrompt(request, weather, restaurants);

        //
        // 検証
        //

        assertEquals(
                "## デートプラン作成リクエスト" + LS +
                        LS +
                        "**日付**: 2024-07-01" + LS +
                        "**エリア**: 渋谷" + LS +
                        LS +
                        "## 天気予報" + LS +
                        "**対象地域**: 渋谷" + LS +
                        "**予報**: 晴れのち曇り" + LS +
                        LS +
                        "## 周辺レストラン情報" + LS +
                        "1. **レストランA**" + LS +
                        "   - ジャンル: イタリアン" + LS +
                        "   - 予算: 5000" + LS +
                        "   - アクセス: 渋谷駅から徒歩1分" + LS +
                        "2. **レストランB**" + LS +
                        "   - ジャンル: フレンチ" + LS +
                        "   - 予算: 1000" + LS +
                        "   - アクセス: 渋谷駅からタクシーで１分" + LS +
                        LS +
                        "上記の情報をもとに、素敵なデートプランを作成してください。",
                result);
    }
}
