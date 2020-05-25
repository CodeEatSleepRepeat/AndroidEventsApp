package rs.ac.uns.ftn.eventsbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.eventsbackend.enums.FriendshipStatus;
import rs.ac.uns.ftn.eventsbackend.model.Friendship;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Page<Friendship> findAllByRequestReceiverIdAndStatus(Long userId, FriendshipStatus status, Pageable pageable);

    Optional<Friendship> findByRequestSenderIdAndRequestReceiverId(Long senderId, Long receiverId);
}
