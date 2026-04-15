package com.dateplan.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link AreaCodeResolver}のテストクラス。</p>
 *
 * <h4>対象：{@link AreaCodeResolver#resolve(String)}メソッド。</h4>
 * <ul>
 *     <li>{@link #resolveSuccess01()} 正常系: 正しい地域名を渡すと、対応する地域コードが返されることを確認する。</li>
 *     <li>{@link #resolveSuccess02()} 正常系: 空文字を渡すと、nullが返されることを確認する。</li>
 *     <li>{@link #resolveSuccess03()} 正常系: nullを渡すと、nullが返されることを確認する。</li>
 * </ul>
 */
public class AreaCodeResolverTest {

    /**
     * <p>正常系：正しい地域名を渡すと、対応する地域コードが返される。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void resolveSuccess01() throws Exception {

        //
        // 事前準備
        //

        String areaName = "新宿";

        //
        // 実行
        //

        String result = AreaCodeResolver.resolve(areaName);

        //
        // 検証
        //

        assertEquals("130000", result);
    }

    /**
     * <p>正常系：空文字を渡すと、null が返される。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void resolveSuccess02() throws Exception {

        //
        // 事前準備
        //

        //
        // 実行
        //

        String result = AreaCodeResolver.resolve("");

        //
        // 検証
        //

        assertNull(result);
    }

    /**
     * <p>正常系：null を渡すと、null が返される。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void resolveSuccess03() throws Exception {

        //
        // 事前準備
        //

        //
        // 実行
        //

        String result = AreaCodeResolver.resolve(null);

        //
        // 検証
        //

        assertNull(result);
    }
}