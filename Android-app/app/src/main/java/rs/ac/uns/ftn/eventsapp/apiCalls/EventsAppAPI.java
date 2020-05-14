package rs.ac.uns.ftn.eventsapp.apiCalls;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rs.ac.uns.ftn.eventsapp.dtos.CreateEventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;

public interface EventsAppAPI {

    @POST("/event")
    Call<EventDTO> createEvent(@Body CreateEventDTO dto);

}
