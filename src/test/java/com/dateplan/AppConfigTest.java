package com.dateplan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link AppConfig}のテストクラス。</p>
 *
 * <h4>対象：getter メソッド。</h4>
 * <ul>
 *     <li>{@link #getterSuccess01()} 正常系: getter が正しく値を返すことを確認する。</li>
 * </ul>
 *
 * <h4>対象：{@link AppConfig#validate()} メソッド。</h4>
 * <ul>
 *     <li>{@link #validateError01()} 異常系: discord.token が空文字の場合、例外がスローされることを確認する。</li>
 *     <li>{@link #validateError02()} 異常系: discord.token が null の場合、例外がスローされることを確認する。</li>
 *     <li>{@link #validateError03()} 異常系: openai.api.key が空文字の場合、例外がスローされることを確認する。</li>
 *     <li>{@link #validateError04()} 異常系: openai.api.key が null の場合、例外がスローされることを確認する。</li>
 *     <li>{@link #validateError05()} 異常系: hotpepper.api.key が空文字の場合、例外がスローされることを確認する。</li>
 *     <li>{@link #validateError06()} 異常系: hotpepper.api.key が null の場合、例外がスローされることを確認する。</li>
 * </ul>
 */
public class AppConfigTest {

    /**
     * <p>正常系：getter が正しく値を返すことを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void getterSuccess01() throws Exception {

        //
        // 事前準備
        //

        AppConfig config = new AppConfig(
                "test-discord-token",
                "test-openai-key",
                "test-hotpepper-key"
        );

        //
        // 実行・検証
        //

        assertEquals("test-discord-token", config.getDiscordToken());
        assertEquals("test-openai-key", config.getOpenAiApiKey());
        assertEquals("test-hotpepper-key", config.getHotPepperApiKey());

    }

    /**
     * <p>異常系：discord.token が空文字の場合、例外がスローされることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateError01() throws Exception {

        //
        // 事前準備
        //

        AppConfig config = new AppConfig(
                "",
                "test-openai-key",
                "test-hotpepper-key"
        );

        try {

            //
            // 実行
            //

            config.validate();
            fail();
        } catch (IllegalStateException e) {

            //
            // 検証
            //

            assertEquals("discord.token is not set", e.getMessage());
        }
    }

    /**
     * <p>異常系：discord.token が null の場合、例外がスローされることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateError02() throws Exception {

        //
        // 事前準備
        //

        AppConfig config = new AppConfig(
                null,
                "test-openai-key",
                "test-hotpepper-key"
        );

        try {

            //
            // 実行
            //

            config.validate();
            fail();
        } catch (IllegalStateException e) {

            //
            // 検証
            //

            assertEquals("discord.token is not set", e.getMessage());
        }
    }

    /**
     * <p>異常系：openai.api.key が空文字の場合、例外がスローされることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateError03() throws Exception {

        //
        // 事前準備
        //

        AppConfig config = new AppConfig(
                "test-discord-token",
                "",
                "test-hotpepper-key"
        );

        try {

            //
            // 実行
            //

            config.validate();
            fail();
        } catch (IllegalStateException e) {

            //
            // 検証
            //

            assertEquals("openai.api.key is not set", e.getMessage());
        }
    }

    /**
     * <p>異常系：openai.api.key が null の場合、例外がスローされることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateError04() throws Exception {

        //
        // 事前準備
        //

        AppConfig config = new AppConfig(
                "test-discord-token",
                null,
                "test-hotpepper-key"
        );

        try {

            //
            // 実行
            //

            config.validate();
            fail();
        } catch (IllegalStateException e) {

            //
            // 検証
            //

            assertEquals("openai.api.key is not set", e.getMessage());
        }
    }

    /**
     * <p>異常系：hotpepper.api.key が空文字の場合、例外がスローされることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateError05() throws Exception {

        //
        // 事前準備
        //

        AppConfig config = new AppConfig(
                "test-discord-token",
                "test-openai-key",
                ""
        );

        try {

            //
            // 実行
            //

            config.validate();
            fail();
        } catch (IllegalStateException e) {

            //
            // 検証
            //

            assertEquals("hotpepper.api.key is not set", e.getMessage());
        }
    }

    /**
     * <p>異常系：hotpepper.api.key が null の場合、例外がスローされることを確認する。</p>
     *
     * @throws Exception 想定外の例外が発生した場合
     */
    @Test
    public void validateError06() throws Exception {

        //
        // 事前準備
        //

        AppConfig config = new AppConfig(
                "test-discord-token",
                "test-openai-key",
                null
        );

        try {

            //
            // 実行
            //

            config.validate();
            fail();
        } catch (IllegalStateException e) {

            //
            // 検証
            //

            assertEquals("hotpepper.api.key is not set", e.getMessage());
        }
    }
}
