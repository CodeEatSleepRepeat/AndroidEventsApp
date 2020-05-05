package rs.ac.uns.ftn.eventsbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.repository.ChatMessageRepository;

@Service
public class ChatMessageService {

	@Autowired
	private ChatMessageRepository chatMessageRepository;

}
