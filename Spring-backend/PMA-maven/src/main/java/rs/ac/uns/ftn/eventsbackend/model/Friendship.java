package rs.ac.uns.ftn.eventsbackend.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import rs.ac.uns.ftn.eventsbackend.enums.FriendshipStatus;

@Entity
@Data
public class Friendship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private User requestSender;

	@NotNull
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private User requestReceiver;

	@NotNull
	private FriendshipStatus status;

	public Friendship(Long id, @NotNull User requestSender, @NotNull User requestReceiver,
			@NotNull FriendshipStatus status) {
		super();
		this.id = id;
		this.requestSender = requestSender;
		this.requestReceiver = requestReceiver;
		this.status = status;
	}

	public Friendship() {

	}

}
