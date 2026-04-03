package speciality;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import person.Student;

// Group entity
@Data
public class Group {
    private String id;
    private int groupNumber;
    private List<Student> students = new ArrayList<>();

    public Group(int groupNumber) {
        this.groupNumber = groupNumber;
    }
}