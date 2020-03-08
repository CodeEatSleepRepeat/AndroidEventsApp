package rs.ac.uns.ftn.eventsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.eventsbackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
