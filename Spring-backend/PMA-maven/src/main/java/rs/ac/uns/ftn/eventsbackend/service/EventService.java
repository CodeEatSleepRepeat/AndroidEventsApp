package rs.ac.uns.ftn.eventsbackend.service;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.dto.EventDTO;
import rs.ac.uns.ftn.eventsbackend.dto.RequestEventDetailsDTO;
import rs.ac.uns.ftn.eventsbackend.dto.ResponseEventDetailsDTO;
import rs.ac.uns.ftn.eventsbackend.dto.SimilarEventDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UpdateEventDTO;
import rs.ac.uns.ftn.eventsbackend.enums.SyncStatus;
import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.repository.EventRepository;
import rs.ac.uns.ftn.eventsbackend.repository.UserRepository;

@Service
public class EventService {

	@Value("${eventImages}")
	private String IMAGE_FOLDER;

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

	public List<Event> getAllPageable(Pageable pageable) {
		Page<Event> page = eventRepository.findAllBySyncStatusNot(SyncStatus.DELETE, pageable);
		return page.getContent();
	}

	public List<Event> getMyEvents(Pageable pageable, Long id) throws Exception {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			Page<Event> page = eventRepository.findByOwnerAndSyncStatusNot(user.get(), SyncStatus.DELETE, pageable);
			return page.getContent();
		}
		throw new Exception("User with id " + id + " not found!");
	}
	
	public List<Event> getMyEventsForSync(Long id, ZonedDateTime timestamp) {
		return eventRepository.findAllByOwnerAndUpdatedTime(id, timestamp);
	}

	public List<Event> getGoingEvents(Pageable pageable, Long id) {
		Page<Event> page = eventRepository.findByGoingIdAndSyncStatusNot(id, SyncStatus.DELETE, pageable);
		return page.getContent();
	}

	public List<Event> getInterestedEvents(Pageable pageable, Long id) {
		Page<Event> page = eventRepository.findByInterestedIdAndSyncStatusNot(id, SyncStatus.DELETE, pageable);
		return page.getContent();
	}

	public Event addToGoing(Long eventId, Long userId) throws Exception {
		Optional<Event> e = eventRepository.findById(eventId);
		Optional<User> u = userRepository.findById(userId);
		if (e.isPresent() && u.isPresent()) {
			List<Event> going = u.get().getGoingEvents();
			if (!going.contains(e.get())) {
				List<Event> interested = u.get().getInterestedEvents();
				if (interested.contains(e.get())) {
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
		if (e.isPresent() && u.isPresent()) {
			List<Event> interested = u.get().getInterestedEvents();
			if (!interested.contains(e.get())) {
				List<Event> going = u.get().getGoingEvents();
				if (going.contains(e.get())) {
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
		if (e.isPresent() && u.isPresent()) {
			List<Event> interested = u.get().getInterestedEvents();
			if (interested.contains(e.get())) {
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
		if (e.isPresent() && u.isPresent()) {
			List<Event> going = u.get().getGoingEvents();
			if (going.contains(e.get())) {
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
		event.setSyncStatus(SyncStatus.ADD);
		event.setUpdated_time(ZonedDateTime.now());
		/*Calendar cal = Calendar.getInstance();
		cal.setTime(event.getStart_time());
		cal.add(Calendar.HOUR_OF_DAY, -2);
		event.setStart_time(cal.getTime());
		cal.setTime(event.getEnd_time());
		cal.add(Calendar.HOUR_OF_DAY, -2);
		event.setEnd_time(cal.getTime());*/
		return eventRepository.save(event);
	}

	public Event delete(Long userId, Long eventId) throws Exception {
		Optional<User> user = userRepository.findById(userId);
		Optional<Event> event = eventRepository.findById(eventId);
		if (user.isPresent() && event.isPresent() && event.get().getOwner().equals(user.get())) {
			event.get().setSyncStatus(SyncStatus.DELETE);
			event.get().setUpdated_time(ZonedDateTime.now());
			eventRepository.save(event.get());
			if (event.get().getCover() != null) {
				removeImage(event.get().getCover().getSource());
			}
			return event.get();
		}
		throw new Exception("Event does not exist!");
	}

	private void removeImage(String imageName) {
		if (!imageName.equals("")) {
			File oldImage = new File(IMAGE_FOLDER + imageName);
			oldImage.delete();
		}
	}

	public Event update(Long userId, UpdateEventDTO dto) throws Exception {
		Optional<User> user = userRepository.findById(userId);
		Optional<Event> event = eventRepository.findById(dto.getId());
		if (user.isPresent() && event.isPresent() && event.get().getOwner().equals(user.get())) {
			/*Calendar cal = Calendar.getInstance();
			cal.setTime(dto.getStart_time());
			cal.add(Calendar.HOUR_OF_DAY, -2);
			event.get().setStart_time(cal.getTime());
			cal.setTime(dto.getEnd_time());
			cal.add(Calendar.HOUR_OF_DAY, -2);
			event.get().setEnd_time(cal.getTime());*/
			event.get().setStart_time(dto.getStart_time());
			event.get().setEnd_time(dto.getEnd_time());
			event.get().setLatitude(dto.getLatitude());
			event.get().setLongitude(dto.getLongitude());
			event.get().setPlace(dto.getPlace());
			event.get().setName(dto.getName());
			event.get().setDescription(dto.getDescription());
			event.get().setType(dto.getType());
			event.get().setPrivacy(dto.getPrivacy());
			event.get().setSyncStatus(SyncStatus.UPDATE);
			event.get().setUpdated_time(ZonedDateTime.now());
			return eventRepository.save(event.get());
		}
		throw new Exception("Event does not exist!");
	}

	public Event update(Event event) {
		event.setSyncStatus(SyncStatus.UPDATE);
		event.setUpdated_time(ZonedDateTime.now());/*
		Calendar cal = Calendar.getInstance();
		cal.setTime(event.getStart_time());
		cal.add(Calendar.HOUR_OF_DAY, -2);
		event.setStart_time(cal.getTime());
		cal.setTime(event.getEnd_time());
		cal.add(Calendar.HOUR_OF_DAY, -2);
		event.setEnd_time(cal.getTime());*/
		return eventRepository.save(event);
	}
	/*
	public Event updateNoTime(Event event) {
		return eventRepository.save(event);
	}*/
	
	public ResponseEventDetailsDTO getDetails(RequestEventDetailsDTO dto) {
		Optional<Event> event = eventRepository.findById(dto.getEventId());
		ResponseEventDetailsDTO resDTO = new ResponseEventDetailsDTO();
		if(event.isPresent()) {	
			resDTO.setUserImage(event.get().getOwner().getImageUri());
			resDTO.setUserName(event.get().getOwner().getName());
			resDTO.setOrganizedEventsNum(event.get().getOwner().getUserEvents().size());

			List<String> goingImages = new ArrayList<>();
			List<String> interestedImages = new ArrayList<>();
			List<SimilarEventDTO> events = new ArrayList<>();
			for (User u : event.get().getGoing()) {
				goingImages.add(u.getImageUri());
				if(goingImages.size()==8) {
					break;
				}
			}
			for (User u : event.get().getInterested()) {
				interestedImages.add(u.getImageUri());
				if(goingImages.size()==8) {
					break;
				}
			}
			for (Event e : eventRepository.findByType(event.get().getType())) {
				if(!e.getId().equals(event.get().getId())) {
					if(e.getCover()!=null) {
						events.add(new SimilarEventDTO(e.getName(), e.getCover().getSource()));
					}else {
						events.add(new SimilarEventDTO(e.getName(), null));
					}
					if(events.size()==5) {
						break;
					}
				}
			}
			resDTO.setGoingImages(goingImages);
			resDTO.setInterestedImages(interestedImages);
			resDTO.setEvents(events);
			
			return resDTO;
		}
		return null;
	}

	public List<EventDTO> getSimilarEvents(int num, Long eventId) {
		Optional<Event> e = eventRepository.findById(eventId);
		if(e.isPresent()) {
			List<EventDTO> dtos = new ArrayList<>(); 
			Pageable pageable = PageRequest.of(num, 10);
			List<Event> events = eventRepository.findByTypeAndSyncStatusNot(e.get().getType(), SyncStatus.DELETE, pageable).getContent();
			for (Event event : events) {
				if(!event.getId().equals(eventId)) {
					dtos.add(new EventDTO(event));
				}
			}
			return dtos;
		}
		return new ArrayList<>();
	}

}
