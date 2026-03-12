package com.dateplan.api;

import com.dateplan.entity.Restaurant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>ホットペッパーグルメAPIクライアント</p>
 */
@Component
public class HotPepperApiClient {
    private static final String BASE_URL = "http://webservice.recruit.co.jp/hotpepper/gourmet/v1/";
    private final HttpClient httpClient;
    private final Gson gson;
    private final String apiKey;

    public HotPepperApiClient(@Value("${hotpepper.api.key}") String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
        this.apiKey = apiKey;
    }

    /**
     * <p>指定されたキーワードでレストランを非同期で検索する。</p>
     *
     * @param keyword 検索キーワード（例: "渋谷 カフェ"）
     * @param count 取得するレストランの最大数
     * @return レストランのリストを含むCompletableFuture
     */
    public CompletableFuture<List<Restaurant>> searchRestaurants(String keyword, int count) {
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String url = BASE_URL + "?key=" + apiKey
                + "&keyword=" + encodedKeyword
                + "&format=json"
                + "&count=" + count;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw new RuntimeException("HotPepper API returned status " + response.statusCode());
                    }
                    return parseRestaurants(response.body());
                });
    }

    /**
     * <p>API からの JSON レスポンスをレストランのリストに変換する。</p>
     *
     * @param json API からの JSON レスポンスボディ
     * @return レストランのリスト
     */
    private List<Restaurant> parseRestaurants(String json) {
        List<Restaurant> restaurants = new ArrayList<>();
        JsonObject root = gson.fromJson(json, JsonObject.class);
        JsonObject results = root.getAsJsonObject("results");

        if (results == null || !results.has("shop")) {
            return restaurants;
        }

        JsonArray shops = results.getAsJsonArray("shop");
        for (JsonElement element : shops) {
            JsonObject shop = element.getAsJsonObject();
            restaurants.add(new Restaurant(
                    getStringOrEmpty(shop, "name"),
                    getStringOrEmpty(shop, "address"),
                    getGenre(shop),
                    getBudget(shop),
                    getStringOrEmpty(shop, "access"),
                    getNestedString(shop, "urls", "pc"),
                    getPhoto(shop)
            ));
        }
        return restaurants;
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

    private String getGenre(JsonObject shop) {
        if (shop.has("genre") && shop.get("genre").isJsonObject()) {
            return getStringOrEmpty(shop.getAsJsonObject("genre"), "name");
        }
        return "";
    }

    private String getBudget(JsonObject shop) {
        if (shop.has("budget") && shop.get("budget").isJsonObject()) {
            return getStringOrEmpty(shop.getAsJsonObject("budget"), "name");
        }
        return "";
    }

    private String getNestedString(JsonObject obj, String outerKey, String innerKey) {
        if (obj.has(outerKey) && obj.get(outerKey).isJsonObject()) {
            return getStringOrEmpty(obj.getAsJsonObject(outerKey), innerKey);
        }
        return "";
    }

    private String getPhoto(JsonObject shop) {
        if (shop.has("photo") && shop.get("photo").isJsonObject()) {
            JsonObject photo = shop.getAsJsonObject("photo");
            if (photo.has("pc") && photo.get("pc").isJsonObject()) {
                return getStringOrEmpty(photo.getAsJsonObject("pc"), "l");
            }
        }
        return "";
    }
}
