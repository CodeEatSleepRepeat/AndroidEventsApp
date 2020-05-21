package rs.ac.uns.ftn.eventsapp.dtos;

public class OwnerCreateEventDTO {

    private Long userId;
    private String facebookId;

    public OwnerCreateEventDTO(){}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
