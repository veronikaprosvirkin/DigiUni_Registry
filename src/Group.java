import java.util.ArrayList;
import java.util.List;

public class Group {
    private int groupNumber;
    private List<Student> students = new ArrayList<>();

    public Group(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getGroupNumber() { return groupNumber; }


    public List<Student> getStudents() {
        return students;
    }
}