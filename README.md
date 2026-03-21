# DatePlanAgent

- デートプランを提案するAIエージェント

## 概要

- 日付とエリアを入力すると、天気とレストラン情報を並列取得し、LLMでデートプランを生成して返すAIエージェント。

## セットアップ手順

### 前提条件

- Java 21 以上
- Gradle 9.0 以上
- Discord Bot トークン
- OpenAI API キー
- ホットペッパー API キー

### 手順

**1. リポジトリをクローン**

```sh
git clone https://github.com/omae8Tsuzaki/DatePlanAgent.git
cd DatePlanAgent
```

**2. 環境変数を設定**

以下の環境変数を設定してください。

| 環境変数 | 説明 |
| ----- | ----- |
| `DISCORD_TOKEN` | Discord Bot のトークン |
| `OPENAI_API_KEY` | OpenAI の API キー |
| `HOTPEPPER_API_KEY` | ホットペッパー Web サービスの API キー |

または `src/main/resources/application.properties` に直接記述することも可能です。

```properties
discord.token=your_discord_token
openai.api.key=your_openai_api_key
hotpepper.api.key=your_hotpepper_api_key
```

**3. ビルド・起動**

```sh
./gradlew bootRun
```

## コマンドの使用方法

DatePlanAgentは2種類のコマンド形式に対応しています。

### スラッシュコマンド

```sh
/dateplan date:<日付> area:<エリア>
```

| オプション | 説明 | 例 |
| ----- | ----- | -------------- |
| `date` | デートの日付（`YYYY-MM-DD` 形式） | `2026-03-15` |
| `area` | デートのエリア | `渋谷`、`梅田` |

- **使用例:**

    ```sh
    /dateplan date:2026-03-15 area:渋谷
    ```

### メッセージコマンド

```sh
!dateplan <日付> <エリア>
```

- **使用例:**

    ```sh
    !dateplan 2026-03-15 渋谷
    ```

### 注意事項

- 日付は `YYYY-MM-DD` 形式で入力してください（例: `2026-03-15`）
- エリアは渋谷・梅田などの地名を入力してください
- プランの生成には少し時間がかかります

## 使用技術

| カテゴリ | 技術・ライブラリ        |
| ----- |-----------------|
| 言語 | Java 21         |
| フレームワーク | Spring Boot     |
| Discord | JDA (Java Discord API) |
| AI | OpenAI Java SDK |
| JSON | Gson            |
| ビルドツール | Gradle          |
| テスト | JUnit 5         |

### 外部 API

| API | 用途 | 認証 |
| ----- | ----- | ----- |
| 気象庁 API | 天気情報の取得 | 不要 |
| ホットペッパー Web サービス | レストラン情報の取得 | API キー必要 |
| OpenAI API | デートプランの生成（`gpt-5-nano-2025-08-07`） | API キー必要 |
