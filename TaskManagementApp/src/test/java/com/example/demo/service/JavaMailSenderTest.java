package com.example.demo.service;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.sun.mail.imap.IMAPStore;

@SpringBootTest
@Transactional
public class JavaMailSenderTest {

	private GreenMail greenMail;
	private GreenMailUser user;
	private Session session = null;

	@BeforeEach
	public void setUp() {
		this.greenMail = new GreenMail(ServerSetupTest.SMTP_IMAP);
		// テスト用SMTPサーバーを起動
		this.greenMail.start();

		// メール送信を設定
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		this.session = Session.getDefaultInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("matsushun753@gmail.com", "egqfycstvxkxpxfq");// Specify the Username
																								// and the PassWord
			}
		});
	}

	@AfterEach
	public void tearDown() {
		this.greenMail.stop();
	}

	@Test
	public void testSendAndReceive() throws Exception {

		// メール送信内容を設定
		MimeMessage message = new MimeMessage(this.session);
		message.setRecipient(RecipientType.TO, new InternetAddress("matsushun753@gmail.com"));
		message.setFrom(new InternetAddress("mmada8293@gmail.com"));
		message.setSubject("タイトルです");
		message.setText("本文です");

		// メールを送信
		this.user = greenMail.setUser("matsushun753@gmail.com", "egqfycstvxkxpxfq");
		this.user.deliver(message);

		// IMAPはサーバー上でメール管理
		IMAPStore imapStore = greenMail.getImap().createStore();
		imapStore.connect("matsushun753@gmail.com", "egqfycstvxkxpxfq");
		// サーバー上の特定のユーザーのプライマリーなフォルダ
		Folder inbox = imapStore.getFolder("INBOX");
		inbox.open(Folder.READ_ONLY);
		Message msgReceived = inbox.getMessage(1);
		assertEquals(message.getSubject(), msgReceived.getSubject());

		// 直接フェッチする
		Message[] messages = greenMail.getReceivedMessages();
		Message actual = messages[0];
		assertEquals(message.getSubject(), actual.getSubject());
		assertEquals(message.getContent(), actual.getContent());

	}

}
