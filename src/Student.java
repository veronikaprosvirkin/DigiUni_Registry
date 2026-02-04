public class Student extends Person {
    private int course;
    private int group;

    public Student(String name, int course, int group) {
        super(name);
        this.course = course;
        this.group = group;
    }

    public int getCourse() { return course; }
    public int getGroup() { return group; }

    @Override
    public String toString() {
        return getName() + " (Course: " + course + ", Group: " + group + ")";
    }
}