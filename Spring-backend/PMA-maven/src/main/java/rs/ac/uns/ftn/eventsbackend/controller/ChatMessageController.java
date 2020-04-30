package rs.ac.uns.ftn.eventsbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.eventsbackend.service.ChatMessageService;

@RequestMapping("/message")
@RestController
public class ChatMessageController {
	
	@Autowired
	private ChatMessageService messageService;
	

}
