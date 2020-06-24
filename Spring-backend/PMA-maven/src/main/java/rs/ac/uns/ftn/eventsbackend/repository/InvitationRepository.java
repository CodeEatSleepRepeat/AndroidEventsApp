package rs.ac.uns.ftn.eventsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.eventsbackend.model.Invitation;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<List<Invitation>> findByInvitationReceiverId(Long userId);

	Optional<List<Invitation>> findAllByInvitationReceiverIdAndEventId(Long id, Long id2);
}
