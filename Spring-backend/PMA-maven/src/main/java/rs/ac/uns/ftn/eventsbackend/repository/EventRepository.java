package rs.ac.uns.ftn.eventsbackend.repository;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.eventsbackend.dto.EventDistanceDTO;
import rs.ac.uns.ftn.eventsbackend.enums.EventType;
import rs.ac.uns.ftn.eventsbackend.enums.SortType;
import rs.ac.uns.ftn.eventsbackend.enums.SyncStatus;
import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.model.User;

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
	@Query(value = "SELECT new rs.ac.uns.ftn.eventsbackend.dto.EventDistanceDTO(e, (6371 * acos (cos(radians(?1))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?2))+sin(radians(?1))*sin(radians(e.latitude)))) AS distance) FROM Event e WHERE (6371 * acos (cos(radians(?1))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?2))+sin(radians(?1))*sin(radians(e.latitude)))) < ?3 ORDER BY distance")
	Page<EventDistanceDTO> getOpenEventsNearCoordinates(Double lat, Double lng, Double dist, Pageable pageable);

	/**
	 * Event that has this facebookId is returned from DB
	 * 
	 * @param fbId
	 * @return
	 */
	@Query(value = "SELECT e FROM Event e WHERE e.facebookId = ?1")
	Event findByFacebookId(String facebookId);
	
	Page<Event> findAllBySyncStatusNot(SyncStatus s, Pageable pageable);
	Page<Event> findByOwnerAndSyncStatusNot(User user, SyncStatus s, Pageable pageable);
	Page<Event> findByOwnerAndSyncStatusNotAndNameContainingIgnoreCase(User user, SyncStatus s, String name, Pageable pageable);
	Page<Event> findByGoingIdAndSyncStatusNot(Long id, SyncStatus s, Pageable pageable);
	Page<Event> findByGoingIdAndSyncStatusNotAndNameContainingIgnoreCase(Long id, SyncStatus s, String name, Pageable pageable);
	Page<Event> findByInterestedIdAndSyncStatusNot(Long id, SyncStatus s, Pageable pageable);
	Page<Event> findByInterestedIdAndSyncStatusNotAndNameContainingIgnoreCase(Long id, SyncStatus s, String name, Pageable pageable);
	List<Event> findByType(EventType et);
	
	Page<Event> findAllBySyncStatus(SyncStatus s, Pageable pageable);
	Page<Event> findByOwnerAndSyncStatus(User user, SyncStatus s, Pageable pageable);
	Page<Event> findByGoingIdAndSyncStatus(Long id, SyncStatus s, Pageable pageable);
	Page<Event> findByInterestedIdAndSyncStatus(Long id, SyncStatus s, Pageable pageable);
	Page<Event> findByTypeAndSyncStatusNot(EventType et, SyncStatus s, Pageable pageable);
	
	@Query(value =" SELECT new rs.ac.uns.ftn.eventsbackend.dto.EventDistanceDTO(e, (6371 * acos (cos(radians(?3))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?4))+sin(radians(?3))*sin(radians(e.latitude)))) AS distance)"
			+ " FROM Event e WHERE (6371 * acos (cos(radians(?3))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?4))+sin(radians(?3))*sin(radians(e.latitude)))) < ?2"
			+ " AND (?1 IS NULL OR e.name LIKE %?1%)"
			+ " AND (?5 IS NULL OR e.start_time>?5)"
			+ " AND (?6 IS NULL OR e.end_time<?6)"
			+ " AND (?7 IS NULL OR e.privacy = ?7)"
			+ " AND (e.type IN (?8, ?9, ?10, ?11, ?12, ?13))"
			+ " AND e.end_time>?14"
			+ " AND e.syncStatus != ?15"
			+ " ORDER BY distance")
	Page<EventDistanceDTO> getEventsSearchFilterForYou(String search, Double distance, Double lat, Double lng, ZonedDateTime eventStart, ZonedDateTime eventEnd, int facebookPrivacy,
			EventType charity, EventType educational, EventType talks, EventType sports, EventType music, EventType party, ZonedDateTime now, SyncStatus ss, Pageable pageable);
	
	@Query(value =" SELECT new rs.ac.uns.ftn.eventsbackend.dto.EventDistanceDTO(e, (6371 * acos (cos(radians(?3))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?4))+sin(radians(?3))*sin(radians(e.latitude)))) AS distance)"
			+ " FROM Event e WHERE (6371 * acos (cos(radians(?3))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?4))+sin(radians(?3))*sin(radians(e.latitude)))) < ?2"
			+ " AND (?1 IS NULL OR e.name LIKE %?1%)"
			+ " AND (?5 IS NULL OR e.start_time>?5)"
			+ " AND (?6 IS NULL OR e.end_time<?6)"
			+ " AND (?7 IS NULL OR e.privacy = ?7)"
			+ " AND (e.type IN (?8, ?9, ?10, ?11, ?12, ?13))"
			+ " AND e.end_time>?14"
			+ " AND e.syncStatus != ?15"
			+ " ORDER BY e.created_time DESC")
	Page<EventDistanceDTO> getEventsSearchFilterRecent(String search, Double distance, Double lat, Double lng, ZonedDateTime eventStart, ZonedDateTime eventEnd, int facebookPrivacy,
			EventType charity, EventType educational, EventType talks, EventType sports, EventType music, EventType party, ZonedDateTime now, SyncStatus ss, Pageable pageable);
	
	@Query(value =" SELECT new rs.ac.uns.ftn.eventsbackend.dto.EventDistanceDTO(e, (6371 * acos (cos(radians(?3))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?4))+sin(radians(?3))*sin(radians(e.latitude)))) AS distance)"
			+ " FROM Event e WHERE (6371 * acos (cos(radians(?3))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?4))+sin(radians(?3))*sin(radians(e.latitude)))) < ?2"
			+ " AND (?1 IS NULL OR e.name LIKE %?1%)"
			+ " AND (?5 IS NULL OR e.start_time>?5)"
			+ " AND (?6 IS NULL OR e.end_time<?6)"
			+ " AND (?7 IS NULL OR e.privacy = ?7)"
			+ " AND (e.type IN (?8, ?9, ?10, ?11, ?12, ?13))"
			+ " AND e.end_time>?14"
			+ " AND e.syncStatus != ?15"
			+ " ORDER BY e.start_time ASC")
	Page<EventDistanceDTO> getEventsSearchFilterSoonest(String search, Double distance, Double lat, Double lng, ZonedDateTime eventStart, ZonedDateTime eventEnd, int facebookPrivacy,
			EventType charity, EventType educational, EventType talks, EventType sports, EventType music, EventType party, ZonedDateTime now, SyncStatus ss, Pageable pageable);
	
/*	@Query(value =" SELECT new rs.ac.uns.ftn.eventsbackend.dto.EventDistanceDTO(e, (6371 * acos (cos(radians(?3))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?4))+sin(radians(?3))*sin(radians(e.latitude)))) AS distance)"
			+ " FROM Event e inner join"
			+ " (SELECT COUNT(g.user_id) c, g.event_id idd FROM android_db.going_events g group by g.event_id order by c DESC) t on e.id = t.idd"
			+ " WHERE (6371 * acos (cos(radians(?3))*cos(radians(e.latitude))*cos(radians(e.longitude)-radians(?4))+sin(radians(?3))*sin(radians(e.latitude)))) < ?2"
			+ " AND (?1 IS NULL OR e.name LIKE %?1%)"
			+ " AND (?5 IS NULL OR e.start_time>?5)"
			+ " AND (?6 IS NULL OR e.end_time<?6)"
			+ " AND (?7 IS NULL OR e.privacy = ?7)"
			+ " AND (e.type IN (?8, ?9, ?10, ?11, ?12, ?13))"
			+ " AND e.end_time<?14"
			)
	List<EventDistanceDTO> testiranje4(String search, Double distance, Double lat, Double lng, ZonedDateTime eventStart, ZonedDateTime eventEnd, int facebookPrivacy,
			EventType charity, EventType educational, EventType talks, EventType sports, EventType music, EventType party, ZonedDateTime now);*/
	
	@Query(value = "SELECT e FROM Event e WHERE e.owner.id = ?1 AND e.updated_time > ?2")
	List<Event> findAllByOwnerAndUpdatedTime(Long id, ZonedDateTime updated_time);
}
