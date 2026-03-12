package com.dateplan.entity;

/**
 * <p>天気予報の情報を表すクラス。</p>
 *
 * @param targetArea 予報対象地域
 * @param reportDatetime 予報作成日時
 * @param headlineText 予報の見出しテキスト
 * @param text 予報の詳細テキスト
 */
public record WeatherInfo(String targetArea, String reportDatetime, String headlineText, String text) {
}
