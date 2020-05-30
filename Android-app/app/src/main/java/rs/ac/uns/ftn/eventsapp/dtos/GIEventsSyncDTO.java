package rs.ac.uns.ftn.eventsapp.dtos;

import java.util.ArrayList;

public class GIEventsSyncDTO {
    private String email;
    private String password;
    private ArrayList<GoingInterestedEventsDTO> eventsForUpdate;

    public GIEventsSyncDTO(String email, String password, ArrayList<GoingInterestedEventsDTO> eventsForUpdate) {
        this.email = email;
        this.password = password;
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

    public ArrayList<GoingInterestedEventsDTO> getEventsForUpdate() {
        return eventsForUpdate;
    }

    public void setEventsForUpdate(ArrayList<GoingInterestedEventsDTO> eventsForUpdate) {
        this.eventsForUpdate = eventsForUpdate;
    }
}
