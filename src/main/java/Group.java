import lombok.Data;
import java.util.ArrayList;
import java.util.List;

// Group entity
@Data
public class Group {
    private int groupNumber;
    private List<Student> students = new ArrayList<>();

    public Group(int groupNumber) {
        this.groupNumber = groupNumber;
    }
}