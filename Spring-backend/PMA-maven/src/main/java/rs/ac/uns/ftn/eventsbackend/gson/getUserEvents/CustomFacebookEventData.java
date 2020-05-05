package rs.ac.uns.ftn.eventsbackend.gson.getUserEvents;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import rs.ac.uns.ftn.eventsbackend.enums.FacebookPrivacy;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFacebookEventData {

	private CustomFacebookEventOwner owner;
	private CustomFacebookEventCover cover;
	private Long attending_count;
	private String description;
	@JsonProperty("end_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date end_time;
	private Boolean guest_list_enabled;
	private String id;
	private Long declined_count;
	private Boolean can_guests_invite;
	@JsonProperty("start_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date start_time;
	private CustomFacebookEventPlace place;
	private String name;
	private Long maybe_count;
	private Boolean is_canceled;
	private String timezone;
	private String type;
	@JsonProperty("updated_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date updated_time;
	private Long interested_count;
	private Boolean is_online;

	public FacebookPrivacy getPrivacy() {
		return FacebookPrivacy.valueOf(type.toUpperCase());
	}

}
