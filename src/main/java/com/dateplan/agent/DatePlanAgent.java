package com.dateplan.agent;

import com.dateplan.api.ai.AiClient;
import com.dateplan.api.ai.DatePlanPromptBuilder;
import com.dateplan.api.HotPepperApiClient;
import com.dateplan.api.WeatherApiClient;
import com.dateplan.entity.DatePlan;
import com.dateplan.entity.DatePlanRequest;
import com.dateplan.entity.Restaurant;
import com.dateplan.entity.WeatherInfo;
import com.dateplan.util.AreaCodeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>デートプラン生成の中心的な役割を担うエージェントクラス。</p>
 */
@Service
public class DatePlanAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatePlanAgent.class);
    // ホットペッパーAPIから取得するレストランの最大数
    private static final int RESTAURANT_COUNT = 5;

    // 使用するAIモデルのID
    // TODO 将来的に application.properties から設定できるようにする
    private static final String MODEL_ID = "gpt-5-nano-2025-08-07";

    private final WeatherApiClient weatherClient;
    private final HotPepperApiClient hotPepperClient;
    private final AiClient aiClient;
    private final DatePlanPromptBuilder promptBuilder;

    public DatePlanAgent(WeatherApiClient weatherClient, HotPepperApiClient hotPepperClient,
                         AiClient aiClient, DatePlanPromptBuilder promptBuilder) {
        this.weatherClient = weatherClient;
        this.hotPepperClient = hotPepperClient;
        this.aiClient = aiClient;
        this.promptBuilder = promptBuilder;
    }

    /**
     * <p>デートプランを生成するメインメソッド。</p>
     *
     * @param request デートプランのリクエスト情報（日付、エリアなど）
     * @return 生成されたデートプランを含む CompletableFuture
     */
    public CompletableFuture<DatePlan> generatePlan(DatePlanRequest request) {
        LOGGER.info("Generating date plan for date={}, area={}", request.date(), request.area());

        String areaCode = AreaCodeResolver.resolve(request.area());
        if (areaCode == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("エリア「" + request.area() + "」に対応する地域コードが見つかりませんでした。"));
        }

        // 天気予報とレストラン情報を並列取得
        CompletableFuture<WeatherInfo> weatherFuture = weatherClient.getWeather(areaCode)
                .exceptionally(e -> {
                    LOGGER.warn("Failed to fetch weather: {}", e.getMessage());
                    return new WeatherInfo(request.area(), "", "", "天気情報を取得できませんでした。");
                });

        CompletableFuture<List<Restaurant>> restaurantFuture = hotPepperClient.searchRestaurants(request.area(), request.genre(), RESTAURANT_COUNT)
                .exceptionally(e -> {
                    LOGGER.warn("Failed to fetch restaurants: {}", e.getMessage());
                    return Collections.emptyList();
                });

        // 両方の結果を待ってからOpenAIでプラン生成
        return weatherFuture.thenCombine(restaurantFuture, (weather, restaurants) -> {
            LOGGER.info("Weather and restaurant data collected. Generating plan with AI...");

            // システムプロンプト生成
            String systemPrompt = promptBuilder.buildSystemPrompt();
            // ユーザープロンプト生成
            String userPrompt = promptBuilder.buildUserPrompt(request, weather, restaurants);

            return aiClient.chat(systemPrompt, userPrompt, MODEL_ID)
                    .thenApply(planText -> new DatePlan(request, weather, restaurants, planText));
        }).thenCompose(future -> future);
    }
}
