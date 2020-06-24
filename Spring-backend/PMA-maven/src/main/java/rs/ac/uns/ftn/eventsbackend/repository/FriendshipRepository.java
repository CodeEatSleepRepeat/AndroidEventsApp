package rs.ac.uns.ftn.eventsbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.eventsbackend.enums.FriendshipStatus;
import rs.ac.uns.ftn.eventsbackend.model.Friendship;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Page<Friendship> findAllByRequestReceiverIdAndStatus(Long userId, FriendshipStatus status, Pageable pageable);

    Optional<Friendship> findByRequestSenderIdAndRequestReceiverId(Long senderId, Long receiverId);

    @Query("SELECT count(f) from Friendship f where f.requestReceiver.id = ?1 and f.status = 2")
    Optional<Integer> getNumberOfUserFriendRequests(Long userId);

    @Query("SELECT f from Friendship f where (f.requestReceiver.id = ?1 and f.requestSender.id = ?2 and f.status = ?3) " +
            "or (f.requestReceiver.id = ?2 and f.requestSender.id = ?1 and f.status = ?3)")
    Optional<Friendship> findFriendshipOfTwoUsers(Long user1, Long user2, FriendshipStatus status);
}
