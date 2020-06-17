package rs.ac.uns.ftn.eventsbackend.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rs.ac.uns.ftn.eventsbackend.comparators.UserComparator;
import rs.ac.uns.ftn.eventsbackend.dto.UserDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserFBSyncChangeDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserLoginDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserProfileChangeDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserProfileSyncDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UserRegisterDTO;
import rs.ac.uns.ftn.eventsbackend.enums.SyncStatus;
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

	/**
	 * Prijava korisnika koji ima FB nalog povezan sa ovom aplikacijom i koristi FB
	 * dugme
	 * 
	 * @param accessToken - Token dobijen preko FB OAuth 2.0 protokola
	 * @return logged user
	 */
	@RequestMapping(value = "/login/{accessToken}", method = RequestMethod.GET)
	public ResponseEntity<User> login(@PathVariable String accessToken) {
		System.out.println("login-fb");

		CustomFacebookProfile fbProfile = facebookService.getFbUserProfile(accessToken);
		if (fbProfile == null) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
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

			final User u = dbUser;
			new Thread(new Runnable() {
				@Override
				public void run() {
					// slanje welcome maila
					try {
						emailService.sendWelcomeDBEmail(u);
					} catch (MailException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
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

		// update profila ako postoji promena FB profila
		if (!UserComparator.compare(fbProfile, dbUser)) {
			dbUser = facebookService.updateDbUser(fbProfile, dbUser.getId());
		}

		dbUser.setFacebookToken(accessToken);
		userService.save(dbUser);

		return new ResponseEntity<User>(dbUser, HttpStatus.OK);
	}

	/**
	 * Prijava korisnika sa email-om i password-om
	 * 
	 * @param korisnikPrijavaDTO
	 * @return logged user
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<User> login(@RequestBody @Valid UserLoginDTO user) {
		System.out.println("login");

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
	@RequestMapping(value = "activate", method = RequestMethod.GET)
	public ResponseEntity<User> activateAccount(HttpServletRequest request) {

		String encr = (String) request.getParameter("id");
		try {
			String stringId = emailService.decript(URLDecoder.decode(encr, StandardCharsets.UTF_8.name()));

			User user = userService.findById(new Long(stringId));
			if (user == null) {
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}

			user.setActivatedAccount(true);
			final User retVal = userService.save(user);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// slanje welcome maila
					try {
						emailService.sendWelcomeDBEmail(retVal);
					} catch (MailException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();

			return new ResponseEntity<>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Registracija korisnika sa imenom, email-om i password-om
	 * 
	 * @param UserRegisterDTO user
	 * @return User newUser
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<User> register(@RequestBody @Valid UserRegisterDTO user) {
		System.out.println("register");

		if (userService.existsByEmail(user.getEmail())) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		User newUser = new User(user.getName(), user.getEmail(), user.getPassword());
		newUser = userService.save(newUser);

		if (newUser.getId() != null) {
			final User u = newUser;
			// slanje maila za aktivaciju naloga
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						emailService.sendActivationEmail(u);
					} catch (MailException | InterruptedException | UnsupportedEncodingException e) {
						System.out.println("email failed");
						e.printStackTrace();
					}
				}
			}).start();

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
		System.out.println("register-fb");

		CustomFacebookProfile fbProfile = facebookService.getFbUserProfile(accessToken);
		if (fbProfile == null) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		if (userService.findByEmail(fbProfile.getEmail()) != null) {
			// pogresno je otisao ovde, treba na login
			return login(accessToken);
		}

		// kreiranje i cuvanje novog usera u bazi
		String psw = UUID.randomUUID().toString();
		psw = psw.substring(0, psw.indexOf("-")).toUpperCase() + "F7a";
		User newUser = new User(fbProfile.getName(), fbProfile.getEmail(), psw);
		newUser.setFacebookId(fbProfile.getId());
		newUser.setImageUri(fbProfile.getUrl());
		newUser.setActivatedAccount(true);
		newUser.setSyncFacebookEvents(true);
		newUser.setSyncFacebookProfile(true);
		newUser.setFacebookToken(accessToken);

		newUser = userService.save(newUser);
		if (newUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// povlacenje liste eventova sa fb i azuriranje iste
		facebookService.pullEvents(accessToken, newUser);

		// slanje maila za aktivaciju naloga
		final User u = newUser;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					emailService.sendWelcomeFBEmail(u);
				} catch (MailException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}

	/**
	 * Update basic user info in database (name, password and delete user image)
	 * 
	 * @param user
	 * @return updated user
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<User> update(@RequestBody @Valid UserProfileChangeDTO user) {
		System.out.println("update user");

		User dbUser = userService.findByEmail(user.getEmail());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (user.getName() != null && !user.getName().trim().equals("")) {
			// promeni ime
			dbUser.setName(user.getName().trim());
			if (dbUser.getSyncFacebookProfile()) {
				dbUser.setSyncFacebookProfile(false);	//deactivate fb profile update if manual profile change happened
			}
		}
		if (user.getImageUri() != null && user.getImageUri().equals("")) {
			// obrisi staru sliku ako postoji na serveru
			removeImage(dbUser.getImageUri());

			// promeni sliku
			dbUser.setImageUri("");
			if (dbUser.getSyncFacebookProfile()) {
				dbUser.setSyncFacebookProfile(false);	//deactivate fb profile update if manual profile change happened
			}
		}
		if (user.getPasswordNew1() != null && !user.getPasswordNew1().trim().equals("")) {
			// promeni password ako je dobar
			if (!dbUser.getPassword().equals(user.getPasswordOld())
					|| !user.getPasswordNew1().equals(user.getPasswordNew2())) {
				return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
			}

			dbUser.setPassword(user.getPasswordNew1());
		}

		dbUser.setSyncStatus(SyncStatus.UPDATE);
		dbUser.setUpdated_time(ZonedDateTime.now());

		// sacuvaj usera
		dbUser = userService.save(dbUser);
		if (dbUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(dbUser, HttpStatus.CREATED);
	}

	/**
	 * Check if user profile on android is same as in server DB
	 * 
	 * @param user
	 * @return updated user
	 */
	@RequestMapping(value = "/sync", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<User> syncUser(@RequestBody @Valid UserProfileSyncDTO data) {
		System.out.println("sync user with last updated time: " + data.getLastSyncTime());

		User dbUser = userService.findByCredentials(data.getEmail(), data.getPassword());
		if (dbUser == null) {
			System.out.println("sync user user not found");
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		//sync with fb if user allows
		if (dbUser.getSyncFacebookProfile()) {
			CustomFacebookProfile fbProfile = facebookService.getFbUserProfile(dbUser.getFacebookToken());
			if (fbProfile != null) {
				// update profila ako postoji promena FB profila
				if (!UserComparator.compare(fbProfile, dbUser)) {
					dbUser = facebookService.updateDbUser(fbProfile, dbUser.getId());
				}
			}
		}

		if (data.getLastSyncTime() == null) {
			System.out.println("sync user no last update time - sending user");
			return new ResponseEntity<>(dbUser, HttpStatus.OK);
		}
		
		if (dbUser.getUpdated_time().toInstant().isAfter(Instant.ofEpochMilli(data.getLastSyncTime()))) {
			// need to update
			System.out.println("sync user last update time to long ago - sending user");
			return new ResponseEntity<>(dbUser, HttpStatus.OK);
		}

		// no need to update
		System.out.println("sync user no need to update - sending null");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Resets password and send email to user
	 * 
	 * @param email
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/forgot/{email}", method = RequestMethod.GET)
	public ResponseEntity<Void> forgotPassword(@PathVariable @Email String email) {
		User dbUser = userService.findByEmail(email);
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// reset password of this user
		String psw = UUID.randomUUID().toString();
		psw = psw.substring(0, psw.indexOf("-")).toUpperCase() + "F7a";
		dbUser.setPassword(psw);
		dbUser.setSyncStatus(SyncStatus.UPDATE);
		dbUser.setUpdated_time(ZonedDateTime.now());

		dbUser = userService.save(dbUser);
		if (dbUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		final User u = dbUser;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// slanje novog passworda
				try {
					emailService.sendForgottenPassword(u);
				} catch (MailException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Delete user from database
	 * 
	 * @param user to be deleted
	 * @return OK if deleted, else NOT_FOUND
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Void> delete(@RequestBody @Valid UserLoginDTO user) {
		System.out.println("delete");

		User dbUser = userService.findByCredentials(user.getEmail(), user.getPassword());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// TODO: dodati brisanje ili redefinisanje ownera eventova

		userService.delete(dbUser.getId());
		removeImage(dbUser.getImageUri());

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Odvezivanje FB naloga sa Events app nalogom - brisanje podesavanja fb i
	 * brisanje fb eventova
	 * 
	 * @param UserLoginDTO user - email i password za autentifikaciju
	 * @return novi user
	 */
	@RequestMapping(value = "/unlink", method = RequestMethod.POST)
	public ResponseEntity<User> unlink(@RequestBody @Valid UserLoginDTO user) {
		System.out.println("unlink fb");

		User dbUser = userService.findByCredentials(user.getEmail(), user.getPassword());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		dbUser.setFacebookId("");
		dbUser.setSyncFacebookEvents(false);
		dbUser.setSyncFacebookProfile(false);
		dbUser.setSyncStatus(SyncStatus.UPDATE);
		dbUser.setUpdated_time(ZonedDateTime.now());

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
	public ResponseEntity<User> syncFBProfile(@RequestBody @Valid UserFBSyncChangeDTO syncSettings) {
		User dbUser = userService.findByEmail(syncSettings.getEmail());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		dbUser.setSyncFacebookProfile(syncSettings.getSync());
		dbUser.setSyncStatus(SyncStatus.UPDATE);
		dbUser.setUpdated_time(ZonedDateTime.now());
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
	public ResponseEntity<User> syncFBEvents(@RequestBody @Valid UserFBSyncChangeDTO syncSettings) {
		User dbUser = userService.findByEmail(syncSettings.getEmail());
		if (dbUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		dbUser.setSyncFacebookEvents(syncSettings.getSync());
		dbUser.setSyncStatus(SyncStatus.UPDATE);
		dbUser.setUpdated_time(ZonedDateTime.now());
		dbUser = userService.save(dbUser);
		if (dbUser.getId() == null) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<User>(dbUser, HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<User> getUser(@PathVariable Long userId) throws Exception {
		User foundUser = userService.getById(userId);
		return ResponseEntity.ok(foundUser);
	}

	@GetMapping("/friendsOf/{userId}")
	public ResponseEntity<List<User>> getFriendsOfUser(@PathVariable Long userId) throws Exception {
		List<User> userFriends = userService.findAllFriendsOfUser(userId);
		return ResponseEntity.ok(userFriends);
	}

	@GetMapping("/containsUsername/{userId}/{username}/page/{num}")
	public ResponseEntity<List<User>> getUsersWhichContainsUsername(@PathVariable int num,
			@PathVariable String username, @PathVariable Long userId) {
		Pageable pageable = PageRequest.of(num, 10);
		List<User> foundUsers = userService.findAllWhichContainsUsernamePageable(userId, username, pageable);
		return ResponseEntity.ok(foundUsers);
	}

	@GetMapping("/getFriendsForEventsInvite/{senderId}/{eventId}")
	public ResponseEntity<List<User>> getFriendsForEventsInvite(@PathVariable Long senderId,
																@PathVariable Long eventId) throws Exception {
		List<User> foundFriends = userService.findAllFriendsWhoAreNotYetInvitedToEvent(senderId, eventId);
		return ResponseEntity.ok(foundFriends);
	}

	@GetMapping("/image/{name}")
	public ResponseEntity<byte[]> getImage(@PathVariable String name) throws IOException {
		System.out.println("get user image");

		try {
			File f = new File(IMAGE_FOLDER + name);
			FileInputStream fis = new FileInputStream(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				baos.write(buf, 0, readNum);
			}
			byte[] bytes = baos.toByteArray();
			fis.close();
			baos.close();
			return new ResponseEntity<byte[]>(bytes, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Upload user image to server for storage
	 * 
	 * @param image
	 * @return
	 */
	@RequestMapping(value = "/upload/{id}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<User> uploadImage(@PathVariable Long id, @RequestPart(name = "image") MultipartFile image) {
		System.out.println("upload user image");

		if (image != null && !image.isEmpty()) {
			if (!MIME_IMAGE_TYPES.contains(image.getContentType())) {
				// sent file is not valid type
				return new ResponseEntity<>(null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
			}
			try {
				User user = userService.findById(id);
				if (user == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

				// image is good size
				String newImageName = System.currentTimeMillis() + "";
				String newFileUri = new File(IMAGE_FOLDER + newImageName).getAbsolutePath();

				// save image to folder
				image.transferTo(new File(newFileUri));

				if (user.getImageUri() != null) {
					removeImage(user.getImageUri());
				}
				user.setImageUri(newImageName);
				user.setSyncFacebookProfile(false);	//deactivate fb profile update if manual image change happened
				user.setSyncStatus(SyncStatus.UPDATE);
				user.setUpdated_time(ZonedDateTime.now());
				user = userService.save(user);
				System.out.println("new user image: " + user.getImageUri());
				return new ResponseEntity<>(user, HttpStatus.CREATED);

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
			String uri = userImageURI.startsWith("http") ? userImageURI : IMAGE_FOLDER + userImageURI;
			File oldImage = new File(uri);
			oldImage.delete();
		}
	}
	
	@GetMapping("/goingTo/{num}/{eventId}")
	private ResponseEntity<List<UserDTO>> getAllGoingToEvent(@PathVariable int num, @PathVariable Long eventId){
		return ResponseEntity.ok(userService.getAllGoingToEvent(num, eventId));
	}
	
	@GetMapping("/interestedIn/{num}/{eventId}")
	private ResponseEntity<List<UserDTO>> getAllInterestedInEvent(@PathVariable int num, @PathVariable Long eventId){
		return ResponseEntity.ok(userService.getAllInterestedInEvent(num, eventId));
	}

}
