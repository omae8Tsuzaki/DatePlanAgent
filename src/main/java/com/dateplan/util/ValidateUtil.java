package com.dateplan.util;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * <p>入力値の検証を行うユーティリティクラス。</p>
 */
public class ValidateUtil {

    private ValidateUtil() {
        // ユーティリティクラスのため，インスタンス化を防止するために private コンストラクタを定義
    }

    /**
     * <p>日付の形式を定義する定数。</p>
     * <p>形式：YYYY-MM-DD</p>
     */
    public static final DateTimeFormatter YYYY_MM_DD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * <p>日時の形式を定義する定数。</p>
     * <p>形式：YYYY-MM-DD HH:mm</p>
     */
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * <p>日付の形式を検証するメソッド。</p>
     *
     * @param date 検証する日付文字列
     * @param formatter 日付の形式を定義する DateTimeFormatter
     * @return 日付の形式が正しい場合は true、そうでない場合は false
     */
    public static boolean validateDate(String date, DateTimeFormatter formatter) {

        // null チェック
        if (date == null || formatter == null) {
            return false;
        }

        // 形式チェック
        try {
            formatter.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
