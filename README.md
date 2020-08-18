## ユーザー間タスク管理アプリ
登録したユーザー間で頼みごとを依頼し合うCRUDアプリケーション

## 機能
- 頼みごとの新規登録・閲覧・更新・削除
- 頼みごとの検索
- ページネーション
- 認証
- メール認証を用いたサインアップ
- 頼みごとの新規登録・更新時に1色のラベルを設定し優先順位付けが行えるようにする。
- 頼まれたことをカレンダーで確認する機能
- 頼みごとの作成、更新時に依頼相手にメール通知を送る。
- 頼まれたことの完了ボタンを押すと、依頼元にメール通知を送る。
- 頼みごとの期限が1時間を切ると、依頼先にメール通知を送る。


## デプロイまでのアーキテクチャ図
![Untitled Diagram (2)](https://user-images.githubusercontent.com/60130295/85918518-7d9ce800-b89e-11ea-9370-f922bc5026bb.png)

## ER図
![Class Diagram (1)](https://user-images.githubusercontent.com/60130295/90215018-593ca100-de35-11ea-8312-0100ed49b924.jpg)

## デモプレイ用ログインアカウント
```bash
URL:http://task-management-app:8080
ユーザー名:hoge
パスワード:password
```

## 開発言語,使用技術
- Java
- Spring Framework
- MySQL
- HTML
- CSS
- JavaScript
- Git,GitHub
- Jenkins
- AWS(S3,CodePipeline,CodeDeploy,EC2,RDS,Route53)

## その他使用技術
- 非同期処理を用いたメール送信
- 定期実行と非同期を用いて、期限1時間前の頼みごと依頼先にメール通知

## ローカル用プロパティ
application.properties
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/task_management
spring.datasource.username=任意のユーザー名
spring.datasource.password=任意のパスワード名
spring.session.store-type=jdbc

server.port=9996
spring.mail.host=smtp.gmail.com
spring.mail.username=登録したメールアドレス
spring.mail.password=2段階認証の際に設定したパスワード
spring.mail.port=587
spring.mail.properties.mail.smtp.auth: true
spring.mail.properties.mail.smtp.starttls.enable: true

spring.datasource.schema=classpath:schema.sql
spring.datasource.initialization-mode=always
```

## クローン
```bash
git clone https://github.com/masshun/TaskManagement.git
```

## その他
動作確認：GoogleChrome