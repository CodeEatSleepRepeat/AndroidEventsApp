package rs.ac.uns.ftn.eventsapp.apiCalls;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rs.ac.uns.ftn.eventsapp.dtos.InvitationDTO;

public interface InvitationAppApi {

    @POST("/invitation/{senderId}/{receiverId}/{eventId}")
    Call<InvitationDTO> sendInvitation(@Path("senderId") Long senderId,
                                       @Path("receiverId") Long receiverId,
                                       @Path("eventId") Long eventId);

    @DELETE("/invitation/{invitationId}")
    Call<InvitationDTO> deleteInvitation(@Path("invitationId") Long invitationId);

    @GET("/invitation/getInvitationsOf/{userId}")
    Call<List<InvitationDTO>> getUserInvitations(@Path("userId") Long userId);
}
