package rs.ac.uns.ftn.eventsapp.apiCalls;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rs.ac.uns.ftn.eventsapp.dtos.CreateEventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.SearchFilterEventsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.StringDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UpdateEventDTO;

public interface EventsAppAPI {

    @POST("/event")
    Call<EventDTO> createEvent(@Body CreateEventDTO dto);

    @POST("/event/upload/{id}")
    Call<EventDTO> uploadEventImg(@Body RequestBody file, @Path("id") Long id);

    @POST("/event/page/{num}")
    Call<List<EventDTO>> getInitialEvents(@Path("num") int num, @Body SearchFilterEventsDTO searchFilterEventsDTO);

    @POST("/event/myevents/{id}/{num}")
    Call<List<EventDTO>> getMyEvents(@Path("id") Long id, @Path("num") int num, @Body SearchFilterEventsDTO searchFilterEventsDTO);

    @POST("/event/goingevents/{id}/{num}")
    Call<List<EventDTO>> getGoingEvents(@Path("id") Long id, @Path("num") int num, @Body SearchFilterEventsDTO searchFilterEventsDTO);

    @POST("/event/interestedevents/{id}/{num}")
    Call<List<EventDTO>> getInterestedEvents(@Path("id") Long id, @Path("num") int num, @Body SearchFilterEventsDTO searchFilterEventsDTO);

    @GET("/event/going/{eventId}/{userId}")
    Call<EventDTO> goingToEvent(@Path("eventId") Long eventId, @Path("userId") Long userId);

    @GET("/event/remove/going/{eventId}/{userId}")
    Call<EventDTO> removeGoingEvent(@Path("eventId") Long eventId, @Path("userId") Long userId);

    @GET("/event/interested/{eventId}/{userId}")
    Call<EventDTO> interestedInEvent(@Path("eventId") Long eventId, @Path("userId") Long userId);

    @GET("/event/remove/interested/{eventId}/{userId}")
    Call<EventDTO> removeInterestedEvent(@Path("eventId") Long eventId, @Path("userId") Long userId);

    @DELETE("/event/{userId}/{eventId}")
    Call<EventDTO> removeMyEvent(@Path("userId") Long userId, @Path("eventId") Long eventId);

    @PUT("/event/{userId}")
    Call<EventDTO> updateEvent(@Path("userId") Long userId, @Body UpdateEventDTO dto);

    @GET("/event/test/{id}")
    Call<StringDTO> getEventUpdateImage(@Path("id") Long id);

}
