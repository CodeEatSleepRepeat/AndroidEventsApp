package rs.ac.uns.ftn.eventsbackend.comparators;

import java.time.ZonedDateTime;

import rs.ac.uns.ftn.eventsbackend.gson.getUserEvents.CustomFacebookEventCover;
import rs.ac.uns.ftn.eventsbackend.gson.getUserEvents.CustomFacebookEventData;
import rs.ac.uns.ftn.eventsbackend.model.Cover;
import rs.ac.uns.ftn.eventsbackend.model.Event;

public class EventComparator {

	public static Boolean compare(CustomFacebookEventCover fbCover, Cover cover) {
		if (cover.getFbId() != null) {
			// ceo cover se ispituje
			if (!compare(fbCover.getId(), cover.getFbId()))
				return false;
			if (!compare(fbCover.getOffset_x(), cover.getOffset_x()))
				return false;
			if (!compare(fbCover.getOffset_y(), cover.getOffset_y()))
				return false;
		}

		if (!fbCover.getSource().equals(cover.getSource()))
			return false;

		return true;
	}

	public static Boolean compare(CustomFacebookEventData fbEvent, Event dbEvent) {
		if (dbEvent.getFacebookId() != null) {
			// ceo event se ispituje
			if (!compare(dbEvent.getFacebookId(), fbEvent.getId()))
				return false;
			if (!compare(dbEvent.getUpdated_timeFB(), fbEvent.getUpdated_time()))
				return false;
			if (!compare(dbEvent.getAttending_countFB(), fbEvent.getAttending_count()))
				return false;
			if (!compare(dbEvent.getGuest_list_enabledFB(), fbEvent.getGuest_list_enabled()))
				return false;
			if (!compare(dbEvent.getDeclined_count(), fbEvent.getDeclined_count()))
				return false;
			if (!compare(dbEvent.getCan_guests_invite(), fbEvent.getCan_guests_invite()))
				return false;
			if (!compare(dbEvent.getMaybe_count(), fbEvent.getMaybe_count()))
				return false;
			if (!compare(dbEvent.getIs_canceled(), fbEvent.getIs_canceled()))
				return false;
			if (!compare(dbEvent.getTimezone(), fbEvent.getTimezone()))
				return false;
			if (!compare(dbEvent.getInterested_count(), fbEvent.getInterested_count()))
				return false;
			if (!compare(dbEvent.getIs_online(), fbEvent.getIs_online()))
				return false;
		}

		if (!UserComparator.compare(fbEvent.getOwner(), dbEvent.getOwner()))
			return false;
		if (!compare(fbEvent.getCover(), dbEvent.getCover()))
			return false;
		if (!compare(dbEvent.getName(), fbEvent.getName()))
			return false;
		if (!compare(dbEvent.getDescription(), fbEvent.getDescription()))
			return false;
		if (!compare(dbEvent.getPrivacy().toString().toLowerCase(), fbEvent.getType()))
			return false;
		if (!compare(dbEvent.getStart_time(), fbEvent.getStart_time()))
			return false;
		if (!compare(dbEvent.getEnd_time(), fbEvent.getEnd_time()))
			return false;
		if (!compare(dbEvent.getPlace(), fbEvent.getPlace().getName()))
			return false;

		return true;
	}

	private static boolean compare(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equals(str2));
	}

	private static boolean compare(ZonedDateTime d1, ZonedDateTime d2) {
		return (d1 == null ? d2 == null : d1.equals(d2));
	}

	private static boolean compare(Long l1, Long l2) {
		return (l1 == null ? l2 == null : l1.equals(l2));
	}

	private static boolean compare(Boolean b1, Boolean b2) {
		return (b1 == null ? b2 == null : b1.equals(b2));
	}
}
