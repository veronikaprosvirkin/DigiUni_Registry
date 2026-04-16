package person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import department.Department;

// Teacher entity
@Data
@EqualsAndHashCode(callSuper = true)
public final class Teacher extends Person {
    private Position position;
    private Department department;
    private String academicDegree;
    private String academicTitle;
    private LocalDate employmentDate;
    private double workload;

    public Teacher(String id, String name, String surname, String patronymic, Position position, Department department) {
        this(id, name, surname, patronymic, position, department, null);
    }

    public Teacher(String id, String name, String surname, String patronymic, Position position, Department department, LocalDate dateOfBirth) {
        super(id, name, surname, patronymic, dateOfBirth);
        this.position = position;
        this.department = department;
    }

    @Override
    public String toString() {
        String deptName = (this.department != null) ? this.department.getName() : "No Department Assigned";
        return getFullName() + " | Age: " + (age != null ? age : "N/A") + " | Position: " + position + " | Department: " + deptName;
    }

    @Override
    public String getDisplayInfo() {
        return toString();
    }
}