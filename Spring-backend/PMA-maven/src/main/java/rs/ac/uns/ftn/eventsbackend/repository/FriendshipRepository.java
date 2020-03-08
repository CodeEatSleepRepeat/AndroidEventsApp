package rs.ac.uns.ftn.eventsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.eventsbackend.model.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long>{

}
