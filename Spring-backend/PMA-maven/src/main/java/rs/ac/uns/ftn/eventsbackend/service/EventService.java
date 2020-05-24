package rs.ac.uns.ftn.eventsbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.dto.EventDTO;
import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.repository.EventRepository;
import rs.ac.uns.ftn.eventsbackend.repository.UserRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	public List<Event> getMyEvents(Pageable pageable, Long id) throws Exception{
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			Page<Event> page = eventRepository.findByOwner(user.get(), pageable);
			return page.getContent();
		}
		throw new Exception("User with id " + id + " not found!");
	}
	
	public List<Event> getGoingEvents(Pageable pageable, Long id) throws Exception{
		Page<Event> page = eventRepository.findByGoingId(id, pageable);
		return page.getContent();
	}
	
	public List<Event> getInterestedEvents(Pageable pageable, Long id) throws Exception{
		Page<Event> page = eventRepository.findByInterestedId(id, pageable);
		return page.getContent();
	}
	
	public Event addToGoing(Long eventId, Long userId) throws Exception {
		Optional<Event> e = eventRepository.findById(eventId);
		Optional<User> u = userRepository.findById(userId);
		if(e.isPresent() && u.isPresent()) {
			List<Event> going = u.get().getGoingEvents();
			if(!going.contains(e.get())) {
				List<Event> interested = u.get().getInterestedEvents();
				if(interested.contains(e.get())) {
					interested.remove(e.get());
					u.get().setInterestedEvents(interested);
				}
				going.add(e.get());
				u.get().setGoingEvents(going);
				userRepository.save(u.get());
				return e.get();
			}
			throw new Exception("User is already going to this event!");
		}
		throw new Exception("User or event not found!");
	}

	public Event addToInterested(Long eventId, Long userId) throws Exception {
		Optional<Event> e = eventRepository.findById(eventId);
		Optional<User> u = userRepository.findById(userId);
		if(e.isPresent() && u.isPresent()) {
			List<Event> interested = u.get().getInterestedEvents();
			if(!interested.contains(e.get())) {
				List<Event> going = u.get().getGoingEvents();
				if(going.contains(e.get())) {
					going.remove(e.get());
					u.get().setGoingEvents(going);
				}
				interested.add(e.get());
				u.get().setInterestedEvents(interested);
				userRepository.save(u.get());
				return e.get();
			}
			throw new Exception("User is already interested in this event!");
		}
		throw new Exception("User or event not found!");
	}
	
	public Event removeFromInterested(Long eventId, Long userId) throws Exception {
		Optional<Event> e = eventRepository.findById(eventId);
		Optional<User> u = userRepository.findById(userId);
		if(e.isPresent() && u.isPresent()) {
			List<Event> interested = u.get().getInterestedEvents();
			if(interested.contains(e.get())){
				interested.remove(e.get());
				u.get().setInterestedEvents(interested);
				userRepository.save(u.get());
				return e.get();
			}
			throw new Exception("User does not have this event in his interested list!");
		}
		throw new Exception("User or event not found!");
	}
	
	public Event removeFromGoing(Long eventId, Long userId) throws Exception {
		Optional<Event> e = eventRepository.findById(eventId);
		Optional<User> u = userRepository.findById(userId);
		if(e.isPresent() && u.isPresent()) {
			List<Event> going = u.get().getGoingEvents();
			if(going.contains(e.get())){
				going.remove(e.get());
				u.get().setGoingEvents(going);
				userRepository.save(u.get());
				return e.get();
			}
			throw new Exception("User does not have this event in his going list!");
		}
		throw new Exception("User or event not found!");
	}
	
	public Event save(Event event) {
		event.setIsDeleted(false);
		return eventRepository.save(event);
	}
	
	public Event delete(Long userId, Long eventId) throws Exception {
		Optional<User> user = userRepository.findById(userId);
		Optional<Event> event = eventRepository.findById(eventId);
		if(user.isPresent() && event.isPresent() && event.get().getOwner().equals(user.get())) {
			event.get().setIsDeleted(true);
			eventRepository.save(event.get());
			return event.get();
		}
		throw new Exception("Event does not exist!");
	}
	
}
