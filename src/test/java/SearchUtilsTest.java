import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

public class SearchUtilsTest {
    private University university;
    private StudentService studentService;
    private TeacherService teacherService;
    private FacultyService facultyService;

    // Init data
    @BeforeEach
    void setUp() {
        university = new University();
        UniversityService us = new UniversityService(university);
        studentService = new StudentService(university);
        teacherService = new TeacherService(university);
        facultyService = new FacultyService(university);

        studentService.addStudent("Piotr", "Kamiński", LocalDate.of(2024, 9, 1), 101, StudyForm.BUDGET);
        studentService.addStudent("Piotr", "Lewandowski", LocalDate.of(2025, 9, 1), 102, StudyForm.CONTRACT);

        Faculty f = university.getFaculties().get(0);
        Department d = f.getDepartments().get(0);
        teacherService.addTeacher("Tomasz", "Zieliński", "Docent", d);
        teacherService.addTeacher("Tomasz", "Szymański", "Prof", d);
    }

    // Mock scanner input
    private Scanner getScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    // Test search student by name
    @Test
    void testSearchStudentByName() {
        // "Piotr" -> Name, "1" -> Sort, "\n" -> Pause
        Scanner scanner = getScanner("Piotr\n1\n\n");
        SearchUtils.searchStudentByName(scanner, studentService);
    }

    // Test search student by specific group
    @Test
    void testSearchStudentByGroupSpecific() {
        // "1" -> Faculty FI, "1" -> SE Spec, "101" -> group, "1" -> sort, "\n" -> pause
        Scanner scanner = getScanner("1\n1\n101\n1\n\n");
        SearchUtils.searchStudentByGroupSpecific(scanner, facultyService, studentService);
    }

    // Test search student by group everywhere
    @Test
    void testSearchStudentByGroupEverywhere() {
        // "102" -> group, "1" -> sort, "\n" -> pause
        Scanner scanner = getScanner("102\n1\n\n");
        SearchUtils.searchStudentByGroupEverywhere(scanner, studentService);
    }

    // Test search student by course
    @Test
    void testSearchStudentByCourse() {
        // "2" -> course, "1" -> sort, "\n" -> pause
        Scanner scanner = getScanner("2\n1\n\n");
        SearchUtils.searchStudentByCourse(scanner, studentService);
    }

    // Test search student by speciality
    @Test
    void testSearchStudentBySpeciality() {
        // "1" -> Faculty FI, "1" -> SE Spec, "1" -> sort, "\n" -> pause
        Scanner scanner = getScanner("1\n1\n1\n\n");
        SearchUtils.searchStudentBySpeciality(scanner, studentService, facultyService);
    }

    // Test search teacher by name
    @Test
    void testSearchTeacherByName() {
        // "Tomasz" -> Name, "1" -> Sort, "\n" -> Pause
        Scanner scanner = getScanner("Tomasz\n1\n\n");
        SearchUtils.searchTeacherByName(scanner, teacherService);
    }

    // Test search teacher by position
    @Test
    void testSearchTeacherByPosition() {
        // "Docent" -> Position, "1" -> Sort, "\n" -> Pause
        Scanner scanner = getScanner("Docent\n1\n\n");
        SearchUtils.searchTeacherByPosition(scanner, teacherService);
    }

    // Test search teacher by department
    @Test
    void testSearchTeacherByDepartment() {
        // "1" -> Faculty FI, "1" -> CS Dept, "1" -> Sort, "\n" -> Pause
        Scanner scanner = getScanner("1\n1\n1\n\n");
        SearchUtils.searchTeacherByDepartment(scanner, facultyService, teacherService);
    }
}