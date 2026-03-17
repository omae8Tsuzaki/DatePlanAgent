package com.dateplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>デートプランエージェントアプリケーションのエントリーポイント。</p>
 *
 * <p>このクラスはSpring Bootアプリケーションの起動クラスであり、mainメソッドを含んでいる。</p>
 */
@SpringBootApplication
public class DatePlanAgentApp {

    /**
     * <p>アプリケーションのエントリーポイント。</p>
     * <p>このメソッドはSpringApplication.run()を呼び出して、Spring Bootアプリケーションを起動する。</p>
     *
     * @param args コマンドライン引数（必要に応じて使用される）
     */
    public static void main(String[] args) {
        SpringApplication.run(DatePlanAgentApp.class, args);
    }
}
