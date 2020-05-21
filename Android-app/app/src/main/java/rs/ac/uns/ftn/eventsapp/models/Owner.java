package rs.ac.uns.ftn.eventsapp.models;

public class Owner {

    private Long id;
    private User user;
    private String facebookId;

    public Owner(){}

    public Owner(Long id, User user, String facebookId) {
        this.id = id;
        this.user = user;
        this.facebookId = facebookId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
