import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

// Faculty entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Faculty implements NamedEntity {
    @EqualsAndHashCode.Include
    private String id;
    private String nameOfFaculty;
    private String contacts;
    private Teacher dean;
    private List<Speciality> speciality = new ArrayList<>();
    private List<Department> departments = new ArrayList<>();

    public Faculty(String id, String nameOfFaculty, String contacts, Teacher dean) {
        this.id = id;
        this.nameOfFaculty = nameOfFaculty;
        this.contacts = contacts;
        this.dean = dean;
    }

    @Override
    public String getName() {
        return nameOfFaculty;
    }

    // Override for NamedEntity logic
    public void setName(String newName) {
        this.nameOfFaculty = newName;
    }

    @Override
    public String toString() {
        String deanName = (dean != null) ? dean.getFullName() : "Not assigned";
        return "[" + id + "] " + nameOfFaculty + " | Contacts: " + contacts + " | Dean: " + deanName;
    }

    @Override
    public String getDisplayInfo() {
        return "[Code: " + this.id + "] " + this.nameOfFaculty;
    }
}