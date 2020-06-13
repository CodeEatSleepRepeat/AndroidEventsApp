package rs.ac.uns.ftn.eventsapp.dtos;

public class UserDTO {

    private Long id;
    private String name;
    private String imgUri;

    public UserDTO(){}

    public UserDTO(Long id, String name, String imgUri) {
        this.id = id;
        this.name = name;
        this.imgUri = imgUri;
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
}
