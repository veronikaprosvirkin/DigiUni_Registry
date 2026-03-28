import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Faculty implements NamedEntity{
    private String nameOfFaculty;
    private List<Speciality> speciality = new ArrayList<>();
    private List<Department> departments = new ArrayList<>();
    private String id;
    private String contacts;
    private Teacher dean;


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
        String deanName;
        if (dean != null) {
            deanName = dean.getFullName();
        } else {
            deanName = "Не призначено";
        }
        return "[" + id + "] " + nameOfFaculty + " | Контакти: " + contacts + " | Декан: " + deanName;
    }

    @Override
    public String getDisplayInfo() {
        return "[Code: " + this.id + "] " + this.nameOfFaculty;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getContacts() { return contacts;}
    public void setContacts(String contacts) {this.contacts = contacts;}

    public Teacher getDean() { return dean; }
    public void setDean(Teacher dean) { this.dean = dean; }
    public String getId() { return id; }
}
