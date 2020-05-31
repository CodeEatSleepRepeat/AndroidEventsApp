package rs.ac.uns.ftn.eventsbackend.controller;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.eventsbackend.dto.CommentDTO;
import rs.ac.uns.ftn.eventsbackend.dto.CreateCommentDTO;
import rs.ac.uns.ftn.eventsbackend.model.Comment;
import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.service.CommentService;
import rs.ac.uns.ftn.eventsbackend.service.EventService;
import rs.ac.uns.ftn.eventsbackend.service.UserService;

@CrossOrigin
@RequestMapping("/comment")
@RestController
public class CommentController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/{eventId}/{num}")
	public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long eventId, @PathVariable int num){
		Pageable pageable = PageRequest.of(num, 10);
		List<Comment> comments = commentService.findAllPageable(eventId, pageable);
		List<CommentDTO> dtos = new ArrayList<>();
		for (Comment comment : comments) {
			dtos.add(new CommentDTO(comment.getText(), comment.getUser().getName(), comment.getUser().getImageUri()));
		}
		return ResponseEntity.ok(dtos);
	}
	
	@PostMapping
	public ResponseEntity<CommentDTO> create(@RequestBody CreateCommentDTO dto) throws Exception{
		
		Event event = eventService.findById(dto.getEventId());
		User user = userService.findById(dto.getUserId());
		if(event!=null && user!=null) {
			Comment comment = new Comment();
			comment.setText(dto.getText());
			comment.setEvent(event);
			comment.setUser(user);
			comment.setTimestamp(ZonedDateTime.now());
			commentService.create(comment);
			return ResponseEntity.ok(new CommentDTO(dto.getText(), user.getName(), user.getImageUri()));
		}
		throw new Exception("User or event not found.");
		
	}

}
