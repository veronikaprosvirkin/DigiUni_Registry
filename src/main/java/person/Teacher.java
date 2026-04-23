package person;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import department.Department;
import faculty.Faculty;

// Teacher entity
@Data
@EqualsAndHashCode(callSuper = true)
public final class Teacher extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private Position position;
    private Department department;
    private Faculty faculty;
    private String academicDegree;
    private String academicTitle;
    private LocalDate employmentDate;
    private double workload;

    public Teacher(String id, String name, String surname, String patronymic, Position position, Department department) {
        this(id, name, surname, patronymic, position, department, null);
    }

    public Teacher(String id, String name, String surname, String patronymic, String position, Department department) {
        this(id, name, surname, patronymic, Position.fromString(position), department, null);
    }

    public Teacher(String id, String name, String surname, String patronymic, Position position, Department department, LocalDate dateOfBirth) {
        super(id, name, surname, patronymic, dateOfBirth);
        this.position = position;
        this.department = department;
    }

    public Teacher(String id, String name, String surname, String patronymic, String position, Department department, LocalDate dateOfBirth) {
        this(id, name, surname, patronymic, Position.fromString(position), department, dateOfBirth);
    }

    public void setPosition(String position) {
        this.position = Position.fromString(position);
    }

    @Override
    public String toString() {
        String deptName = (this.department != null) ? this.department.getName() : "No Department Assigned";
        return getFullName() + " | Age: " + (age != null ? age : "N/A") + " | Gender: " + (getGender() != null ? getGender() : "N/A")
                + " | Position: " + position + " | Department: " + deptName;
    }

    @Override
    public String getDisplayInfo() {
        return toString();
    }
}