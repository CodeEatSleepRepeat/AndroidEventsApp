package rs.ac.uns.ftn.eventsbackend.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rs.ac.uns.ftn.eventsbackend.dto.CreateEventDTO;
import rs.ac.uns.ftn.eventsbackend.dto.EventDTO;
import rs.ac.uns.ftn.eventsbackend.dto.EventsSyncDTO;
import rs.ac.uns.ftn.eventsbackend.dto.GIEventsSyncDTO;
import rs.ac.uns.ftn.eventsbackend.dto.GoingInterestedEventsDTO;
import rs.ac.uns.ftn.eventsbackend.dto.RequestEventDetailsDTO;
import rs.ac.uns.ftn.eventsbackend.dto.ResponseEventDetailsDTO;
import rs.ac.uns.ftn.eventsbackend.dto.SearchFilterEventsDTO;
import rs.ac.uns.ftn.eventsbackend.dto.StringDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UpdateEventDTO;
import rs.ac.uns.ftn.eventsbackend.enums.GoingInterestedStatus;
import rs.ac.uns.ftn.eventsbackend.enums.SyncStatus;
import rs.ac.uns.ftn.eventsbackend.model.Cover;
import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.service.EventService;
import rs.ac.uns.ftn.eventsbackend.service.FacebookService;
import rs.ac.uns.ftn.eventsbackend.service.UserService;

@CrossOrigin
@RequestMapping("/event")
@RestController
public class EventController {

	@Value("${eventImages}")
	private String IMAGE_FOLDER;

	private final List<String> MIME_IMAGE_TYPES = new ArrayList<>(Arrays.asList("image/bmp", "image/gif", "image/jpeg",
			"image/png", "image/x-icon", "image/vnd.microsoft.icon"));

	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;

	@Autowired
	private FacebookService facebookService;

	/**
	 * Upload event cover image to server for storage
	 * 
	 * @param image
	 * @return
	 */
	@RequestMapping(value = "/upload/{id}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EventDTO> uploadImage(@PathVariable Long id,
			@RequestPart(name = "image") MultipartFile image) {

		if (image != null && !image.isEmpty()) {
			if (!MIME_IMAGE_TYPES.contains(image.getContentType())) {
				// sent file is not valid type
				return new ResponseEntity<>(null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
			}
			try {
				// image is good size
				String newImageName = System.currentTimeMillis() + "";
				String newFileUri = new File(IMAGE_FOLDER + newImageName).getAbsolutePath();

				// save image to folder
				image.transferTo(new File(newFileUri));
				Cover c = new Cover();
				c.setSource(newImageName);
				Event e = eventService.findById(id);
				if (e.getCover() != null) {
					removeImage(e.getCover().getSource());
				}
				e.setCover(c);
				eventService.update(e);
				return new ResponseEntity<>(new EventDTO(e), HttpStatus.CREATED);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			// image is empty
			return new ResponseEntity<>(null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}

		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Delete old event image when updating event details (not from REST Call!) ->
	 * this goes to update and delete Event
	 * 
	 * @param userImageURI
	 */
	private void removeImage(@PathVariable String userImageURI) {
		if (!userImageURI.equals("")) {
			String uri = userImageURI.startsWith("http") ? userImageURI : IMAGE_FOLDER + userImageURI;
			File oldImage = new File(uri);
			oldImage.delete();
		}
	}

	@PostMapping("/page/{num}")
	public ResponseEntity<List<EventDTO>> eventsPageable(@PathVariable int num,
			@RequestBody SearchFilterEventsDTO searchFilterEventsDTO) {
		Pageable pageable = PageRequest.of(num, 10);
		List<Event> events = eventService.getAllPageable(pageable);
		List<EventDTO> dtos = new ArrayList<>();
		for (Event event : events) {
			EventDTO dto = new EventDTO(event);
			dtos.add(dto);
		}
		return ResponseEntity.ok(dtos);
	}

	@PostMapping("/myevents/{id}/{num}")
	public ResponseEntity<List<EventDTO>> myEventsPageable(@PathVariable Long id, @PathVariable int num, @RequestBody SearchFilterEventsDTO searchFilterEventsDTO) {			
		Pageable pageable = PageRequest.of(num, 10);
		List<Event> events = new ArrayList<Event>();
		try {
			User dbUser = userService.findById(id);
			// povlacenje liste eventova sa fb i azuriranje iste
			if (dbUser.getSyncFacebookEvents() && !dbUser.getFacebookToken().isEmpty()) {
				// dbUser.get
				facebookService.pullEvents(dbUser.getFacebookToken(), dbUser);
			}
			
			events = eventService.getMyEvents(pageable, id);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		List<EventDTO> dtos = new ArrayList<>();
		for (Event event : events) {
			EventDTO dto = new EventDTO(event);
			dtos.add(dto);
		}
		return ResponseEntity.ok(dtos);
	}

	@PostMapping("/goingevents/{id}/{num}")
	public ResponseEntity<List<EventDTO>> goingEventsPageable(@PathVariable Long id, @PathVariable int num,
			@RequestBody SearchFilterEventsDTO searchFilterEventsDTO) {
		System.out.println("stigao");
		Pageable pageable = PageRequest.of(num, 10);
		List<Event> events = eventService.getGoingEvents(pageable, id);
		List<EventDTO> dtos = new ArrayList<>();
		for (Event event : events) {
			EventDTO dto = new EventDTO(event);
			dtos.add(dto);
		}
		return ResponseEntity.ok(dtos);
	}

	@PostMapping("/interestedevents/{id}/{num}")
	public ResponseEntity<List<EventDTO>> interestedEventsPageable(@PathVariable Long id, @PathVariable int num,
			@RequestBody SearchFilterEventsDTO searchFilterEventsDTO) {
		Pageable pageable = PageRequest.of(num, 10);
		List<Event> events = eventService.getInterestedEvents(pageable, id);
		List<EventDTO> dtos = new ArrayList<>();
		for (Event event : events) {
			EventDTO dto = new EventDTO(event);
			dtos.add(dto);
		}
		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/image/{name}")
	public ResponseEntity<byte[]> getImage(@PathVariable String name) {
		File f = new File(IMAGE_FOLDER + name);
		FileInputStream fis;
		try {
			fis = new FileInputStream(f);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				baos.write(buf, 0, readNum);
			}
			byte[] bytes = baos.toByteArray();
			fis.close();
			baos.close();
			return ResponseEntity.ok(bytes);
		} catch (Exception e) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<Event> getById(@PathVariable Long id) {
		return ResponseEntity.ok(eventService.findById(id));
	}

	@GetMapping("/going/{eventId}/{userId}")
	public ResponseEntity<EventDTO> goingToEvent(@PathVariable Long eventId, @PathVariable Long userId)
			throws Exception {
		Event e = eventService.addToGoing(eventId, userId);
		return ResponseEntity.ok(new EventDTO(e));
	}

	@GetMapping("/interested/{eventId}/{userId}")
	public ResponseEntity<EventDTO> interestedInEvent(@PathVariable Long eventId, @PathVariable Long userId)
			throws Exception {
		Event e = eventService.addToInterested(eventId, userId);
		return ResponseEntity.ok(new EventDTO(e));
	}

	@GetMapping("/remove/interested/{eventId}/{userId}")
	public ResponseEntity<EventDTO> removeInterestedEvent(@PathVariable Long eventId, @PathVariable Long userId)
			throws Exception {
		Event e = eventService.removeFromInterested(eventId, userId);
		return ResponseEntity.ok(new EventDTO(e));
	}

	@GetMapping("/remove/going/{eventId}/{userId}")
	public ResponseEntity<EventDTO> removeGoingEvent(@PathVariable Long eventId, @PathVariable Long userId)
			throws Exception {
		Event e = eventService.removeFromGoing(eventId, userId);
		return ResponseEntity.ok(new EventDTO(e));
	}

	@PostMapping
	public ResponseEntity<EventDTO> create(@RequestBody CreateEventDTO dto) throws Exception {
		System.out.println("create");
		User user = userService.findById(dto.getOwner());
		Event e = new Event(dto);
		if (user != null) {
			e.setOwner(user);
			return ResponseEntity.ok(new EventDTO(eventService.save(e)));
		}
		throw new Exception("User not found!");
	}

	@DeleteMapping("/{userId}/{eventId}")
	public ResponseEntity<EventDTO> delete(@PathVariable Long userId, @PathVariable Long eventId) throws Exception {
		System.out.println("delete");
		Event e = eventService.delete(userId, eventId);
		return ResponseEntity.ok(new EventDTO(e));
	}

	@PutMapping("/{userId}")
	public ResponseEntity<EventDTO> update(@PathVariable Long userId, @RequestBody UpdateEventDTO dto)
			throws Exception {
		System.out.println("update");
		Event e = eventService.update(userId, dto);
		return ResponseEntity.ok(new EventDTO(e));
	}

	/**
	 * Check if user events on android are same as in server DB and sync them if
	 * they are not
	 * 
	 * @param user
	 * @return updated user
	 */
	@RequestMapping(value = "/sync/myevents", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<List<EventDTO>> syncUserEvents(@RequestBody @Valid EventsSyncDTO data) {
		System.out.println("sync myevents " + data.getEmail() + " (last updated time: " + data.getLastSyncTime() + " )");

		User dbUser = userService.findByCredentials(data.getEmail(), data.getPassword());
		if (dbUser == null) {
			System.out.println("sync user events user not found");
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		// povlacenje liste eventova sa fb i azuriranje iste
		if (dbUser.getSyncFacebookEvents() && !dbUser.getFacebookToken().isEmpty()) {
			// dbUser.get
			facebookService.pullEvents(dbUser.getFacebookToken(), dbUser);
		}

		// no data sent from android for update?
		if (data.getEventsForUpdate() == null || data.getEventsForUpdate().isEmpty()) {
			// just return events that have update_time > last_update_time
			List<Event> forUpdate = eventService.getMyEventsForSync(dbUser.getId(), data.getLastSyncTime());
			ArrayList<EventDTO> retList = new ArrayList<>();
			for (Event event : forUpdate) {
				System.out.println(event.getUpdated_time());
				System.out.println(data.getLastSyncTime());
				retList.add(new EventDTO(event));
			}
			if (retList.isEmpty()) {
				System.out.println("sync myevents no data for server since last update time - no data for android");
			} else {
				System.out.println("sync myevents no data for server since last update time - sending changed user events");
			}
			
			return new ResponseEntity<>(retList, HttpStatus.OK);
		}

		// iterate over sent updates and update db if android timestamp > db tmestamp
		for (UpdateEventDTO androidEvent : data.getEventsForUpdate()) {
			Event dbEvent = eventService.findById(androidEvent.getId());
			if (dbEvent != null && dbEvent.getUpdated_time().isBefore(androidEvent.getUpdated_time())) {
				dbEvent.update(androidEvent);
				if (eventService.update(dbEvent).getSyncStatus() == SyncStatus.DELETE) {
					// remove cover from server if event is deleted
					if (dbEvent.getCover() != null) {
						removeImage(dbEvent.getCover().getSource());
					}
				}
			}
		}

		// get all updates
		List<Event> forUpdate = eventService.getMyEventsForSync(dbUser.getId(), data.getLastSyncTime());
		ArrayList<EventDTO> retList = new ArrayList<>();
		for (Event event : forUpdate) {
			retList.add(new EventDTO(event));
		}
		System.out.println("sync myevents last update time to long ago - sending user events (mabye empty?)");
		return new ResponseEntity<>(retList, HttpStatus.OK);
	}

	/**
	 * Check if going or interested events on android are same as in server DB and
	 * sync them if they are not
	 * 
	 * @param user
	 * @return updated user
	 */
	@RequestMapping(value = "/sync/gi-events", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<List<GoingInterestedEventsDTO>> syncGoingInterestedEvents(@RequestBody @Valid GIEventsSyncDTO data) {
		System.out.println("sync going/interested " + data.getEmail());

		User dbUser = userService.findByCredentials(data.getEmail(), data.getPassword());
		if (dbUser == null) {
			System.out.println("sync going/interested events user not found");
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		// data sent from android for update?
		if (data.getEventsForUpdate() != null && !data.getEventsForUpdate().isEmpty()) {

			// iterate over sent updates and update db
			for (GoingInterestedEventsDTO androidEvent : data.getEventsForUpdate()) {
				switch (androidEvent.getStatus()) {
				case GOING:
					try {
						removeInterestedEvent(androidEvent.getEvent().getId(), dbUser.getId());
					} catch (Exception e) {
					}
					try {
						goingToEvent(androidEvent.getEvent().getId(), dbUser.getId());
					} catch (Exception e) {
					}
					break;
				case INTERESTED:
					try {
						removeGoingEvent(androidEvent.getEvent().getId(), dbUser.getId());
					} catch (Exception e) {
					}
					try {
						interestedInEvent(androidEvent.getEvent().getId(), dbUser.getId());
					} catch (Exception e) {
					}
					break;
				default:
					try {
						removeGoingEvent(androidEvent.getEvent().getId(), dbUser.getId());
					} catch (Exception e) {
					}
					try {
						removeInterestedEvent(androidEvent.getEvent().getId(), dbUser.getId());
					} catch (Exception e) {
					}
				}
			}
		}
		
		// get all going and interested events
		List<Event> forUpdate = dbUser.getGoingEvents();
		ArrayList<GoingInterestedEventsDTO> retList = new ArrayList<>();
		for (Event event : forUpdate) {
			retList.add(new GoingInterestedEventsDTO(new EventDTO(event), GoingInterestedStatus.GOING));
		}
		forUpdate = dbUser.getInterestedEvents();
		for (Event event : forUpdate) {
			retList.add(new GoingInterestedEventsDTO(new EventDTO(event), GoingInterestedStatus.INTERESTED));
		}

		return new ResponseEntity<>(retList, HttpStatus.OK);
	}

	@GetMapping("/test/{id}")
	public ResponseEntity<StringDTO> getImageForUpdate(@PathVariable Long id) {
		Event e = eventService.findById(id);
		try {
			File f = new File(IMAGE_FOLDER + e.getCover().getSource());

			byte[] b = new byte[(int) f.length()];
			FileInputStream fis = new FileInputStream(f);
			fis.read(b);
			String s = new String(Base64.getEncoder().encodeToString(b));
			fis.close();
			return ResponseEntity.ok(new StringDTO(s));
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
	
	@PostMapping("/details")
	public ResponseEntity<ResponseEventDetailsDTO> getDetails(@RequestBody RequestEventDetailsDTO dto){
		ResponseEventDetailsDTO returnDTO = eventService.getDetails(dto);
		if(returnDTO!=null) {
			return ResponseEntity.ok(returnDTO);
		}
		return new ResponseEntity<ResponseEventDetailsDTO>(HttpStatus.NO_CONTENT);	
	}
	
	@GetMapping("/similar/{num}/{eventId}")
	public ResponseEntity<List<EventDTO>> similarEvents(@PathVariable int num, @PathVariable Long eventId){
		return ResponseEntity.ok(eventService.getSimilarEvents(num, eventId));
	}

	@PostMapping("/test/test")
	public ResponseEntity<List<EventDTO>> testiranje(@RequestBody SearchFilterEventsDTO dto){
		return ResponseEntity.ok(eventService.testiranje(dto));
	}
	
	@PostMapping("/test/test/distance")
	public ResponseEntity<List<EventDTO>> testiranje2(@RequestBody SearchFilterEventsDTO dto){
		return ResponseEntity.ok(eventService.testiranje2(dto));
	}
}
