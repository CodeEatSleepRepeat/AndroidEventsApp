package rs.ac.uns.ftn.eventsbackend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.eventsbackend.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	Page<Comment> findAllByEventId(Long id, Pageable pageable);
	
}
