package rs.ac.uns.ftn.eventsbackend.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import lombok.var;
import rs.ac.uns.ftn.eventsbackend.comparators.EventComparator;
import rs.ac.uns.ftn.eventsbackend.comparators.UserComparator;
import rs.ac.uns.ftn.eventsbackend.converters.EventConverter;
import rs.ac.uns.ftn.eventsbackend.gson.getFbUserProfile.CustomFacebookProfile;
import rs.ac.uns.ftn.eventsbackend.gson.getUserEvents.CustomFacebookEventData;
import rs.ac.uns.ftn.eventsbackend.gson.getUserEvents.CustomFacebookEvents;
import rs.ac.uns.ftn.eventsbackend.model.Cover;
import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.model.Owner;
import rs.ac.uns.ftn.eventsbackend.model.User;

@Service
public class FacebookService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	/******************************   ___           ___         _____  ___  ******************************/
	/******************************  |    \      / |     |\   |   |   |     ******************************/
	/******************************  |___  \    /  |___  | \  |   |   |___  ******************************/
	/******************************  |      \  /   |     |  \ |   |       | ******************************/
	/******************************  |___    \/    |___  |   \|   |    ___| ******************************/
	/******************************                                         ******************************/
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	
	
	/**
	 * Uz pomoć FB tokena vadi listu korisnikovih dogadjaja
	 * @return CustomFacebookEvents - sadrzi listu korisnikovih eventova
	 */
	public CustomFacebookEvents getUserEvents(String accessToken){
		final Facebook facebook = new FacebookTemplate(accessToken);
		if (!facebook.isAuthorized()) return null;
		
		final String uri = "https://graph.facebook.com/v6.0/me?fields=events.limit(20)%7Bowner%2Ccover%2Cattending_count%2Cdescription%2Cend_time%2Cguest_list_enabled%2Cid%2Cdeclined_count%2Ccan_guests_invite%2Cstart_time%2Cplace%2Cname%2Cmaybe_count%2Cis_canceled%2Ctimezone%2Ctype%2Cupdated_time%2Cinterested_count%2Cis_online%7D&access_token=" + accessToken;
		
		ResponseEntity<CustomFacebookEvents> fbEvents = facebook.restOperations().getForEntity(URI.create(uri), CustomFacebookEvents.class);
		return fbEvents.getBody();
	}
	
	
	/**
	 * Osvezava u bazi osnovna polja modelEvent na osnovu fbEvent
	 * @param fbEvent - CustomFacebookEventData
	 * @param eventId - ID eventa u DB
	 */
	public void updateDbEvent(CustomFacebookEventData fbEvent, Long eventId) {
		Event dbEvent = eventService.findById(eventId);
		if (dbEvent==null) {
			return;
		}
		
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
		dbEvent.setGuest_list_enabledFB(fbEvent.getGuest_list_enabled());
		dbEvent.setName(fbEvent.getName());
		dbEvent.setCover(new Cover());
		dbEvent.getCover().setFbId(fbEvent.getCover().getId());
		dbEvent.getCover().setOffset_x(fbEvent.getCover().getOffset_x());
		dbEvent.getCover().setOffset_y(fbEvent.getCover().getOffset_y());
		dbEvent.getCover().setSource(fbEvent.getCover().getSource());
		dbEvent.getOwner().setFacebookId(fbEvent.getOwner().getId());
		
		eventService.save(dbEvent);
	}
	
	
	
	/**
	 * Proverava u bazi da li je fb event sveziji i sa promenjenim parametrima.
	 * Prvo se proverava da li je "updated_time" fbEvent > dbEventa (koji se izvlaci iz baze)
	 * @param fbEvent
	 * @return true (ukoliko nema promena ili je obrisan) ili false (ima promena i potrebno je osveziti bazu)
	 */
	public Boolean isUpToDate(CustomFacebookEventData fbEvent) {
		Event dbEvent = eventService.findByFacebookId(fbEvent.getId());
		if (dbEvent==null) {
			return false;
		}
		//nema pull eventa ukoliko je on obrisan
		if (dbEvent.getIsDeleted()) {
			return true;
		}
		
		return EventComparator.compare(fbEvent, dbEvent);
	}

	
	/***********************************************************************************************/
	/***********************************************************************************************/
	/******************************          ___    ___   ___    ___  ******************************/
	/******************************  |   |  |      |     |   |  |     ******************************/
	/******************************  |   |  |___   |___  |___|  |___  ******************************/
	/******************************  |   |      |  |     |  \       | ******************************/
	/******************************  \ _ /   ___|  |___  |   \   ___| ******************************/
	/******************************                                   ******************************/
	/***********************************************************************************************/
	/***********************************************************************************************/
	
	/**
	 * Metoda vraca FB profil ulogovanog korisnika
	 * @param accessToken
	 * @return
	 */
	public CustomFacebookProfile getFbUserProfile(String accessToken) {
		final Facebook facebook = new FacebookTemplate(accessToken);
		if (!facebook.isAuthorized()) return null;
		
		final String uri = "https://graph.facebook.com/v6.0/me?fields=id%2Cname%2Cemail%2Cpicture%7Burl%2Cheight%2Cwidth%7D&access_token=" + accessToken;
		
		ResponseEntity<CustomFacebookProfile> fbUser = facebook.restOperations().getForEntity(URI.create(uri), CustomFacebookProfile.class);
		return fbUser.getBody();
	}
	
	
	/**
	 * Proverava da li su osnovni podaci korisnika azurni sa fb
	 * @param fbProfile
	 * @return true ako se profil ne treba azurirati u suprotnom false
	 */
	public Boolean isUpToDate(CustomFacebookProfile fbProfile) {
		User dbUser = userService.findByFacebookId(fbProfile.getId());
		if (dbUser==null) {
			return false;
		}
		
		return UserComparator.compare(fbProfile, dbUser);
	}


	/**
	 * Azuriranje DB User profila u skladu sa FB User profilom (ime i slika)
	 * @param fbProfile - CustomFacebookProfile
	 * @param userId - Long
	 * @return user - DB user that is saved to db
	 */
	public User updateDbUser(CustomFacebookProfile fbProfile, Long userId) {
		User dbUser = userService.findById(userId);
		if (dbUser==null) {
			return null;
		}
		
		dbUser.setFacebookId(fbProfile.getId());
		dbUser.setName(fbProfile.getName());
		dbUser.setEmail(fbProfile.getEmail());
		dbUser.setImageUri(fbProfile.getPicture().getData().getUrl());
		dbUser.setImageHeight(fbProfile.getPicture().getData().getHeight());
		dbUser.setImageWidth(fbProfile.getPicture().getData().getWidth());
		
		return userService.save(dbUser);
	}


	/**
	 * Preuzimanje eventova sa FB i azuriranje istih u bazi
	 * @param accessToken
	 * @param newUser
	 */
	public void pullEvents(String accessToken, User newUser) {
		//povlacenje liste eventova sa fb
		var events = getUserEvents(accessToken);
		for (CustomFacebookEventData fbEvent : events.getEvents().getData()) {
			if (!isUpToDate(fbEvent)) {
				//da li je to postojeci event, pa ga treba samo azurirati?
				Event dbEvent = eventService.findByFacebookId(fbEvent.getId());
				if (dbEvent != null) {
					//event postoji
					updateDbEvent(fbEvent, dbEvent.getId());
				} else {
					//ovo je novi event
					dbEvent = EventConverter.convert(fbEvent);
					dbEvent.setOwner(new Owner(newUser, fbEvent.getOwner().getId()));
					dbEvent = eventService.save(dbEvent);
				}
			}
		}
	}
	
	
}
