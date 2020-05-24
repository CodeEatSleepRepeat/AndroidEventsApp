package rs.ac.uns.ftn.eventsapp.dtos;

public class UserSyncChangeDTO {

    private String email;
    private Boolean sync;

    public UserSyncChangeDTO(String email, Boolean sync) {
        this.email = email;
        this.sync = sync;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getSync() {
        return sync;
    }

    public void setSync(Boolean sync) {
        this.sync = sync;
    }
}
