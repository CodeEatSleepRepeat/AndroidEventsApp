package rs.ac.uns.ftn.eventsapp.dtos;

public class UserProfileChangeDTO {

    private String email;
    private String name;
    private String imageUri;
    private String passwordOld;
    private String passwordNew1;
    private String passwordNew2;

    public UserProfileChangeDTO(String email, String name, String imageUri, String passwordOld, String passwordNew1, String passwordNew2) {
        this.email = email;
        this.name = name;
        this.imageUri = imageUri;
        this.passwordOld = passwordOld;
        this.passwordNew1 = passwordNew1;
        this.passwordNew2 = passwordNew2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }

    public String getPasswordNew1() {
        return passwordNew1;
    }

    public void setPasswordNew1(String passwordNew1) {
        this.passwordNew1 = passwordNew1;
    }

    public String getPasswordNew2() {
        return passwordNew2;
    }

    public void setPasswordNew2(String passwordNew2) {
        this.passwordNew2 = passwordNew2;
    }
}
