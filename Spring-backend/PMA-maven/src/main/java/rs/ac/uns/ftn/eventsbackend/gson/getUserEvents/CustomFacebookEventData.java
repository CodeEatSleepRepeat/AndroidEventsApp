package rs.ac.uns.ftn.eventsbackend.gson.getUserEvents;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	@Temporal(TemporalType.TIMESTAMP)
	private Date end_time;
	private Boolean guest_list_enabled;
	private String id;
	private Long declined_count;
	private Boolean can_guests_invite;
	@JsonProperty("start_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	@Temporal(TemporalType.TIMESTAMP)
	private Date start_time;
	private CustomFacebookEventPlace place;
	private String name;
	private Long maybe_count;
	private Boolean is_canceled;
	private String timezone;
	private String type;
	@JsonProperty("updated_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_time;
	private Long interested_count;
	private Boolean is_online;

	public FacebookPrivacy getPrivacy() {
		return FacebookPrivacy.valueOf(type.toUpperCase());
	}

	@SuppressWarnings("deprecation")
	public ZonedDateTime getEnd_time() {
		return ZonedDateTime.of(end_time.getYear()+1900, end_time.getMonth()+1, end_time.getDate(), end_time.getHours(), end_time.getMinutes(), end_time.getSeconds(), 0, ZoneId.of(timezone));
	}

	@SuppressWarnings("deprecation")
	public ZonedDateTime getStart_time() {
		return ZonedDateTime.of(start_time.getYear()+1900, start_time.getMonth()+1, start_time.getDate(), start_time.getHours(), start_time.getMinutes(), start_time.getSeconds(), 0, ZoneId.of(timezone));
	}

	@SuppressWarnings("deprecation")
	public ZonedDateTime getUpdated_time() {
		return ZonedDateTime.of(updated_time.getYear()+1900, updated_time.getMonth()+1, updated_time.getDate(), updated_time.getHours(), updated_time.getMinutes(), updated_time.getSeconds(), 0, ZoneId.of(timezone));
	}
	
	

}
