package person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import university.University;
import faculty.FacultyService;
import faculty.Faculty;
import department.Department;
import user.UserService;
import utils.EntityNotFoundException;

class ModTeacherUtilsTest {

    private University university;
    private FacultyService facultyService;
    private TeacherService teacherService;
    private StudentService studentService;
    private UserService userService;
    private Department deptCs;
    private Teacher teacherCs;

    @BeforeEach
    void setUp() {
        university = new University();
        facultyService = new FacultyService(university);
        teacherService = new TeacherService(university);
        studentService = new StudentService(university);
        userService = UserService.createTestInstance();

        Faculty faculty = new Faculty("F-1", "Faculty of Informatics", "FI", "contact", null);
        deptCs = new Department("D-1", "Computer Science");
        Department deptMath = new Department("D-2", "Mathematics");
        faculty.getDepartments().add(deptCs);
        faculty.getDepartments().add(deptMath);
        university.getFaculties().add(faculty);

        teacherCs = new Teacher("t0001", "Ivan", "Petrenko", "Ivanovych", "Lecturer", deptCs);
        Teacher teacherMath = new Teacher("t0002", "Oleh", "Shevchenko", "Olehych", "Professor", deptMath);
        teacherService.addTeacher(teacherCs);
        teacherService.addTeacher(teacherMath);
    }

    @Test
    void teacherAddTeacher_withBlankEmploymentDate_setsCurrentDate() {
        // Given
        Scanner scanner = scannerFromLines(
                "1",                // faculty
                "1",                // department
                "John",             // name
                "Doe",              // surname
                "Smith",            // patronymic
                "8",                // position (Assistant)
                "",                 // email
                "",                 // phone
                "",                 // academic degree
                "",                 // academic title
                "",                 // employment date (blank)
                "",                 // workload
                "",                 // date of birth
                ""                  // pause
        );

        // When
        ModTeacherUtils.teacherAddTeacher(scanner, facultyService, teacherService, university, userService, studentService);

        // Then
        Teacher added = teacherService.findTeachersByFullName("Doe John").get(0);
        assertNotNull(added.getEmploymentDate());
        assertEquals(LocalDate.now(), added.getEmploymentDate());
        assertTrue(added.getEmail().endsWith("@ukma.edu.ua"));
    }

    @Test
    void teacherAddTeacher_withInvalidDateAndWorkload_skipsBothFields() {
        // Given
        Scanner scanner = scannerFromLines(
                "1",
                "1",
                "Alice",
                "Brown",
                "Pat",
                "8",                // position (Assistant)
                "a@b.com",
                "12345",
                "PhD",
                "Docent",
                "not-a-date",       // invalid date
                "not-a-number",     // invalid workload
                "",                 // date of birth
                ""                  // pause
        );

        // When
        ModTeacherUtils.teacherAddTeacher(scanner, facultyService, teacherService, university, userService, studentService);

        // Then
        Teacher added = deptCs.getTeachers().get(1);
        assertNull(added.getEmploymentDate());
        assertEquals(0.0, added.getWorkload(), 1e-9);
    }

    @Test
    void teacherAddTeacher_whenFacultyNotSelected_throwsEntityNotFoundException() {
        // Given
        Scanner scanner = scannerFromLines("0"); // cancel faculty selection

        // When + Then
        assertThrows(EntityNotFoundException.class,
                () -> ModTeacherUtils.teacherAddTeacher(scanner, facultyService, teacherService, university, userService, studentService));
    }

    @Test
    void teacherDeleteById_existingTeacher_deletesTeacher() {
        // Given
        Scanner scanner = scannerFromLines(
                "t0001",            // teacher ID
                "y",                // confirm delete
                ""                  // pause
        );

        // When
        ModTeacherUtils.teacherDeleteById(scanner, teacherService, university, userService);

        // Then
        assertEquals(0, deptCs.getTeachers().size());
    }

    @Test
    void teacherEditByName_singleMatch_updatesSurname() {
        // Given
        Scanner scanner = scannerFromLines(
                "Petrenko",         // name part
                "1",                // change surname
                "Kovalenko",        // new surname
                "0"                 // finish editing
        );

        // When
        ModTeacherUtils.teacherEditByName(scanner, teacherService, university, userService);

        // Then
        assertEquals("Kovalenko", teacherCs.getSurname());
    }

    @Test
    void teacherEditByName_noMatch_keepsStateUnchanged() {
        // Given
        Scanner scanner = scannerFromLines("Unknown Person");
        String originalSurname = teacherCs.getSurname();

        // When
        ModTeacherUtils.teacherEditByName(scanner, teacherService, university, userService);

        // Then
        assertEquals(originalSurname, teacherCs.getSurname());
    }

    @Test
    void teacherEditById_invalidDate_keepsOldEmploymentDate() {
        // Given
        teacherCs.setEmploymentDate(LocalDate.of(2020, 1, 1));
        Scanner scanner = scannerFromLines(
                "t0001",            // id
                "8",                // change employment date
                "bad-date",         // invalid date
                "0"                 // finish
        );

        // When
        ModTeacherUtils.teacherEditById(scanner, teacherService, university, userService);

        // Then
        assertEquals(LocalDate.of(2020, 1, 1), teacherCs.getEmploymentDate());
    }

    @Test
    void showTeacherMenu_option4_showAllWithSorting_keepsTeachersCount() {
        // Given
        Scanner scanner = scannerFromLines(
                "4",                // show all
                "1",                // sort by full name
                ""                  // pause
        );

        // When
        ModTeacherUtils.showTeacherMenu(scanner, teacherService, facultyService, userService, true, university, studentService);

        // Then
        assertEquals(2, teacherService.getAllTeachers().size());
    }

    @Test
    void showTeacherMenu_option2_deleteById_removesTeacher() {
        // Given
        Scanner scanner = scannerFromLines(
                "2",                // delete teacher
                "2",                // delete by id
                "t0001",            // id
                "y",                // confirm
                ""                  // pause
        );

        // When
        ModTeacherUtils.showTeacherMenu(scanner, teacherService, facultyService, userService, true, university, studentService);

        // Then
        assertTrue(teacherService.findTeacherById("t0001").isEmpty());
    }

    @Test
    void showTeacherMenu_option3_editById_updatesPosition() {
        // Given
        Scanner scanner = scannerFromLines(
                "3",                // edit teacher
                "2",                // by id
                "t0001",            // id
                "3",                // change position
                "6",                // new position (Senior Lecturer)
                "0"                 // finish
        );

        // When
        ModTeacherUtils.showTeacherMenu(scanner, teacherService, facultyService, userService, true, university, studentService);

        // Then
        assertEquals(Position.SENIOR_LECTURER, teacherCs.getPosition());
    }

    @Test
    void searchTeacherMenu_option1_searchByName_executesWithoutStateChange() {
        // Given
        Scanner scanner = scannerFromLines(
                "1",                // search by full name
                "Petrenko",         // query
                ""                  // pause
        );
        int countBefore = teacherService.getAllTeachers().size();

        // When
        ModTeacherUtils.searchTeacherMenu(scanner, teacherService, facultyService, null, true);

        // Then
        assertEquals(countBefore, teacherService.getAllTeachers().size());
    }

    @Test
    void searchTeacherMenu_option2_searchByDepartment_executesWithoutStateChange() {
        // Given
        Scanner scanner = scannerFromLines(
                "2",                // search by department
                "1",                // faculty
                "1",                // department
                ""                  // pause
        );
        int countBefore = teacherService.getAllTeachers().size();

        // When
        ModTeacherUtils.searchTeacherMenu(scanner, teacherService, facultyService, null, true);

        // Then
        assertEquals(countBefore, teacherService.getAllTeachers().size());
    }

    @Test
    void searchTeacherMenu_option3_searchByPosition_executesWithoutStateChange() {
        // Given
        Scanner scanner = scannerFromLines(
                "3",                // search by position
                "Lecturer",         // query
                ""                  // pause
        );
        int countBefore = teacherService.getAllTeachers().size();

        // When
        ModTeacherUtils.searchTeacherMenu(scanner, teacherService, facultyService, null, true);

        // Then
        assertEquals(countBefore, teacherService.getAllTeachers().size());
    }

    @Test
    void searchTeacherMenu_option4_showAll_executesWithSorting() {
        // Given
        Scanner scanner = scannerFromLines(
                "4",                // show all
                "2",                // sort by position
                ""                  // pause
        );

        // When
        ModTeacherUtils.searchTeacherMenu(scanner, teacherService, facultyService, null, true);

        // Then
        assertEquals(2, teacherService.getAllTeachers().size());
    }

    private Scanner scannerFromLines(String... lines) {
        // Add a few trailing blank lines to make interactive test scripts robust
        // against extra prompt reads in validation loops.
        String joined = String.join("\n", lines) + "\n\n\n\n\n";
        return new Scanner(new ByteArrayInputStream(joined.getBytes(StandardCharsets.UTF_8)));
    }
}

