import java.time.LocalDate;

public class Student extends Person {
    private LocalDate enrollmentDate;
    private int group;
    private String faculty;
    private Speciality speciality;
    private String id;
    private StudyForm studyForm;
    private StudentStatus status = StudentStatus.ACTIVE;

    // Update constructor to accept context
    public Student(String id,String name, String surname,String patronymic, LocalDate enrollmentDate, int group, String faculty, Speciality speciality, StudyForm studyForm) {
        super(name, surname, patronymic);
        if (group <= 0)
            throw new IllegalArgumentException("Group number must be greater than 0.");
        this.enrollmentDate = enrollmentDate;
        this.group = group;
        this.faculty = faculty;
        this.speciality = speciality;
        this.id = id;
        this.studyForm = studyForm;

    }
    //count course based on enrollment date
    public int getCourse() {
        LocalDate today = LocalDate.now();
        return today.getYear() - enrollmentDate.getYear()+1;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }



    public String getCourseDisplay() {
        if(getStatus() == StudentStatus.GRADUATED) {
            return "Graduated";
        } else if (getStatus() == StudentStatus.EXPELLED) {
            return "Expelled";
        } else if (getStatus() == StudentStatus.ACADEMIC_LEAVE) {
            return "Academic Leave";
        }
            return String.valueOf(getCourse());
    }

    public int getGroup() { return group; }
    public void setGroup(int group) {this.group = group;}
    public String getFaculty() { return faculty; }
    public Speciality getSpeciality() { return speciality; }

    @Override
    public String toString() {
        return getFullName() + " | Course: " + getCourseDisplay() + " | Study form: " + studyForm + " | Group: " + group +
                " | Faculty: " + faculty + " | Spec: " + speciality.getName();
    }

    @Override
    public String getDisplayInfo(){
        return toString();
    }

    public int getEnrollmentDate(){
        return enrollmentDate.getYear();
    }

    public String getId() { return id; }

    public StudyForm getStudyForm() { return studyForm; }
    
    public void setStudyForm(StudyForm studyForm) { this.studyForm = studyForm; }
    
    public StudentStatus getStatus() {
        if (this.status == StudentStatus.ACTIVE && getCourse() >6 ){
            return StudentStatus.GRADUATED;
        }
        return status;
    }
    
    public void setStatus(StudentStatus status) { this.status = status; }
}