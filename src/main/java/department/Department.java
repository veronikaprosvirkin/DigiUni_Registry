package department;

import faculty.Faculty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import person.Teacher;
import utils.annotations.DetailDisplay;
import utils.namedEntity.NamedEntity;

// Department entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Department implements NamedEntity, Serializable {
    private static final long serialVersionUID = 1L;
    private Faculty faculty;
    @EqualsAndHashCode.Include
    @DetailDisplay(label = "Department Code")
    private String id;
    @DetailDisplay(label = "Department Name")
    private String nameOfDepartment;
    @DetailDisplay(label = "Head of Department")
    private Teacher head;
    @DetailDisplay(label = "Location")
    private String location;
    private List<Teacher> teachers = new ArrayList<>();

    public Department(String id, String nameOfDepartment) {
        this.id = id;
        this.nameOfDepartment = nameOfDepartment;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
        if (head != null) {
            head.setFaculty(faculty);
            head.setDepartment(this);
        }
        if (teachers != null) {
            for (Teacher teacher : teachers) {
                if (teacher != null) {
                    teacher.setFaculty(faculty);
                    teacher.setDepartment(this);
                }
            }
        }
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