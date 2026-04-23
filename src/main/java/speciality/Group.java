package speciality;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import person.Student;

// Group entity
@Data
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private int groupNumber;
    private List<Student> students = new ArrayList<>();

    public Group(int groupNumber) {
        this.groupNumber = groupNumber;
    }
}