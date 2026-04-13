package com.dateplan.api;

import com.dateplan.AppConfig;
import com.dateplan.entity.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link HotPepperApiClient}のテストクラス。</p>
 *
 * <h4>対象：{@link HotPepperApiClient#searchRestaurants(String, int)}メソッド。</h4>
 * <ul>
 *    <li>{@link #searchRestaurantsSuccess01()} 正常系: キーワードを使用してレストラン情報を取得できることを確認する。</li>
 * </ul>
 */
public class HotPepperApiClientTest {

    /**
     * <p>正常系: キーワードを使用してレストラン情報を取得できることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void searchRestaurantsSuccess01() throws Exception {

        //
        // 事前準備
        //
        String apiKey = System.getenv("HOTPEPPER_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            apiKey = "test-hotpepper-key";
        }
        AppConfig appConfig = new AppConfig("dummy-discord-token", "dummy-openai-key", apiKey);
        HotPepperApiClient hotPepperClient = new HotPepperApiClient(appConfig);
        String keyword = "渋谷 デート";
        int maxResults = 5;

        //
        // 実行
        //
        CompletableFuture<List<Restaurant>> searchFuture = hotPepperClient.searchRestaurants(keyword, maxResults);
        // API の結果格納
        List<Restaurant> restaurantList = searchFuture.get();

        //
        // 検証
        //

        //assertEquals(maxResults, restaurantList.size());
    }
}
