import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UniversityService {
    // Creating students and teacher lists
    private List<Student> students = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();

    // Adding a student to students list
    public void addStudent(Student student) {
        students.add(student);
    }

    // Adding a teacher to teachers list
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    // Search student by name
    public List<Student> findStudentsByName(String namePart) {
        if (namePart == null || namePart.isBlank()) return new ArrayList<>();

        return students.stream()
                .filter(s -> s.getName() != null)
                .filter(s -> s.getName().toLowerCase().contains(namePart.toLowerCase()))
                .toList();
    }

    // Search student by group
    public List<Student> findStudentsByGroup(int groupNumber) {
        return students.stream()
                .filter(s -> s.getGroup() == groupNumber)
                .toList();
    }

    // Search student by faculty
    public List<Student> findStudentsByFaculty(String facultyName) {
        return students.stream()
                .filter(s -> s.getFaculty() != null)
                .filter(s -> s.getFaculty().equalsIgnoreCase(facultyName))
                .toList();
    }

    // Search teacher by name
    public List<Teacher> findTeachersByName(String namePart) {
        if (namePart == null || namePart.isBlank()) return new ArrayList<>();

        return teachers.stream()
                .filter(t -> t.getName() != null)
                .filter(t -> t.getName().toLowerCase().contains(namePart.toLowerCase()))
                .toList();
    }

    // Method to show all students as a list
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // a copy not to ruin the original
    }
}