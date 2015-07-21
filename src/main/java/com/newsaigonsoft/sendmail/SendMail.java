package com.newsaigonsoft.sendmail;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class provide function to send email
 * 
 * @author canhnm
 *
 */
public class SendMail {
	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		InputStream fis = SendMail.class.getClassLoader().getResourceAsStream(
				"mail.properties");
		try {
			properties.load(fis);

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			fis.close();
		}
		final String username = properties.getProperty("mail.smtp.user");
		final String password = SecurePassword.getPlainPassword(properties
				.getProperty("mail.smtp.password"));

		Session session = Session.getDefaultInstance(properties,
				new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						// TODO Auto-generated method stub
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			MimeMessage message = new MimeMessage(session);
			message.setHeader("Content-Type", "text/plain; charset=UTF-8");
			String to = null, subject = null, body = null;

			if (args.length >= 3) {
				to = args[2];
				subject = args[0];
				body = args[1];
			} else {
				Scanner scanner = new Scanner(System.in);
				System.out.println("Please enter following information");
				System.out.println("To: ");
				to = scanner.nextLine();
				System.out.println("Subject: ");
				subject = scanner.nextLine();
				System.out.println("Body: ");
				body = scanner.nextLine();
				scanner.close();
			}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject, "UTF-8");
			message.setText(body, "UTF-8");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
}
