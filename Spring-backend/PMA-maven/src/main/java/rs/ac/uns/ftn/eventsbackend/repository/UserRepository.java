package rs.ac.uns.ftn.eventsbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.eventsbackend.model.User;

import java.util.List;
import java.util.Optional;

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

	@Query(value = "SELECT u from User u where " +
			"u.id not in " +
			"(SELECT f.requestReceiver.id from Friendship f where f.requestSender.id = ?1 and f.status !=2)" +
			"and u.id not in" +
			"(SELECT f.requestSender.id from Friendship f where f.requestReceiver.id = ?1 and f.status !=2)" +
			"and u.id != ?1" +
			"and u.name LIKE CONCAT('%', ?2, '%')" +
			"and u.activatedAccount = true")
	Page<User> findByNameContaining(Long userId, String username, Pageable pageable);

	// Napomena: Hibernate ne podrzava UNION operator
    @Query(value = "SELECT u from User u where u.id in " +
			"(SELECT f.requestReceiver.id from Friendship f where f.requestSender.id = ?1 and f.status = 0 )" +
			"or u.id in" +
			"(SELECT f.requestSender.id from Friendship f where f.requestReceiver.id = ?1 and f.status = 0)")
	Optional<List<User>> findUsersFriends(Long userId);
    Page<User> findByGoingEventsId(Long eventId, Pageable pageable);
    Page<User> findByInterestedEventsId(Long eventId, Pageable pageable);

    @Query(value ="SELECT u from User u where " +
			"(u.id in (SELECT f.requestReceiver.id from Friendship f where f.requestSender.id = ?1 and status = 0)" +
			" or u.id in (SELECT f.requestSender.id from Friendship f where f.requestReceiver.id = ?1 and status = 0)) " +
			"and u.id != ?1 " +
			"and u.id not in (SELECT i.invitationReceiver.id from Invitation i where event.id = ?2)")
    Optional<List<User>> findFriendsForInvitation(Long senderId, Long eventId);
}
