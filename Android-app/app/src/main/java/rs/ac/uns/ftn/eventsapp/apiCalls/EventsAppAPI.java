package rs.ac.uns.ftn.eventsapp.apiCalls;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rs.ac.uns.ftn.eventsapp.dtos.CreateEventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;

public interface EventsAppAPI {

    @POST("/event")
    Call<EventDTO> createEvent(@Body CreateEventDTO dto);

    @POST("/event/upload/{id}")
    Call<EventDTO> uploadEventImg(@Body RequestBody file, @Path("id") Long id);

    @GET("/event")
    Call<List<EventDTO>> getInitialEvents();

}
