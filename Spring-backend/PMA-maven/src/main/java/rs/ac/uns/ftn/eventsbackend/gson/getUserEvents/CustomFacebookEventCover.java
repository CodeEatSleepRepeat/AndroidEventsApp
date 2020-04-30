package rs.ac.uns.ftn.eventsbackend.gson.getUserEvents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFacebookEventCover {

	private Long offset_x;
	private Long offset_y;
	private String source;
	private String id;
}
