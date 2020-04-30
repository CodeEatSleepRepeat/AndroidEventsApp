package rs.ac.uns.ftn.eventsbackend.gson.getUserEvents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFacebookEvents {

	private CustomFacebookEvent events;
	private String id;
	
}
