package person;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import speciality.Speciality;
import faculty.Faculty;
import utils.validation.MinValue;

// Student entity
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public final class Student extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate enrollmentDate;
    @MinValue(value = 1, message = "Group number must be at least 1")
    private int group;
    private Faculty faculty;
    private Speciality speciality;
    private StudyForm studyForm;
    private StudentStatus status = StudentStatus.ACTIVE;

    public Student(String id, String name, String surname, String patronymic, LocalDate enrollmentDate, int group, Faculty faculty, Speciality speciality, StudyForm studyForm) {
        this(id, name, surname, patronymic, enrollmentDate, group, faculty, speciality, studyForm, null);
    }

    public Student(String id, String name, String surname, String patronymic, LocalDate enrollmentDate, int group, Faculty faculty, Speciality speciality, StudyForm studyForm, LocalDate dateOfBirth) {
        super(id, name, surname, patronymic, dateOfBirth);
        if (group <= 0) throw new IllegalArgumentException("Group number must be greater than 0.");
        this.enrollmentDate = enrollmentDate;
        this.group = group;
        this.faculty = faculty;
        this.speciality = speciality;
        this.studyForm = studyForm;
    }

    // Calculate course
    public int getCourse() {
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        int course = currentYear - enrollmentDate.getYear();

        if (currentMonth >= 9) {
            course++;
        }

        course = Math.max(1, course);
        
        // Auto-graduate if course > 6
        if (course > 6 && this.status == StudentStatus.ACTIVE) {
            this.status = StudentStatus.GRADUATED;
            System.out.println("AUTOMATIC GRADUATION: Student " + this.getFullName() +
                             " (ID: " + this.id + ") has completed more than 6 years of study. Status changed to GRADUATED.");
        }

        return course;
    }

    // Format course output
    public String getCourseDisplay() {
        if (status == StudentStatus.GRADUATED) return "Graduated";
        if (status == StudentStatus.EXPELLED) return "Expelled";
        if (status == StudentStatus.ACADEMIC_LEAVE) return "Academic Leave";
        return String.valueOf(getCourse());
    }
    
    // Setter for status with validation and notification
    public void setStatus(StudentStatus newStatus) {
        if (this.status != newStatus) {
            System.out.println("Student " + this.getFullName() + " status changed from " +
                             this.status + " to " + newStatus);
        }
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return getFullName() + " | Age: " + (age != null ? age : "N/A") + " | Gender: " + (getGender() != null ? getGender() : "N/A")
                + " | Course: " + getCourseDisplay() + " | Study form: " + studyForm + " | Group: " + group
                + " | Faculty: " + faculty.getName() + " | Spec: " + speciality.getName();
    }

    @Override
    public String getDisplayInfo() {
        return toString();
    }
}