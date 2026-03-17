package com.dateplan.util;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>{@link ValidateUtil}のテストクラス。</p>
 *
 * <h4>対象：{@link ValidateUtil#validateDate(String, DateTimeFormatter)}メソッド。</h4>
 * <ul>
 *     <li>{@link #validateDateSuccess01()} 正常系: 正しい日付文字列を渡した場合。</li>
 *     <li>{@link #validateDateSuccess02()} 正常系: フォーマットが正しくない日付文字列を渡した場合。</li>
 *     <li>{@link #validateDateSuccess03()} 正常系: 日付のフォーマットは正しいが、存在しない日付を渡した場合。</li>
 *     <li>{@link #validateDateSuccess04()} 正常系: 検証対象の文字列が null の場合。</li>
 *     <li>{@link #validateDateSuccess05()} 正常系: フォーマットが null の場合。</li>
 * </ul>
 */
public class ValidateUtilTest {

    /**
     * <p>正常系: 正しい日付文字列を渡した場合。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateDateSuccess01() throws Exception {

        //
        // 事前準備
        //

        String dateStr = "2026-03-18";

        //
        // 実行
        //

        boolean result = ValidateUtil.validateDate(dateStr, ValidateUtil.YYYY_MM_DD_FORMATTER);

        //
        // 検証
        //

        assertTrue(result);
    }

    /**
     * <p>正常系: フォーマットが正しくない日付文字列を渡した場合。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateDateSuccess02() throws Exception {

        //
        // 事前準備
        //

        String dateStr = "2026/03/18";

        //
        // 実行
        //

        boolean result = ValidateUtil.validateDate(dateStr, ValidateUtil.YYYY_MM_DD_FORMATTER);

        //
        // 検証
        //

        assertFalse(result);
    }

    /**
     * <p>正常系: 日付のフォーマットは正しいが、存在しない日付を渡した場合。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateDateSuccess03() throws Exception {

        //
        // 事前準備
        //

        // 日付のフォーマットは正しいが、存在しない日付
        String dateStr = "2026-13-01";

        //
        // 実行
        //

        boolean result = ValidateUtil.validateDate(dateStr, ValidateUtil.YYYY_MM_DD_FORMATTER);

        //
        // 検証
        //

        assertFalse(result);
    }

    /**
     * <p>正常系: 検証対象の文字列が null の場合。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateDateSuccess04() throws Exception {

        //
        // 事前準備
        //

        //
        // 実行
        //

        boolean result = ValidateUtil.validateDate(null, ValidateUtil.YYYY_MM_DD_FORMATTER);

        //
        // 検証
        //

        assertFalse(result);
    }

    /**
     * <p>正常系: フォーマットが null の場合。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateDateSuccess05() throws Exception {

        //
        // 事前準備
        //

        String dateStr = "2026-03-18";

        //
        // 実行
        //

        boolean result = ValidateUtil.validateDate(dateStr, null);

        //
        // 検証
        //

        assertFalse(result);
    }
}