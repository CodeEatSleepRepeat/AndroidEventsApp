package rs.ac.uns.ftn.eventsbackend.gson.getFbUserProfile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFacebookPicture {

	private CustomFacebookData data;
	
}
