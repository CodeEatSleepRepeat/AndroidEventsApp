package rs.ac.uns.ftn.eventsbackend.model;

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
	private Long friendshipId;

	@NotNull
	@ManyToOne
	private User requestSender;
	
	@NotNull
	@ManyToOne
	private User requestReceiver;
	
	@NotNull
	private FriendshipStatus status;

	public Friendship(Long friendshipId, @NotNull User requestSender, @NotNull User requestReceiver,
			@NotNull FriendshipStatus status) {
		super();
		this.friendshipId = friendshipId;
		this.requestSender = requestSender;
		this.requestReceiver = requestReceiver;
		this.status = status;
	}

	public Friendship() {
	
	}
	
}
