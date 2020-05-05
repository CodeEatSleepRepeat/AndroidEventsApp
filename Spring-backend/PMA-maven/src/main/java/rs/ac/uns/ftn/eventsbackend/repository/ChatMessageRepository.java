package rs.ac.uns.ftn.eventsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.eventsbackend.model.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
