package rs.ac.uns.ftn.eventsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.eventsbackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	/**
	 * User that has this facebookId is returned from DB
	 * 
	 * @param fbId
	 * @return
	 */
	@Query(value = "SELECT u FROM User u WHERE u.facebookId = ?1")
	User findByFacebookId(String facebookId);

	public boolean existsByEmail(String email);

}
