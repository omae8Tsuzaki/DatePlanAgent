package com.dateplan.entity;

/**
 * <p>デートプランのリクエストを表すクラス。</p>
 *
 * @param date デートの予定日（例: "2024-07-01"）
 * @param area デートの予定エリア（例: "東京"）
 */
public record DatePlanRequest(String date, String area) {
}
