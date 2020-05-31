package rs.ac.uns.ftn.eventsbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.model.Comment;
import rs.ac.uns.ftn.eventsbackend.repository.CommentRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	public Comment create(Comment comment) {
		return commentRepository.save(comment);
	}
	
	public List<Comment> findAllPageable(Long eventId, Pageable pageable){
		return commentRepository.findAllByEventId(eventId, pageable).getContent();
	}
}
