package rs.ac.uns.ftn.eventsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.eventsbackend.model.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

}
