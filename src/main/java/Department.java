import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Department implements NamedEntity {
    private String nameOfDepartment;
    private List<Teacher> teachers= new ArrayList<>();
    private String id;


    public Department(String id,String nameOfDepartment) {
        this.id=id;
        this.nameOfDepartment = nameOfDepartment;
    }
    public String getName() { return nameOfDepartment; }
    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
