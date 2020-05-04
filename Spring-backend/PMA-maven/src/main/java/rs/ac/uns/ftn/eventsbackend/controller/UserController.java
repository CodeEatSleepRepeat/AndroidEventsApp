package rs.ac.uns.ftn.eventsbackend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.eventsbackend.comparators.UserComparator;
import rs.ac.uns.ftn.eventsbackend.dto.UserLoginDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserProfileChangeDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserRegisterDTO;
import rs.ac.uns.ftn.eventsbackend.gson.getFbUserProfile.CustomFacebookProfile;
import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.service.EmailService;
import rs.ac.uns.ftn.eventsbackend.service.FacebookService;
import rs.ac.uns.ftn.eventsbackend.service.UserService;

@RequestMapping("/user")
@RestController
public class UserController {
	
	@Autowired
	private FacebookService facebookService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	
	/************ METODE ZA TESTIRANJE *************/
	
	/***
	 * Test za kontroler - ping metoda
	 * @return HttpStatus = 202 -> sve je ok
	 */
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public ResponseEntity<String> ping() {
		System.out.println("Server is pinged");
		return new ResponseEntity<String>("hello from server", HttpStatus.ACCEPTED);
	}
		
	
	/*******************************************************************/
	
	/**
	 * Prijava korisnika koji ima FB nalog povezan sa ovom aplikacijom i koristi FB dugme
	 * @param accessToken - Token dobijen preko FB OAuth 2.0 protokola
	 * @return logged user
	 */
	@RequestMapping(value = "/login/{accessToken}", method = RequestMethod.GET)
	public ResponseEntity<User> login(@PathVariable String accessToken) {
		
		CustomFacebookProfile fbProfile = facebookService.getFbUserProfile(accessToken);
		if (fbProfile==null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		
		User dbUser = userService.findByEmail(fbProfile.getEmail());
		if (dbUser == null) {
			//korisnik je lose odveden na login umesto na register
			return register(accessToken);
		}
		
		//povlacenje liste eventova sa fb i azuriranje iste
		facebookService.pullEvents(accessToken, dbUser);
		
		
		//da li da osvazima profil u skladu sa fb podacima?
		if (!dbUser.getUseFacebookProfile()) {
			//nema azuriranja profila, ali ipak zapamti FB id
			if (!dbUser.getFacebookId().equals(fbProfile.getId())) {
				dbUser.setFacebookId(fbProfile.getId());
				dbUser.setActivatedAccount(true);
				User updatedUser = userService.save(dbUser);
				dbUser = (updatedUser == null) ? dbUser : updatedUser;
			}
			return new ResponseEntity<User>(dbUser, HttpStatus.OK);
		}
		
		//provera da li je mozda ovo prvi put da se korisnik ulogovao preko fb, a pre je isao preko maila i passworda ->update profila ako je "useFacebookProfile" na true
		if (!UserComparator.compare(fbProfile, dbUser)) {
			if (dbUser.getActivatedAccount()) {
				dbUser.setActivatedAccount(true);
				
				//slanje welcome maila
				try {
					emailService.sendWelcomeDBEmail(dbUser);
				} catch (MailException | InterruptedException e) {
					e.printStackTrace();
				}
			}
			dbUser = facebookService.updateDbUser(fbProfile, dbUser.getUserId());
		}

		return new ResponseEntity<User>(dbUser, HttpStatus.OK);
	}
	
	
	/**
	 * Prijava korisnika sa email-om i password-om
	 * @param korisnikPrijavaDTO
	 * @return logged user
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<User> login(@RequestBody UserLoginDTO user) {
		
		User dbUser = userService.findByEmail(user.getEmail(), user.getPassword());
		if (dbUser == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		
		if (dbUser.getActivatedAccount() == false) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<User>(dbUser, HttpStatus.OK);
	}
	
	
	/**
	 * Metoda koja aktivira korisnika preko ovog linka koji je korisnik dobio u emailu
	 * @param encr - Token koji sadrzi enkriptovan User ID
	 * @return
	 */
	@RequestMapping(value = "activate/{encr}", method = RequestMethod.GET)
	public ResponseEntity<User> activateAccount(@PathVariable String encr) {

		try {
			String stringId = emailService.decript(encr);
			
			User user = userService.findById(new Long(stringId));
			if (user == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
	
			user.setActivatedAccount(true);
			User retVal = userService.save(user);
			
			//slanje welcome maila
			try {
				emailService.sendWelcomeDBEmail(retVal);
			} catch (MailException | InterruptedException e) {
				e.printStackTrace();
			}
			
			return new ResponseEntity<>(retVal, HttpStatus.OK);	
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	/**
	 * Registracija korisnika sa email-om i password-om
	 * @param korisnikPrijavaDTO
	 * @return new user
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<User> register(@RequestBody UserRegisterDTO user) {
		
		if (userService.existsByEmail(user.getEmail())) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}
		
		User newUser = new User(user.getName(), user.getEmail(), user.getPassword());
		newUser = userService.save(newUser);
		
		if (newUser.getUserId()!=null) {
			//slanje maila za aktivaciju naloga
			try {
				emailService.sendActivationEmail(newUser);
			} catch (MailException | InterruptedException e) {
				e.printStackTrace();
			}

			return new ResponseEntity<>(newUser, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	
	/**
	 * Registracija korisnika koji ima FB nalog povezan sa ovom aplikacijom i koristi FB dugme
	 * @param korisnikPrijavaDTO
	 * @return new user
	 */
	@RequestMapping(value = "/register/{accessToken}", method = RequestMethod.GET)
	public ResponseEntity<User> register(@PathVariable String accessToken) {
		
		CustomFacebookProfile fbProfile = facebookService.getFbUserProfile(accessToken);
		if (fbProfile==null) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}
		
		//kreiranje i cuvanje novog usera u bazi
		String psw = UUID.randomUUID().toString();
		psw = psw.substring(0, psw.indexOf("-")).toUpperCase();
		User newUser = new User(fbProfile.getName(), fbProfile.getEmail(), psw);
		newUser.setFacebookId(fbProfile.getId());
		newUser.setUserImageURI(fbProfile.getPicture().getData().getUrl());
		newUser.setImageHeight(fbProfile.getPicture().getData().getHeight());
		newUser.setImageWidth(fbProfile.getPicture().getData().getWidth());
		newUser.setActivatedAccount(true);
		
		newUser = userService.save(newUser);
		if (newUser.getUserId()==null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//povlacenje liste eventova sa fb i azuriranje iste
		facebookService.pullEvents(accessToken, newUser);
		
		//slanje welcome maila sa passwordom
		try {
			emailService.sendWelcomeFBEmail(newUser);
		} catch (MailException | InterruptedException e) {
			e.printStackTrace();
		}
			
		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}
	
	/**
	 * Update basic user info in DB
	 * @param user
	 * @return updated user
	 */
	@RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<User> update(@RequestBody UserProfileChangeDTO user) {
		User dbUser = userService.findByEmail(user.getEmail());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if (user.getName()!=null && user.getName().trim().equals("")) {
			//promeni ime
			dbUser.setUserName(user.getName().trim());
		}
		if (user.getImageUri()!=null) {
			//promeni sliku
			dbUser.setUserImageURI(user.getImageUri().trim());
		}
		if (user.getPasswordNew1()!=null && user.getPasswordNew1().trim().equals("")) {
			//promeni password ako je dobar
			if (!dbUser.getPassword().equals(user.getPasswordOld()) || !user.getPasswordNew1().equals(user.getPasswordNew2())) {
				return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
			}
			
			dbUser.setPassword(user.getPasswordNew1());
		}
		
		//sacuvaj usera
		dbUser = userService.save(dbUser);
		if (dbUser.getUserId()==null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(dbUser, HttpStatus.CREATED);
	}
	
	/**
	 * Resets password and send email to user
	 * @param email
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/forgot/{email}", method = RequestMethod.GET)
	public ResponseEntity<Void> forgotPassword(@PathVariable String email) {
		User dbUser = userService.findByEmail(email);
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		//reset password of this user
		String psw = UUID.randomUUID().toString();
		psw = psw.substring(0, psw.indexOf("-")).toUpperCase();
		dbUser.setPassword(psw);
		
		dbUser = userService.save(dbUser);
		if (dbUser.getUserId()==null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//slanje welcome maila sa passwordom
		try {
			emailService.sendForgottenPassword(dbUser);
		} catch (MailException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(HttpStatus.OK);		
	}

}
