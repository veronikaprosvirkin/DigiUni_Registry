import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

// Department entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Department implements NamedEntity {
    @EqualsAndHashCode.Include
    private String id;
    private String nameOfDepartment;
    private Teacher head;
    private String location;
    private List<Teacher> teachers = new ArrayList<>();

    public Department(String id, String nameOfDepartment) {
        this.id = id;
        this.nameOfDepartment = nameOfDepartment;
    }

    public Teacher getHead() {
        return head;
    }

    public void setHead(Teacher head) {
        this.head = head;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getName() {
        return nameOfDepartment;
    }

    // Override for NamedEntity logic
    public void setName(String editName) {
        this.nameOfDepartment = editName;
    }

    @Override
    public String toString() {
        return nameOfDepartment;
    }

    @Override
    public String getDisplayInfo() {
        return "[Code: " + this.id + "] " + this.nameOfDepartment;
    }
}