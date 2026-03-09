import java.time.LocalDate;

public class Student extends Person {
    private LocalDate enrollmentDate;
    private int group;
    private String faculty;
    private Speciality speciality;

    // Update constructor to accept context
    public Student(String name, String surname, LocalDate enrollmentDate, int group, String faculty, Speciality speciality) {
        super(name, surname);
        if (group <= 0)
            throw new IllegalArgumentException("Group number must be greater than 0.");
        this.enrollmentDate = enrollmentDate;
        this.group = group;
        this.faculty = faculty;
        this.speciality = speciality;

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
        int course = getCourse();
        if (course>6){
            return "Student graduated";
        }
        return String.valueOf(course);
    }

    public int getGroup() { return group; }
    public void setGroup(int group) {this.group = group;}
    public String getFaculty() { return faculty; }
    public Speciality getSpeciality() { return speciality; }

    @Override
    public String toString() {
        return getFullName() + " | Course: " + getCourseDisplay() + " | Group: " + group +
                " | Faculty: " + faculty + " | Spec: " + speciality.getName();
    }

    @Override
    public String getDisplayInfo(){
        return toString();
    }

    public int getEnrollmentDate(){
        return enrollmentDate.getYear();
    }
}