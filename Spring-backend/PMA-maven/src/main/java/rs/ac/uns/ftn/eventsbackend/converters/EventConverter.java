package rs.ac.uns.ftn.eventsbackend.converters;

import java.time.ZonedDateTime;

import rs.ac.uns.ftn.eventsbackend.enums.EventType;
import rs.ac.uns.ftn.eventsbackend.enums.SyncStatus;
import rs.ac.uns.ftn.eventsbackend.gson.getUserEvents.CustomFacebookEventData;
import rs.ac.uns.ftn.eventsbackend.model.Cover;
import rs.ac.uns.ftn.eventsbackend.model.Event;

public class EventConverter {

	/**
	 * !!!!!!!!!!!!!!!!!!!!! OVDE SE NE SETUJE OWNER - ON IDE POSLE ZBOG BAZE !!!!!!!!!!!!!!!
	 * @param fbEvent
	 * @return
	 */
	public static Event convert(CustomFacebookEventData fbEvent) {
		
		Event dbEvent = new Event();
		
		dbEvent.setFacebookId(fbEvent.getId());
		dbEvent.setUpdated_timeFB(fbEvent.getUpdated_time());
		dbEvent.setAttending_countFB(fbEvent.getAttending_count());
		dbEvent.setDeclined_count(fbEvent.getDeclined_count());
		dbEvent.setCan_guests_invite(fbEvent.getCan_guests_invite());
		dbEvent.setMaybe_count(fbEvent.getMaybe_count());
		dbEvent.setIs_canceled(fbEvent.getIs_canceled());
		dbEvent.setTimezone(fbEvent.getTimezone());
		dbEvent.setInterested_count(fbEvent.getInterested_count());
		dbEvent.setIs_online(fbEvent.getIs_online());
		dbEvent.setPlace(fbEvent.getPlace().getName());
		dbEvent.setEnd_time(fbEvent.getEnd_time());
		dbEvent.setStart_time(fbEvent.getStart_time());
		dbEvent.setPrivacy(fbEvent.getPrivacy());
		dbEvent.setDescription(fbEvent.getDescription());
		dbEvent.setName(fbEvent.getName());
		dbEvent.setCover(new Cover());
		dbEvent.getCover().setFbId(fbEvent.getCover().getId());
		dbEvent.getCover().setOffset_x(fbEvent.getCover().getOffset_x());
		dbEvent.getCover().setOffset_y(fbEvent.getCover().getOffset_y());
		dbEvent.getCover().setSource(fbEvent.getCover().getSource());
		dbEvent.setSyncStatus(SyncStatus.ADD);
		dbEvent.setUpdated_time(ZonedDateTime.now());
		
		dbEvent.setType(EventType.FACEBOOK);
		
		return dbEvent;
	}
	
}
