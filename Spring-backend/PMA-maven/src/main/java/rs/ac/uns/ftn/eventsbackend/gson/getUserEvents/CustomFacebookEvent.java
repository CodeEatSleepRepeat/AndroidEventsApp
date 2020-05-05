package rs.ac.uns.ftn.eventsbackend.gson.getUserEvents;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFacebookEvent {

	private List<CustomFacebookEventData> data;
}
