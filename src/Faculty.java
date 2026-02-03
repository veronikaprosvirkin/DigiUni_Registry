import java.util.ArrayList;
import java.util.List;

public class Faculty {
    private String nameOfFaculty;
    private List<Department> departments = new ArrayList<>();

    public Faculty(String nameOfFaculty) {
        this.nameOfFaculty = nameOfFaculty;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}
