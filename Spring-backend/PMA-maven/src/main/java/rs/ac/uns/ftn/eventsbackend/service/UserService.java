package rs.ac.uns.ftn.eventsbackend.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.enums.FriendshipStatus;
import rs.ac.uns.ftn.eventsbackend.model.Friendship;
import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.repository.FriendshipRepository;
import rs.ac.uns.ftn.eventsbackend.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FriendshipRepository friendshipRepository;


	public User findByCredentials(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return null;
		}
		if (user.getPassword() != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}

	public User findById(Long id) {
		try {
			return userRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public User findByFacebookId(String fbId) {
		try {
			return userRepository.findByFacebookId(fbId);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void delete(Long userId) {
		userRepository.deleteById(userId);
	}

    public byte[] getImage(String imageFolder, String name) throws IOException {
		File f = new File(imageFolder + name);
		FileInputStream fis = new FileInputStream(f);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		for(int readNum; (readNum = fis.read(buf))!=-1;) {
			baos.write(buf, 0, readNum);
		}
		byte[] bytes = baos.toByteArray();
		fis.close();
		baos.close();
		return bytes;
    }

	public User getById(Long userId) throws Exception {
		Optional<User> foundUserOptional;

		foundUserOptional = userRepository.findById(userId);
		if(!foundUserOptional.isPresent())
			throw new Exception("User not found with ID:" + userId);

		return foundUserOptional.get();
	}

	public List<User> findAllFriendsOfUser(Long userId) {
		Optional<User> foundUser = userRepository.findById(userId);
		List<User> usersFriends = new ArrayList<User>();
		if(foundUser.get() == null){
			usersFriends = Collections.emptyList();
		}
		else {
			for(Friendship friendship : foundUser.get().getSendRequests()){
				if(friendship.getStatus().equals(FriendshipStatus.ACCEPTED))
					usersFriends.add(friendship.getRequestReceiver());
			}
			for(Friendship friendship : foundUser.get().getReceivedRequests()){
				if(friendship.getStatus().equals(FriendshipStatus.ACCEPTED))
					usersFriends.add(friendship.getRequestSender());
			}
		}

		return usersFriends;
	}

	public List<User> findAllWhichContainsUsernamePageable(String username, Pageable pageable) {
		return userRepository.findByNameContaining(username, pageable).getContent();
	}
}
