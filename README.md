## ユーザー間タスク管理アプリ
登録したユーザー間で頼みごとを依頼し合うCRUDアプリケーション

## 機能
- ページネーション
- 頼みごとの新規登録・閲覧・更新・削除
- 頼みごとの検索
- メール認証
- 頼みごとを作成した際依頼先にメール通知を送る。
- 頼みごとを完了した際に依頼元にメール通知を送る。
- 頼みごとの期限が１時間を切ると、依頼先にメール通知を送る。


## デプロイまでのアーキテクチャ図
![Untitled Diagram](https://user-images.githubusercontent.com/60130295/85916747-9a312400-b88e-11ea-87f0-25ebf7febcba.png)

## 開発言語,使用技術
- Java
- Spring
- MySQL
- HTML
- CSS
- JavaScript
- Git,GitHub
- Jenkins
- AWS(S3,CodePipeline,CodeDeploy,EC2,RDS,Route53)

## プロパティ
application.properties
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/task_management
spring.datasource.username=spring1
spring.datasource.password=spring1
spring.session.store-type=jdbc

server.port=9996
spring.mail.host =smtp.gmail.com
spring.mail.username=登録したメールアドレス
spring.mail.password=二段階認証の際に設定したパスワード
spring.mail.port=587
spring.mail.properties.mail.smtp.auth: true
spring.mail.properties.mail.smtp.starttls.enable: true

spring.datasource.schema=classpath:schema.sql
spring.datasource.initialization-mode=always
```
## 使い方
```bash
git clone https://github.com/masshun/TaskManagement.git
```

## その他
動作確認：GoogleChrome