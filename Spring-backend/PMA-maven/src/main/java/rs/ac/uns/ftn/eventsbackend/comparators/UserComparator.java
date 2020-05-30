package rs.ac.uns.ftn.eventsbackend.comparators;

import rs.ac.uns.ftn.eventsbackend.gson.getFbUserProfile.CustomFacebookProfile;
import rs.ac.uns.ftn.eventsbackend.gson.getUserEvents.CustomFacebookEventOwner;
import rs.ac.uns.ftn.eventsbackend.model.User;

public class UserComparator {

	public static Boolean compare(CustomFacebookProfile fbProfile, User user) {

		if (!compare(fbProfile.getId(), user.getFacebookId()))
			return false;
		if (!compare(fbProfile.getName(), user.getName()))
			return false;
		if (!compare(fbProfile.getEmail(), user.getEmail()))
			return false;
		if (!compare(fbProfile.getUrl(), user.getImageUri()))
			return false;
		
		return true;
	}

	public static Boolean compare(CustomFacebookEventOwner fbOwner, User owner) {
		return compare(fbOwner.getId(), owner.getFacebookId());
	}

	private static boolean compare(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equals(str2));
	}
}
