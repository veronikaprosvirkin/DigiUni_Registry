import java.util.ArrayList;
import java.util.List;

public class Department {
    private String nameOfDepartment;
    private List<Teacher> teachers= new ArrayList<>();

    public Department(String nameOfDepartment) {
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

    }
}
