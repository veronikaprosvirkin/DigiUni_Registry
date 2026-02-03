import java.util.ArrayList;
import java.util.List;

public class Department {
    private String nameOfDepartment;
    private List<Teacher> teachers= new ArrayList<>();
    private List<Student> students= new ArrayList<>();

    public Department(String nameOfDepartment) {
        this.nameOfDepartment = nameOfDepartment;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
