package rs.ac.uns.ftn.eventsapp.dtos;

import org.threeten.bp.ZonedDateTime;

import rs.ac.uns.ftn.eventsapp.models.GoingInterestedStatus;

public class GoingInterestedEventsDTO {
    private EventDTO event;
    private GoingInterestedStatus status;

    public GoingInterestedEventsDTO(EventDTO event, GoingInterestedStatus status) {
        this.event = event;
        this.status = status;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public GoingInterestedStatus getStatus() {
        return status;
    }

    public void setStatus(GoingInterestedStatus status) {
        this.status = status;
    }
}
