package rs.ac.uns.ftn.eventsbackend.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Data
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String text;

	@NotNull
	@ManyToOne
	private User user;

	@NotNull
	@ManyToOne
	private Event event;

	@JsonProperty("timestamp")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@CreationTimestamp
	private Date timestamp;

	public Comment(Long id, @NotNull String text, @NotNull User user, @NotNull Event event, Date timestamp) {
		super();
		this.id = id;
		this.text = text;
		this.user = user;
		this.event = event;
		this.timestamp = timestamp;
	}

	public Comment() {

	}

}