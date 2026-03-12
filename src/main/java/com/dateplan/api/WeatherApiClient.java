package com.dateplan.api;

import com.dateplan.entity.WeatherInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * <p>気象庁の天気予報APIクライアント</p>
 */
@Component
public class WeatherApiClient {

    private static final String BASE_URL = "https://www.jma.go.jp/bosai/forecast/data/overview_forecast/";
    private final HttpClient httpClient;
    private final Gson gson;

    public WeatherApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * <p>指定された地域コードに対する天気予報を非同期で取得する。</p>
     *
     * @param areaCode 気象庁の地域コード（例: "130000"は東京）
     * @return 天気予報情報を含むCompletableFuture
     */
    public CompletableFuture<WeatherInfo> getWeather(String areaCode) {
        String url = BASE_URL + areaCode + ".json";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw new RuntimeException("Weather API returned status " + response.statusCode()
                                + " for area code " + areaCode);
                    }
                    return parseWeather(response.body());
                });
    }

    /**
     * <p>API からの JSON レスポンスを WeatherInfo オブジェクトに変換する。</p>
     *
     * @param json API からの JSON レスポンスボディ
     * @return WeatherInfo オブジェクト
     */
    private WeatherInfo parseWeather(String json) {
        JsonObject obj = gson.fromJson(json, JsonObject.class);
        return new WeatherInfo(
                getStringOrEmpty(obj, "targetArea"),
                getStringOrEmpty(obj, "reportDatetime"),
                getStringOrEmpty(obj, "headlineText"),
                getStringOrEmpty(obj, "text")
        );
    }

    /**
     * <p>指定されたキーに対する文字列値を JSON オブジェクトから安全に取得する。キーが存在しないか、値が null の場合は空文字を返す。</p>
     *
     * @param obj JSON オブジェクト
     * @param key 取得したい値のキー
     * @return キーに対応する文字列値、存在しない場合は空文字
     */
    private String getStringOrEmpty(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : "";
    }
}
