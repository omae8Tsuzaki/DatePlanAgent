# CLAUDE.md

このファイルは、Claude Code (claude.ai/code) がこのリポジトリで作業する際のガイドです。

## ビルド・実行コマンド

```bash
./gradlew compileJava          # コンパイルのみ
./gradlew test                 # 全テスト実行 (JUnit 5)
./gradlew test --tests "com.dateplan.util.AreaCodeResolverTest"  # 単一テストクラス実行
./gradlew bootRun              # Discord Botを起動（Spring Boot）
```

Gradle 9.0、Java 21+、Spring Boot 4.0.3。設定は `src/main/resources/application.properties` で管理（必要なキーは `application.properties.example` を参照: `discord.token`, `openai.api.key`, `hotpepper.api.key`）。プロパティが未設定の場合は環境変数（`DISCORD_TOKEN`, `OPENAI_API_KEY`, `HOTPEPPER_API_KEY`）にフォールバックする。Webサーバーは不要なため `spring.main.web-application-type=none` を設定している。

## アーキテクチャ

デートプランを生成するDiscord Bot（Spring Boot）。ユーザーが日付とエリアを入力すると（例: `/dateplan date:2026-03-15 area:渋谷`）、天気とレストラン情報を並列取得し、OpenAIでデートプランを生成して返す。

**リクエストフロー:**
Discordコマンド → `SlashCommandHandler` or `MessageCommandHandler` → `DatePlanAgent.generatePlan()` → `WeatherApiClient` + `HotPepperApiClient`（並列） → `OpenAiClient` → Discordへ応答

**パッケージ構成:**
- `com.dateplan` — `DatePlanAgentApp`（`@SpringBootApplication`）、`AppConfig`（`@Component`、`@Value`による設定注入）
- `com.dateplan.agent` — `DatePlanAgent`（`@Service`、オーケストレーター）
- `com.dateplan.api` — `WeatherApiClient`、`HotPepperApiClient`（`@Component`、外部データ取得系APIクライアント）
- `com.dateplan.api.ai` — `AiClient`（インターフェース）、`OpenAiClient`（`@Component`）、`DatePlanPromptBuilder`（`@Component`、プロンプト生成）
- `com.dateplan.discord` — `DiscordBot`（`@Component`、`CommandLineRunner`）、`SlashCommandHandler`、`MessageCommandHandler`
- `com.dateplan.entity` — `DatePlanRequest`、`DatePlan`、`WeatherInfo`、`Restaurant`（すべてrecord）
- `com.dateplan.util` — `AreaCodeResolver`（staticユーティリティ）

**DI構成:**
Spring Bootのコンストラクタインジェクションで全Bean（`AppConfig` → `WeatherApiClient` / `HotPepperApiClient` / `OpenAiClient` / `DatePlanPromptBuilder` → `DatePlanAgent` → `DiscordBot`）を管理。`DiscordBot`が`CommandLineRunner`としてJDAを起動する。

**設計上の重要ポイント:**
- `DatePlanAgent` がオーケストレーター。天気・レストランAPIを `CompletableFuture.allOf` で並行実行し、結果をまとめてOpenAIに渡す
- 全APIクライアントは `CompletableFuture` を返す非同期設計。天気・レストラン取得の失敗は `exceptionally()` でフォールバックし、プラン生成は継続する
- `AreaCodeResolver` はエリア名（渋谷、梅田等）を `area_codes.json` で気象庁エリアコードに変換。完全一致 → エイリアス一致 → 部分一致の順で解決
- Discordハンドラは非同期処理前に `event.deferReply()` を呼び、3秒のインタラクションタイムアウトを回避
- 2000文字超のメッセージはDiscordの上限に合わせて分割送信

**外部API:**
- 気象庁: `https://www.jma.go.jp/bosai/forecast/data/overview_forecast/{areaCode}.json` — 認証不要
- ホットペッパー: `http://webservice.recruit.co.jp/hotpepper/gourmet/v1/` — APIキー必要
- OpenAI: `gpt-5-nano-2025-08-07` モデルによるChat Completion（公式Java SDK使用）

**言語:** ユーザー向け出力・プロンプトはすべて日本語。
