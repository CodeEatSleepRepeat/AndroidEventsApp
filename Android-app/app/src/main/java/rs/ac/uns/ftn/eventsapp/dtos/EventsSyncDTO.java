package rs.ac.uns.ftn.eventsapp.dtos;

import java.util.ArrayList;

public class EventsSyncDTO {

    private String email;
    private String password;
    private Long lastSyncTime;
    private ArrayList<UpdateEventDTO> eventsForUpdate;

    public EventsSyncDTO(String email, String password, Long lastSyncTime, ArrayList<UpdateEventDTO> eventsForUpdate) {
        this.email = email;
        this.password = password;
        this.lastSyncTime = lastSyncTime;
        this.eventsForUpdate = eventsForUpdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public ArrayList<UpdateEventDTO> getEventsForUpdate() {
        return eventsForUpdate;
    }

    public void setEventsForUpdate(ArrayList<UpdateEventDTO> eventsForUpdate) {
        this.eventsForUpdate = eventsForUpdate;
    }
}
