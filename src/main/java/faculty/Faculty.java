package faculty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

import utils.annotations.DetailDisplay;
import utils.namedEntity.NamedEntity;
import person.Teacher;
import speciality.Speciality;
import department.Department;

// Faculty entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Faculty implements NamedEntity {
    @EqualsAndHashCode.Include
    @DetailDisplay(label = "Faculty Code")
    private String id;
    @DetailDisplay(label = "Faculty Name")
    private String nameOfFaculty;
    @DetailDisplay(label = "Short Name")
    private String shortName;
    @DetailDisplay(label = "Contacts")
    private String contacts;
    @DetailDisplay(label = "Dean")
    private Teacher dean;
    private List<Speciality> speciality = new ArrayList<>();
    private List<Department> departments = new ArrayList<>();

    public Faculty(String id, String nameOfFaculty, String shortName, String contacts, Teacher dean) {
        this.id = id;
        this.nameOfFaculty = nameOfFaculty;
        this.shortName = shortName;
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

    public List<Speciality> getSpecialities() {
        return speciality;
    }
}