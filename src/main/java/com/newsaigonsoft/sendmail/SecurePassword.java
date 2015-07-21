package com.newsaigonsoft.sendmail;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 * This class provide functions to encrypt and decrypt your password to avoid it easy to see
 * @author canhnm
 *
 */
public class SecurePassword {
	public static void main(String[] args) throws Exception {
		
		String plainPassword;
		if (args.length > 0) {
			plainPassword = args[0];
		} else {
			System.out.println("Please enter your password: ");
			try {
				plainPassword = new String(System.console().readPassword());
			} catch (Exception e) {
				Scanner sc = new Scanner(System.in);
				plainPassword = sc.nextLine();
				sc.close();
			}
		}
		byte[] key = KEY.getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		System.out.println(key.length);
		key = Arrays.copyOf(key, 16); // use only first 128 bit

		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		cipher.update(plainPassword.getBytes());
		String password = Base64.encodeBase64String(cipher.doFinal());
		System.out.println("Please copy follow password to mail.properties:");
		System.out.println(password);
		
	}
	
	public static String getPlainPassword(String encryptedPassword) throws Exception {
		byte[] key = KEY.getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); // use only first 128 bit

		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		cipher.update(Base64.decodeBase64(encryptedPassword));
		String password = new String(cipher.doFinal());
		return password;
	}
	
	private static final String KEY = "j8m2gnzbvkavx7c2a94g";
}
