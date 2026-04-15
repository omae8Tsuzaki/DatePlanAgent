package com.dateplan.api;

import com.dateplan.entity.WeatherInfo;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link WeatherApiClient}のテストクラス。</p>
 *
 * <h4>対象：{@link WeatherApiClient#getWeather(String)}メソッド。</h4>
 * <ul>
 *    <li>{@link #getWeatherSuccess01()} 正常系: 実際の地域コードを使用して天気予報を取得できることを確認する。</li>
 *    <li>{@link #getWeatherError01()} 異常系: 存在しない地域コードを使用した場合。</li>
 *    <li>{@link #getWeatherError02()} 異常系: nullを地域コードとして渡した場合。</li>
 * </ul>
 */
public class WeatherApiClientTest {

    /**
     * <p>正常系: 実際の地域コードを使用して天気予報を取得できることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void getWeatherSuccess01() throws Exception {

        //
        // 事前準備
        //
        WeatherApiClient weatherClient = new WeatherApiClient();
        // 新宿の地域コード
        String areaCode = "130000";


        //
        // 実行
        //

        CompletableFuture<WeatherInfo> weatherFuture = weatherClient.getWeather(areaCode);

        //
        // 検証
        //

        WeatherInfo weatherInfo = weatherFuture.get();

        assertEquals("東京都", weatherInfo.targetArea());
        //assertEquals("",weatherInfo.reportDatetime());
        assertEquals("", weatherInfo.headlineText());
        //assertEquals("", weatherInfo.text());
    }

    /**
     * <p>異常系: 存在しない地域コードを使用した場合。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void getWeatherError01() throws Exception {

        //
        // 事前準備
        //
        WeatherApiClient weatherClient = new WeatherApiClient();
        // 存在しない地域コード
        String areaCode = "XXXX";

        //
        // 実行・検証
        //
        CompletableFuture<WeatherInfo> weatherFuture = weatherClient.getWeather(areaCode);
        try {
            weatherFuture.get();
            fail();
        } catch (ExecutionException e) {
            assertEquals(
                    "Weather API returned status 404 for area code " + areaCode,
                    e.getCause().getMessage()
            );
        }
    }

    /**
     * <p>異常系: null を地域コードとして渡した場合。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void getWeatherError02() throws Exception {

        //
        // 事前準備
        //
        WeatherApiClient weatherClient = new WeatherApiClient();

        //
        // 実行・検証
        //
        CompletableFuture<WeatherInfo> weatherFuture = weatherClient.getWeather(null);
        try {
            weatherFuture.get();
            fail();
        } catch (ExecutionException e) {
            assertEquals(
                    "Weather API returned status 404 for area code null",
                    e.getCause().getMessage()
            );
        }
    }
}