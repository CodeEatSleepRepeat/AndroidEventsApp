package rs.ac.uns.ftn.eventsbackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.eventsbackend.dto.InvitationDTO;
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

	@GetMapping("/getInvitationsOf/{userId}")
	public ResponseEntity<List<InvitationDTO>> getUserInvitations(@PathVariable Long userId){
		List<Invitation> receivedInvitations = invitationService.getUserReceivedInvitations(userId);
		List<InvitationDTO> receivedInvitationsDTO = new ArrayList<InvitationDTO>();
		for(Invitation invitation : receivedInvitations){
			InvitationDTO invitationDTO = new InvitationDTO(invitation);
			receivedInvitationsDTO.add(invitationDTO);
		}

		return ResponseEntity.ok(receivedInvitationsDTO);
	}


}
