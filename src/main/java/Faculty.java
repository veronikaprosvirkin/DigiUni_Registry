import java.util.ArrayList;
import java.util.List;

public class Faculty implements NamedEntity{
    private String nameOfFaculty;
    private List<Speciality> speciality = new ArrayList<>();
    private List<Department> departments = new ArrayList<>();
    private String id;


    public Faculty(String id, String nameOfFaculty) {
        this.id=id;
        this.nameOfFaculty = nameOfFaculty;
    }

    public List<Speciality> getSpeciality() {
        return speciality;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public void setSpeciality(List<Speciality> specialities) {
        this.speciality = specialities;
    }

    public String getName() { return nameOfFaculty; }

    public void setName(String newName) {
        this.nameOfFaculty = newName;
    }

    @Override
    public String toString() {
        return nameOfFaculty;
    }

    @Override
    public String getDisplayInfo() {
        return "[Code: " + this.id + "] " + this.nameOfFaculty;
    }
}
