package rs.ac.uns.ftn.eventsbackend.model;

import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String text;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private User sender;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private User recipient;

	@CreationTimestamp
	private ZonedDateTime created_time;

	@NotNull
	private Boolean seen;

	public ChatMessage(Long id, @NotNull String text, @NotNull User sender, @NotNull User recipient, ZonedDateTime created_time,
			@NotNull Boolean seen) {
		super();
		this.id = id;
		this.text = text;
		this.sender = sender;
		this.recipient = recipient;
		this.created_time = created_time;
		this.seen = seen;
	}

	public ChatMessage() {

	}

}
