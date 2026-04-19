package person;

import department.Department;
import faculty.Faculty;
import faculty.FacultyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import speciality.Speciality;
import university.University;
import user.UserService;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModStudentUtilsTest {

    private University university;
    private FacultyService facultyService;
    private StudentService studentService;
    private TeacherService teacherService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        university = new University();
        facultyService = new FacultyService(university);
        studentService = new StudentService(university);
        teacherService = new TeacherService(university);
        userService = UserService.createTestInstance();

        Faculty faculty = new Faculty("f-1", "Faculty of Informatics", "FI", "contact", null);
        Speciality speciality = new Speciality("sp-1", "Software Engineering");
        faculty.getSpeciality().add(speciality);
        faculty.getDepartments().add(new Department("d-1", "Computer Science"));
        university.getFaculties().add(faculty);
    }

    @Test
    void studentAddStudent_withBlankEmailGeneratesUkmaDomainEmail() {
        Scanner scanner = scannerFromLines(
                "1",                // faculty
                "1",                // speciality
                "John",             // name
                "Doe",              // surname
                "Smith",            // patronymic
                "2026",             // enrollment year
                "101",              // group
                "1",                // study form (budget)
                "",                 // email prefix -> auto-generate
                "",                 // phone
                "",                 // date of birth
                ""                  // pause
        );

        ModStudentUtils.studentAddStudent(scanner, facultyService, studentService, university, userService, teacherService);

        assertFalse(studentService.getAllStudents().isEmpty());
        Student added = studentService.getAllStudents().get(0);
        assertEquals("j.doe@ukma.edu.ua", added.getEmail());
        assertTrue(added.getEmail().endsWith("@ukma.edu.ua"));
    }

    private Scanner scannerFromLines(String... lines) {
        String joined = String.join("\n", lines) + "\n\n\n\n\n";
        return new Scanner(new ByteArrayInputStream(joined.getBytes(StandardCharsets.UTF_8)));
    }
}


