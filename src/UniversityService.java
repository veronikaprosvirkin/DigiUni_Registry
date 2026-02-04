import java.util.ArrayList;
import java.util.List;

public class UniversityService {
    private University university;

    public UniversityService() {
        this.university = new University();
        initializeStructure();
    }

    // Creating base structure: Faculty-Department
    private void initializeStructure() {
        Faculty fi = new Faculty("FI");
        Department se = new Department("Software Engineering");

        fi.getDepartments().add(se);
        university.getFaculties().add(fi);
    }

    // Adding a student
    public void addStudent(String name, int course, int group) {
        Student newStudent = new Student(name, course, group);

        // Deciding where to sign (first Faculty and first Department)
        if (!university.getFaculties().isEmpty() &&
                !university.getFaculties().get(0).getDepartments().isEmpty()) {

            university.getFaculties().get(0)
                    .getDepartments().get(0)
                    .getStudents().add(newStudent);
        } else {
            System.out.println("Error: No department found to add student!");
        }
    }

    /** ===== SEARCH ===== **/
    // search all students
    public List<Student> getAllStudents() {
        List<Student> allStudents = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Department dept : faculty.getDepartments()) {
                allStudents.addAll(dept.getStudents());
            }
        }
        return allStudents;
    }

    // Search by name
    public List<Student> findStudentsByName(String namePart) {
        List<Student> result = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Department dept : faculty.getDepartments()) {
                for (Student s : dept.getStudents()) {
                    if (s.getName().toLowerCase().contains(namePart.toLowerCase())) {
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }

    // Search by group
    public List<Student> findStudentsByGroup(int group) {
        List<Student> result = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Department dept : faculty.getDepartments()) {
                for (Student s : dept.getStudents()) {
                    if (s.getGroup() == group) {
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }

    // Search by Course
    public List<Student> findStudentsByCourse(int course) {
        List<Student> result = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Department dept : faculty.getDepartments()) {
                for (Student s : dept.getStudents()) {
                    if (s.getCourse() == course) {
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }
}