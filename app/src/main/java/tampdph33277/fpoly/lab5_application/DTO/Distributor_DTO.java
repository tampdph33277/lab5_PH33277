package tampdph33277.fpoly.lab5_application.DTO;

public class Distributor_DTO {
    String id ;
    String name;

    public Distributor_DTO() {
    }

    public Distributor_DTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
