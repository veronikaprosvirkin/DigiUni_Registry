public class Student extends Person {
    private int course;
    private int group;
    private String faculty;
    private String speciality;

    // Update constructor to accept context
    public Student(String name, String surname, int course, int group, String faculty, String speciality) {
        super(name, surname);
        this.course = course;
        this.group = group;
        this.faculty = faculty;
        this.speciality = speciality;
    }

    public int getCourse() { return course; }
    public int getGroup() { return group; }

    @Override
    public String toString() {
        return getFullName() + " | Course: " + course + " | Group: " + group +
                " | Faculty: " + faculty + " | Spec: " + speciality;
    }
}