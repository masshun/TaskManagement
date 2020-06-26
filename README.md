# ユーザー間タスク管理アプリ
登録したユーザー間で頼みごとを依頼し合うCRUDアプリケーション

# メール自動通知機能
頼みごとを作成した際依頼先にメール通知を送る。
頼みごとを完了した際に依頼元にメール通知を送る。
頼みごとの期限が１時間を切ると、依頼先にメール通知を送る。

# 使い方
```bash
git clone https://github.com/masshun/TaskManagement.git
```

# 環境
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
# その他
動作確認：GoogleChrome