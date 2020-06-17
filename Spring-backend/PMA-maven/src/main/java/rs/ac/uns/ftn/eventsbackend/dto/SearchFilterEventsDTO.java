package rs.ac.uns.ftn.eventsbackend.dto;


import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.eventsbackend.enums.EventType;
import rs.ac.uns.ftn.eventsbackend.enums.FacebookPrivacy;
import rs.ac.uns.ftn.eventsbackend.enums.SortType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilterEventsDTO {
	
	private String search;
    private int distance;
    private Double lat;
    private Double lng;
    private SortType sortType;
    private List<EventType> eventTypes;
    private ZonedDateTime eventStart;
    private ZonedDateTime eventEnd;
    private FacebookPrivacy facebookPrivacy;

}
