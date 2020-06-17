package rs.ac.uns.ftn.eventsbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.eventsbackend.dto.FriendshipDTO;
import rs.ac.uns.ftn.eventsbackend.dto.InvitationDTO;
import rs.ac.uns.ftn.eventsbackend.model.Friendship;
import rs.ac.uns.ftn.eventsbackend.model.Invitation;
import rs.ac.uns.ftn.eventsbackend.service.InvitationService;

@RequestMapping("/invitation")
@RestController
public class InvitationController {

	private final InvitationService invitationService;

	public InvitationController(InvitationService invitationService){
		this.invitationService = invitationService;
	}

	@PostMapping("/{senderId}/{receiverId}/{eventId}")
	public ResponseEntity<InvitationDTO> createInvitation(@PathVariable Long senderId, @PathVariable Long receiverId, @PathVariable Long eventId) throws Exception {
		Invitation newInvitation = invitationService.createInvitation(senderId, receiverId, eventId);
		InvitationDTO newInvitationDTO = new InvitationDTO(newInvitation);

		return ResponseEntity.ok(newInvitationDTO);
	}

	@DeleteMapping("/{invitationId}")
	public ResponseEntity<InvitationDTO> deleteInvitation(@PathVariable Long invitationId) throws Exception {
		Invitation deletedInvitation = invitationService.deleteInvitation(invitationId);
		InvitationDTO deletedInvitationDTO = new InvitationDTO(deletedInvitation);

		return ResponseEntity.ok(deletedInvitationDTO);
	}

}
