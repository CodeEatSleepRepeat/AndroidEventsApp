package rs.ac.uns.ftn.eventsbackend.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rs.ac.uns.ftn.eventsbackend.comparators.UserComparator;
import rs.ac.uns.ftn.eventsbackend.dto.UserLoginDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserProfileChangeDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserRegisterDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserSyncChangeDTO;
import rs.ac.uns.ftn.eventsbackend.gson.getFbUserProfile.CustomFacebookProfile;
import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.service.EmailService;
import rs.ac.uns.ftn.eventsbackend.service.FacebookService;
import rs.ac.uns.ftn.eventsbackend.service.UserService;

@RequestMapping("/user")
@RestController
public class UserController {

	@Value("${userImages}")
	private String IMAGE_FOLDER;

	private final List<String> MIME_IMAGE_TYPES = new ArrayList<>(Arrays.asList("image/bmp", "image/gif", "image/jpeg",
			"image/png", "image/x-icon", "image/vnd.microsoft.icon"));

	@Autowired
	private FacebookService facebookService;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	/************ METODE ZA TESTIRANJE *************/

	/***
	 * Test za kontroler - ping metoda
	 * 
	 * @return HttpStatus = 202 -> sve je ok
	 */
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public ResponseEntity<String> ping() {
		System.out.println("Server is pinged");
		return new ResponseEntity<String>("hello from server", HttpStatus.ACCEPTED);
	}

	/*******************************************************************/

	/**
	 * Prijava korisnika koji ima FB nalog povezan sa ovom aplikacijom i koristi FB
	 * dugme
	 * 
	 * @param accessToken - Token dobijen preko FB OAuth 2.0 protokola
	 * @return logged user
	 */
	@RequestMapping(value = "/login/{accessToken}", method = RequestMethod.GET)
	public ResponseEntity<User> login(@PathVariable String accessToken) {

		CustomFacebookProfile fbProfile = facebookService.getFbUserProfile(accessToken);
		if (fbProfile == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		User dbUser = userService.findByEmail(fbProfile.getEmail());
		if (dbUser == null) {
			// korisnik je lose odveden na login umesto na register
			return register(accessToken);
		}

		// provera da li je mozda ovo prvi put da se korisnik ulogovao preko fb,
		// a pre je isao preko maila i passworda
		if (!dbUser.getActivatedAccount()) {
			dbUser.setActivatedAccount(true);
			dbUser.setSyncFacebookEvents(true);
			dbUser.setSyncFacebookProfile(true);
			User updatedUser = userService.save(dbUser);
			dbUser = (updatedUser == null) ? dbUser : updatedUser;

			// slanje welcome maila
			try {
				emailService.sendWelcomeDBEmail(dbUser);
			} catch (MailException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		// povlacenje liste eventova sa fb i azuriranje iste
		if (dbUser.getSyncFacebookEvents()) {
			facebookService.pullEvents(accessToken, dbUser);
		}

		// da li da osvazima profil u skladu sa fb podacima?
		if (!dbUser.getSyncFacebookProfile()) {
			// nema azuriranja profila, ali ipak zapamti FB id
			if (!dbUser.getFacebookId().equals(fbProfile.getId())) {
				dbUser.setFacebookId(fbProfile.getId());
				dbUser.setActivatedAccount(true);
				User updatedUser = userService.save(dbUser);
				dbUser = (updatedUser == null) ? dbUser : updatedUser;
			}
			return new ResponseEntity<User>(dbUser, HttpStatus.OK);
		}

		// update profila ako je syncFacebookProfile=true i postoji promena FB profila
		if (!UserComparator.compare(fbProfile, dbUser)) {
			dbUser = facebookService.updateDbUser(fbProfile, dbUser.getId());
		}

		return new ResponseEntity<User>(dbUser, HttpStatus.OK);
	}

	/**
	 * Prijava korisnika sa email-om i password-om
	 * 
	 * @param korisnikPrijavaDTO
	 * @return logged user
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<User> login(@RequestBody UserLoginDTO user) {

		User dbUser = userService.findByCredentials(user.getEmail(), user.getPassword());
		if (dbUser == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		if (dbUser.getActivatedAccount() == false) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<User>(dbUser, HttpStatus.OK);
	}

	/**
	 * Metoda koja aktivira korisnika preko ovog linka koji je korisnik dobio u
	 * emailu
	 * 
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

			// slanje welcome maila
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
	 * 
	 * @param korisnikPrijavaDTO
	 * @return new user
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<User> register(@RequestBody UserRegisterDTO user) {

		if (userService.existsByEmail(user.getEmail())) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		User newUser = new User(user.getName(), user.getEmail(), user.getPassword());

		// da li je slika uploade-ovana
		if (!user.getImageUri().equals("")) {
			newUser.setImageUri(user.getImageUri());
		}

		newUser = userService.save(newUser);

		if (newUser.getId() != null) {
			// slanje maila za aktivaciju naloga
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
	 * Registracija korisnika koji ima FB nalog povezan sa ovom aplikacijom i
	 * koristi FB dugme
	 * 
	 * @param korisnikPrijavaDTO
	 * @return new user
	 */
	@RequestMapping(value = "/register/{accessToken}", method = RequestMethod.GET)
	public ResponseEntity<User> register(@PathVariable String accessToken) {

		CustomFacebookProfile fbProfile = facebookService.getFbUserProfile(accessToken);
		if (fbProfile == null) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		// kreiranje i cuvanje novog usera u bazi
		String psw = UUID.randomUUID().toString();
		psw = psw.substring(0, psw.indexOf("-")).toUpperCase();
		User newUser = new User(fbProfile.getName(), fbProfile.getEmail(), psw);
		newUser.setFacebookId(fbProfile.getId());
		newUser.setImageUri(fbProfile.getPicture().getData().getUrl());
		newUser.setImageHeight(fbProfile.getPicture().getData().getHeight());
		newUser.setImageWidth(fbProfile.getPicture().getData().getWidth());
		newUser.setActivatedAccount(true);
		newUser.setSyncFacebookEvents(true);
		newUser.setSyncFacebookProfile(true);

		newUser = userService.save(newUser);
		if (newUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// povlacenje liste eventova sa fb i azuriranje iste
		facebookService.pullEvents(accessToken, newUser);

		// slanje welcome maila sa passwordom
		try {
			emailService.sendWelcomeFBEmail(newUser);
		} catch (MailException | InterruptedException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}

	/**
	 * Update basic user info in database (name, password and user image)
	 * 
	 * @param user
	 * @return updated user
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<User> update(@RequestBody UserProfileChangeDTO user) {
		User dbUser = userService.findByEmail(user.getEmail());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (user.getName() != null && user.getName().trim().equals("")) {
			// promeni ime
			dbUser.setName(user.getName().trim());
		}
		if (user.getImageUri() != null) {
			// obrisi staru sliku ako postoji na serveru
			removeImage(dbUser.getImageUri());

			// promeni sliku
			dbUser.setImageUri(user.getImageUri().trim());
		}
		if (user.getPasswordNew1() != null && user.getPasswordNew1().trim().equals("")) {
			// promeni password ako je dobar
			if (!dbUser.getPassword().equals(user.getPasswordOld())
					|| !user.getPasswordNew1().equals(user.getPasswordNew2())) {
				return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
			}

			dbUser.setPassword(user.getPasswordNew1());
		}

		// sacuvaj usera
		dbUser = userService.save(dbUser);
		if (dbUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(dbUser, HttpStatus.CREATED);
	}

	/**
	 * Resets password and send email to user
	 * 
	 * @param email
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/forgot/{email}", method = RequestMethod.GET)
	public ResponseEntity<Void> forgotPassword(@PathVariable String email) {
		User dbUser = userService.findByEmail(email);
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// reset password of this user
		String psw = UUID.randomUUID().toString();
		psw = psw.substring(0, psw.indexOf("-")).toUpperCase();
		dbUser.setPassword(psw);

		dbUser = userService.save(dbUser);
		if (dbUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// slanje welcome maila sa passwordom
		try {
			emailService.sendForgottenPassword(dbUser);
		} catch (MailException | InterruptedException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Delete user from database
	 * 
	 * @param user to be deleted
	 * @return OK if deleted, else NOT_FOUND
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@RequestBody UserLoginDTO user) {

		User dbUser = userService.findByCredentials(user.getEmail(), user.getPassword());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		userService.delete(dbUser.getId());
		removeImage(dbUser.getImageUri());

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Prijava korisnika koji ima FB nalog povezan sa ovom aplikacijom i koristi FB
	 * dugme
	 * 
	 * @param accessToken - Token dobijen preko FB OAuth 2.0 protokola
	 * @return logged user
	 */
	@RequestMapping(value = "/unlink/{email}", method = RequestMethod.POST)
	public ResponseEntity<User> unlink(@RequestBody UserLoginDTO user) {
		User dbUser = userService.findByCredentials(user.getEmail(), user.getPassword());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		dbUser.setFacebookId("");
		dbUser.setSyncFacebookEvents(false);
		dbUser.setSyncFacebookProfile(false);
		// TODO: dodati brsanje korisnikovih eventova koji su skinuti sa FB

		dbUser = userService.save(dbUser);
		if (dbUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<User>(dbUser, HttpStatus.OK);
	}

	/**
	 * Update sync status of Facebook profile
	 * 
	 * @param user
	 * @return updated user
	 */
	@RequestMapping(value = "/update/profile", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<User> syncFBProfile(@RequestBody UserSyncChangeDTO syncSettings) {
		User dbUser = userService.findByEmail(syncSettings.getEmail());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		dbUser.setSyncFacebookProfile(syncSettings.getSync());
		dbUser = userService.save(dbUser);
		if (dbUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<User>(dbUser, HttpStatus.OK);
	}

	/**
	 * Update sync status of Facebook Events
	 * 
	 * @param user
	 * @return updated user
	 */
	@RequestMapping(value = "/update/events", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<User> syncFBEvents(@RequestBody UserSyncChangeDTO syncSettings) {
		User dbUser = userService.findByEmail(syncSettings.getEmail());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		dbUser.setSyncFacebookEvents(syncSettings.getSync());
		dbUser = userService.save(dbUser);
		if (dbUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<User>(dbUser, HttpStatus.OK);
	}

	/**
	 * Upload user image to server for storage
	 * 
	 * @param image
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadImage(@RequestPart(name = "image") MultipartFile image) {

		if (image != null && !image.isEmpty()) {
			if (!MIME_IMAGE_TYPES.contains(image.getContentType())) {
				// sent file is not valid type
				return new ResponseEntity<>(null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
			}
			try {
				// image is good size
				String newImageName = System.currentTimeMillis()
						+ image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
				String newFileUri = new File(IMAGE_FOLDER + newImageName).getAbsolutePath();

				// save image to folder
				image.transferTo(new File(newFileUri));
				return new ResponseEntity<>(newImageName, HttpStatus.CREATED);

			} catch (Exception e) {
				System.out.println("Failed saving image...");
			}

		} else {
			// image is empty
			return new ResponseEntity<>(null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}

		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Delete old user image when updating user profile
	 * 
	 * @param userImageURI
	 */
	private void removeImage(@PathVariable String userImageURI) {
		if (!userImageURI.equals("")) {
			File oldImage = new File(IMAGE_FOLDER + userImageURI);
			oldImage.delete();
		}
	}
}
