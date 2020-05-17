package rs.ac.uns.ftn.eventsbackend.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.model.User;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	private final String key = "2s5v8y/B?E(H+MbQeThWmZq3t6w9z$C&"; // 256-bit WEP key

	/**
	 * Koriscenje klase za ocitavanje vrednosti iz application.properties fajla
	 */
	@Autowired
	private Environment env;

	/**
	 * Slanje maila sa linkom za aktivaciju naloga
	 * 
	 * @param user
	 * @throws MailException
	 * @throws InterruptedException
	 * @throws UnsupportedEncodingException 
	 */
	@Async
	public void sendActivationEmail(final User user) throws MailException, InterruptedException, UnsupportedEncodingException {

		String link = "http://localhost:8080/user/activate?id=" + URLEncoder.encode(encript(user.getId().toString()), StandardCharsets.UTF_8.name());

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Account activation link");
		mail.setText(
				"Hello " + user.getName() + ",\n\nBy clicking on the following link your account will be activated.\n"
						+ link + " \n\nWelcome to the Events community!\nEvents app developer team");

		javaMailSender.send(mail);
	}

	/**
	 * 
	 * Slanje Welcome maila za Korisnike koji su se registrovali preko FB
	 * 
	 * @param user
	 * @throws MailException
	 * @throws InterruptedException
	 */
	@Async
	public void sendWelcomeFBEmail(User user) throws MailException, InterruptedException {

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Welcome to Events community");
		mail.setText("Hello " + user.getName() + ",\n\nYou have successfully registered with the Events app.\n\n"
				+ "Your password is: " + user.getPassword()
				+ " in case you ever decide to use Events app without Facebook.\n\nEnjoy the events around you!\nEvents app developer team");

		javaMailSender.send(mail);
	}

	/**
	 * 
	 * Slanje Welcome maila za Korisnike koji su se registrovali preko aplikacije
	 * 
	 * @param user
	 * @throws MailException
	 * @throws InterruptedException
	 */
	@Async
	public void sendWelcomeDBEmail(User user) throws MailException, InterruptedException {

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Welcome to Events community");
		mail.setText("Hello " + user.getName()
				+ ",\n\nYou have successfully registered with the Events app.\n\nEnjoy the events around you!\nEvents app developer team");

		javaMailSender.send(mail);
	}

	@Async
	public void sendForgottenPassword(User user) throws MailException, InterruptedException {

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Your password was reset");
		mail.setText("Hello " + user.getName()
				+ ",\n\nWe wanted to let you know that your Events password was reset.\nYour current password is: "
				+ user.getPassword() + "\nPlease change it as soon as possible!\n\nEvents app developer team");

		javaMailSender.send(mail);
	}

	/**
	 * Stupid encryption of message
	 * 
	 * @param message
	 * @return encrypted message
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 */
	private String encript(String message) {
		try {
			// Create key and cipher
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");

			// encrypt the text
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(message.getBytes());

			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			e.getMessage();
		}

		return Base64.getEncoder().encodeToString(message.getBytes());
	}

	/**
	 * Stupid decryption of message
	 * 
	 * @param encryptedMessage
	 * @return decrypted message
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 */
	public String decript(String encryptedMessage) {
		try {
			byte[] encr = Base64.getDecoder().decode(encryptedMessage);

			// Create key and cipher
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");

			// decrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			String decrypted = new String(cipher.doFinal(encr));
			return decrypted;
		} catch (Exception e) {
			e.getMessage();
		}

		return "";
	}

}
