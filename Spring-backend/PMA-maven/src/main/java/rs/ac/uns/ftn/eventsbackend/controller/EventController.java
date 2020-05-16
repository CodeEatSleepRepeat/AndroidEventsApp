package rs.ac.uns.ftn.eventsbackend.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rs.ac.uns.ftn.eventsbackend.dto.CreateEventDTO;
import rs.ac.uns.ftn.eventsbackend.dto.EventDTO;
import rs.ac.uns.ftn.eventsbackend.model.Cover;
import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.service.EventService;

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

	/**
	 * Upload event cover image to server for storage
	 * 
	 * @param image
	 * @return
	 */
	@RequestMapping(value = "/upload/{id}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EventDTO> uploadImage(@PathVariable Long id, @RequestPart(name = "image") MultipartFile image) {

		if (image != null && !image.isEmpty()) {
			if (!MIME_IMAGE_TYPES.contains(image.getContentType())) {
				// sent file is not valid type
				return new ResponseEntity<>(null, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
			}
			try {
				// image is good size
				String newImageName = System.currentTimeMillis()
						+ image.getOriginalFilename()/*.substring(image.getOriginalFilename().lastIndexOf("."))*/;
				String newFileUri = new File(IMAGE_FOLDER + newImageName).getAbsolutePath();

				// save image to folder
				image.transferTo(new File(newFileUri));
				Cover c = new Cover();
				c.setSource(newFileUri);
				Event e = eventService.findById(id);
				e.setCover(c);
				eventService.save(e);
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
			File oldImage = new File(IMAGE_FOLDER + userImageURI);
			oldImage.delete();
		}
	}
	
	@GetMapping
	public ResponseEntity<List<Event>> getAll(){
		return ResponseEntity.ok(eventService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Event> getById(@PathVariable Long id){
		return ResponseEntity.ok(eventService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<EventDTO> create(@RequestBody CreateEventDTO dto){
		System.out.println("create");
		Event e = eventService.save(new Event(dto));
		return ResponseEntity.ok(new EventDTO(e));
	}

}
