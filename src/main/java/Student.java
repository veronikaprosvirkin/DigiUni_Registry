public class Student extends Person {
    private int course;
    private int group;
    private String faculty;
    private Speciality speciality;

    // Update constructor to accept context
    public Student(String name, String surname, int course, int group, String faculty, Speciality speciality) {
        super(name, surname);
        if (group <= 0)
            throw new IllegalArgumentException("Group number must be greater than 0.");
        this.course = course;
        this.group = group;
        this.faculty = faculty;
        this.speciality = speciality;

    }

    public int getCourse() { return course; }
    public int getGroup() { return group; }
    public void setGroup(int group) {this.group = group;}
    public String getFaculty() { return faculty; }
    public Speciality getSpeciality() { return speciality; }

    @Override
    public String toString() {
        return getFullName() + " | Course: " + course + " | Group: " + group +
                " | Faculty: " + faculty + " | Spec: " + speciality.getName();
    }

    @Override
    public String getDisplayInfo(){
        return toString();
    }

    public void setCourse(int newCourse) {this.course = newCourse;}
}