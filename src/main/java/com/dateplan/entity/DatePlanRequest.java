package com.dateplan.entity;

/**
 * <p>デートプランのリクエストを表すクラス。</p>
 *
 * @param date デートの予定日（例: "2024-07-01"）
 * @param area デートの予定エリア（例: "東京"）
 * @param genre 好みのジャンル（例: "和食"）。未指定の場合は {@code null}
 * @param timeOfDay 時間帯（例: "昼間"）。未指定の場合は {@code null}
 * @param transportation 移動手段（例: "徒歩中心"）。未指定の場合は {@code null}
 */
public record DatePlanRequest(String date, String area, String genre, String timeOfDay, String transportation) {
}
