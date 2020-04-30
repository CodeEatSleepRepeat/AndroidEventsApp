package rs.ac.uns.ftn.eventsbackend.gson.getFbUserProfile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFacebookData {

	private String url;
	private Integer height;
	private Integer width;
}
