package rs.ac.uns.ftn.eventsbackend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.eventsbackend.dto.EventDistanceDTO;
import rs.ac.uns.ftn.eventsbackend.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	/**
	 * @author Boris
	 * 
	 *         <p>
	 *         The method returns events open to all within a radius of dist, with a
	 *         center in coordinates (lat, lng). The call may be restricted to
	 *         returning a specified number of instances as follows:
	 *         <p>
	 *         <code>Pageable topTen = new PageRequest(0, 10);</code> <br>
	 *         <code>List<EventDistanceDTO> result = eventRepository.getEventsNearCoordinates(24.9984, 13.4541, 5, topTen);</code>
	 *         </p>
	 *         </p>
	 * 
	 * @param lat      - Latitude of center
	 * @param lng      - Longitude of center
	 * @param dist     - Distance from center of coordinates
	 * @param pageable - Pagination information
	 * @return List of {@link EventDistanceDTO}
	 * 
	 */
	@Query(value = "SELECT new rs.ac.uns.ftn.eventsbackend.dto.EventDistanceDTO(e, (6371 * acos (cos(radians(lat))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(lng))+sin(radians(lat))*sin(radians(e.latitude)))) AS distance) FROM Event e WHERE distance < dist ORDER BY distance")
	Page<EventDistanceDTO> getOpenEventsNearCoordinates(float lat, float lng, float dist, Pageable pageable);

	/**
	 * Event that has this facebookId is returned from DB
	 * 
	 * @param fbId
	 * @return
	 */
	@Query(value = "SELECT e FROM Event e WHERE e.facebookId = ?1")
	Event findByFacebookId(String facebookId);
	
	Page<Event> findAll(Pageable pageable);
}
