package rs.ac.uns.ftn.eventsbackend.model;

import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(length = 10000)
	private String text;

	@NotNull
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private User user;

	@NotNull
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private Event event;

	@CreationTimestamp
	private ZonedDateTime timestamp;

	public Comment(Long id, @NotNull String text, @NotNull User user, @NotNull Event event, ZonedDateTime timestamp) {
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
