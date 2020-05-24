package rs.ac.uns.ftn.eventsapp.dtos.firebase;

public class FirebaseUserDTO {

    private String uid;
    private String username;
    private String profileImageUrl;
    private String email;

    public FirebaseUserDTO(){}

    public FirebaseUserDTO(String uid, String username, String profileImageUrl, String email) {
        this.uid = uid;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}