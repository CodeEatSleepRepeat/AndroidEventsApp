package rs.ac.uns.ftn.eventsbackend.gson.getUserEvents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFacebookEventOwner {

	private String name;
	private String id;
}
