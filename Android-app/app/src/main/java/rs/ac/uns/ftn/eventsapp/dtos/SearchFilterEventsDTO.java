package rs.ac.uns.ftn.eventsapp.dtos;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;
import rs.ac.uns.ftn.eventsapp.models.SortType;

public class SearchFilterEventsDTO {

    private String search;
    private int distance;
    private Double lat;
    private Double lng;
    private SortType sortType;
    private List<EventType> eventTypes;
    private ZonedDateTime eventStart = ZonedDateTime.now();
    private ZonedDateTime eventEnd = ZonedDateTime.now();
    private FacebookPrivacy facebookPrivacy;

    public SearchFilterEventsDTO() {
    }

    public SearchFilterEventsDTO(String search, int distance, Double lat, Double lng, SortType sortType, List<EventType> eventTypes, ZonedDateTime eventStart, ZonedDateTime eventEnd, FacebookPrivacy facebookPrivacy) {
        this.search = search;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
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

    public ZonedDateTime getEventStart() {
        return eventStart;
    }

    public void setEventStart(ZonedDateTime eventStart) {
        this.eventStart = eventStart;
    }

    public ZonedDateTime getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(ZonedDateTime eventEnd) {
        this.eventEnd = eventEnd;
    }

    public FacebookPrivacy getFacebookPrivacy() {
        return facebookPrivacy;
    }

    public void setFacebookPrivacy(FacebookPrivacy facebookPrivacy) {
        this.facebookPrivacy = facebookPrivacy;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
