package rs.ac.uns.ftn.eventsbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.repository.EventRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;
	
	public Event findById(Long id) {
		try {
			return eventRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}
	}

	public Event findByFacebookId(String fbId) {
		try {
			return eventRepository.findByFacebookId(fbId);
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Event> getAllPageable(Pageable pageable){
		Page<Event> page = eventRepository.findAll(pageable);
		return page.getContent();
	}
	
	public List<Event> getMyEvents(Pageable pageable, Long id){
		return eventRepository.findAll();
	}

	public Event save(Event event) {
		return eventRepository.save(event);
	}
	
}
