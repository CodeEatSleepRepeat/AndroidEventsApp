package rs.ac.uns.ftn.eventsapp.dtos;

import java.util.List;

import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;
import rs.ac.uns.ftn.eventsapp.models.SortType;

public class SearchFilterEventsDTO {

    private String search;
    private int distance;
    private SortType sortType;
    private List<EventType> eventTypes;
    private String eventStart;
    private String eventEnd;
    private FacebookPrivacy facebookPrivacy;

    public SearchFilterEventsDTO() {
    }

    public SearchFilterEventsDTO(String search, int distance, SortType sortType, List<EventType> eventTypes, String eventStart, String eventEnd, FacebookPrivacy facebookPrivacy) {
        this.search = search;
        this.distance = distance;
        this.sortType = sortType;
        this.eventTypes = eventTypes;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.facebookPrivacy = facebookPrivacy;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(String eventEnd) {
        this.eventEnd = eventEnd;
    }

    public FacebookPrivacy getFacebookPrivacy() {
        return facebookPrivacy;
    }

    public void setFacebookPrivacy(FacebookPrivacy facebookPrivacy) {
        this.facebookPrivacy = facebookPrivacy;
    }
}
