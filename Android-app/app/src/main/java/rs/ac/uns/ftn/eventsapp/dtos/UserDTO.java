package rs.ac.uns.ftn.eventsapp.dtos;

public class UserDTO {

    private Long id;
    private String name;
    private String imgUri;
    private String email;

    public UserDTO(){}

    public UserDTO(Long id, String name, String imgUri, String email) {
        this.id = id;
        this.name = name;
        this.imgUri = imgUri;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
