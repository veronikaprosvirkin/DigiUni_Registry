import java.util.ArrayList;
import java.util.List;

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
}
