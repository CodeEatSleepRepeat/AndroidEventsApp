package rs.ac.uns.ftn.eventsbackend.comparators;

import rs.ac.uns.ftn.eventsbackend.gson.getFbUserProfile.CustomFacebookProfile;
import rs.ac.uns.ftn.eventsbackend.gson.getUserEvents.CustomFacebookEventOwner;
import rs.ac.uns.ftn.eventsbackend.model.Owner;
import rs.ac.uns.ftn.eventsbackend.model.User;

public class UserComparator {

	public static Boolean compare(CustomFacebookProfile fbProfile, User user) {

		if (!compare(fbProfile.getId(), user.getFacebookId())) return false;
		if (!compare(fbProfile.getName(), user.getUserName())) return false;
		if (!compare(fbProfile.getEmail(), user.getEmail())) return false;
		if (!compare(fbProfile.getPicture().getData().getUrl(), user.getUserImageURI())) return false;
		if (!compare(fbProfile.getPicture().getData().getHeight(), user.getImageHeight())) return false;
		if (!compare(fbProfile.getPicture().getData().getWidth(), user.getImageWidth())) return false;
		
		return true;
	}
	
	public static Boolean compare(CustomFacebookEventOwner fbOwner, Owner owner) {
		return compare(fbOwner.getId(), owner.getFacebookId());
	}
	
	private static boolean compare(String str1, String str2) {
	    return (str1 == null ? str2 == null : str1.equals(str2));
	}
	
	private static boolean compare(Integer d1, Integer d2) {
	    return (d1 == null ? d2 == null : d1.equals(d2));
	}
	
}