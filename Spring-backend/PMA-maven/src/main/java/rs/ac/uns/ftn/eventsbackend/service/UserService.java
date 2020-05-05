package rs.ac.uns.ftn.eventsbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

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
}
