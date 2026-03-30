import lombok.Data;
import lombok.EqualsAndHashCode;

// Teacher entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Teacher extends Person {
    private String position;
    private Department department;

    public Teacher(String id, String name, String surname, String patronymic, String position, Department department) {
        super(id, name, surname, patronymic);
        this.position = position;
        this.department = department;
    }

    @Override
    public String toString() {
        String deptName = (this.department != null) ? this.department.getName() : "No Department Assigned";
        return getFullName() + " | Position: " + position + " | Department: " + deptName;
    }

    @Override
    public String getDisplayInfo() {
        return toString();
    }
}