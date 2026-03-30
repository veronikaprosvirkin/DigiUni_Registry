import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

// Student entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Student extends Person {
    private LocalDate enrollmentDate;
    private int group;
    private String faculty;
    private Speciality speciality;
    private StudyForm studyForm;
    private StudentStatus status = StudentStatus.ACTIVE;

    public Student(String id, String name, String surname, String patronymic, LocalDate enrollmentDate, int group, String faculty, Speciality speciality, StudyForm studyForm) {
        super(id, name, surname, patronymic);
        if (group <= 0) throw new IllegalArgumentException("Group number must be greater than 0.");
        this.enrollmentDate = enrollmentDate;
        this.group = group;
        this.faculty = faculty;
        this.speciality = speciality;
        this.studyForm = studyForm;
    }

    // Calculate course
    public int getCourse() {
        return LocalDate.now().getYear() - enrollmentDate.getYear() + 1;
    }

    // Format course output
    public String getCourseDisplay() {
        if (status == StudentStatus.GRADUATED) return "Graduated";
        if (status == StudentStatus.EXPELLED) return "Expelled";
        if (status == StudentStatus.ACADEMIC_LEAVE) return "Academic Leave";
        return String.valueOf(getCourse());
    }

    // Get status with auto-graduation
    public StudentStatus getStatus() {
        if (this.status == StudentStatus.ACTIVE && getCourse() > 6) {
            return StudentStatus.GRADUATED;
        }
        return status;
    }

    @Override
    public String toString() {
        return getFullName() + " | Course: " + getCourseDisplay() + " | Study form: " + studyForm + " | Group: " + group + " | Faculty: " + faculty + " | Spec: " + speciality.getName();
    }

    @Override
    public String getDisplayInfo() {
        return toString();
    }
}