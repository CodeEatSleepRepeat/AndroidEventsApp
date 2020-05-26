package rs.ac.uns.ftn.eventsapp.dtos;

public class UserProfileSyncDTO {

    private String email;
    private String password;
    private Long lastSyncTime;

    public UserProfileSyncDTO(String email, String password, Long lastSyncTime) {
        this.email = email;
        this.password = password;
        this.lastSyncTime = lastSyncTime;
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
}
