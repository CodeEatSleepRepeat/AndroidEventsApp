package rs.ac.uns.ftn.eventsbackend.gson.getFbUserProfile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFacebookProfile {

	private String id;
	private String name;
	private String email;
	private String url;

}
