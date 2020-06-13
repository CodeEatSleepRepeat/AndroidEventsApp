package rs.ac.uns.ftn.eventsbackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.eventsbackend.dto.FriendshipDTO;
import rs.ac.uns.ftn.eventsbackend.enums.FriendshipStatus;
import rs.ac.uns.ftn.eventsbackend.model.Friendship;
import rs.ac.uns.ftn.eventsbackend.service.FriendshipService;

@RequestMapping("/friendship")
@RestController
public class FriendshipController {

	private FriendshipService friendshipService;

	public FriendshipController(FriendshipService service){
		this.friendshipService = service;
	}

	@PostMapping("/{senderId}/{receiverId}")
	public ResponseEntity<FriendshipDTO> createFriendRequest(@PathVariable Long senderId, @PathVariable Long receiverId) throws Exception {
		Friendship newFriendshipRequest = friendshipService.createFriendRequest(senderId, receiverId);
		FriendshipDTO newFriendshipRequestDTO = new FriendshipDTO(newFriendshipRequest);

		return ResponseEntity.ok(newFriendshipRequestDTO);
	}

	@GetMapping("/getAllFriendRequests/{userId}/page/{num}")
	public ResponseEntity<List<FriendshipDTO>> getAllFriendRequests(@PathVariable int num, @PathVariable Long userId){
		Pageable pageable = PageRequest.of(num, 15);
		List<Friendship> foundUsers = friendshipService.getAllFriendRequests(userId,FriendshipStatus.PENDING, pageable);
		List<FriendshipDTO> foundRequestsDTO = new ArrayList<FriendshipDTO>();
		for(Friendship friendship : foundUsers){
			FriendshipDTO friendshipDTO = new FriendshipDTO(friendship);
			foundRequestsDTO.add(friendshipDTO);
		}
		return ResponseEntity.ok(foundRequestsDTO);
	}

	@GetMapping("/getNumberOfUserFriendRequests/{userId}")
	public ResponseEntity<Integer> getNumberOfUserFriendRequests(@PathVariable Long userId){
		int numberOfFriendRequests = friendshipService.getNumberOfUsersFriendRequests(userId);

		return ResponseEntity.ok(numberOfFriendRequests);
	}

	@PutMapping("/acceptRequest/{receiverId}/{requestId}")
	public ResponseEntity<FriendshipDTO> acceptRequest(@PathVariable Long receiverId, @PathVariable Long requestId) throws Exception {
		Friendship accepted = friendshipService.acceptRequestAndReturnStatus(receiverId, requestId);
		FriendshipDTO acceptedRequestDTO = new FriendshipDTO(accepted);
		return ResponseEntity.ok(acceptedRequestDTO);
	}


	@DeleteMapping("/{requestId}")
	public ResponseEntity<FriendshipDTO> deleteRequest(@PathVariable Long requestId) throws Exception {
		Friendship accepted = friendshipService.deleteFriendshipRequest(requestId);
		FriendshipDTO acceptedRequestDTO = new FriendshipDTO(accepted);
		return ResponseEntity.ok(acceptedRequestDTO);
	}
}
