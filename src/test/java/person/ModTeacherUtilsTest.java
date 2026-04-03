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
import utils.EntityNotFoundException;

class ModTeacherUtilsTest {

    private University university;
    private FacultyService facultyService;
    private TeacherService teacherService;
    private Faculty faculty;
    private Department deptCs;
    private Department deptMath;
    private Teacher teacherCs;
    private Teacher teacherMath;

    @BeforeEach
    void setUp() {
        university = new University();
        facultyService = new FacultyService(university);
        teacherService = new TeacherService(university);

        faculty = new Faculty("F-1", "Faculty of Informatics", "FI", "contact", null);
        deptCs = new Department("D-1", "Computer Science");
        deptMath = new Department("D-2", "Mathematics");
        faculty.getDepartments().add(deptCs);
        faculty.getDepartments().add(deptMath);
        university.getFaculties().add(faculty);

        teacherCs = new Teacher("t0001", "Ivan", "Petrenko", "Ivanovych", "Lecturer", deptCs);
        teacherMath = new Teacher("t0002", "Oleh", "Shevchenko", "Olehych", "Professor", deptMath);
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
                "Assistant",        // position
                "",                 // email
                "",                 // phone
                "",                 // academic degree
                "",                 // academic title
                "",                 // employment date (blank)
                "",                 // workload
                ""                  // pause
        );

        // When
        ModTeacherUtils.teacherAddTeacher(scanner, facultyService, teacherService);

        // Then
        assertEquals(2, deptCs.getTeachers().size());
        Teacher added = deptCs.getTeachers().get(1);
        assertNotNull(added.getEmploymentDate());
        assertEquals(LocalDate.now(), added.getEmploymentDate());
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
                "Assistant",
                "a@b.com",
                "12345",
                "PhD",
                "Docent",
                "not-a-date",       // invalid date
                "not-a-number",     // invalid workload
                ""                  // pause
        );

        // When
        ModTeacherUtils.teacherAddTeacher(scanner, facultyService, teacherService);

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
                () -> ModTeacherUtils.teacherAddTeacher(scanner, facultyService, teacherService));
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
        ModTeacherUtils.teacherDeleteById(scanner, teacherService);

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
        ModTeacherUtils.teacherEditByName(scanner, teacherService);

        // Then
        assertEquals("Kovalenko", teacherCs.getSurname());
    }

    @Test
    void teacherEditByName_noMatch_keepsStateUnchanged() {
        // Given
        Scanner scanner = scannerFromLines("Unknown Person");
        String originalSurname = teacherCs.getSurname();

        // When
        ModTeacherUtils.teacherEditByName(scanner, teacherService);

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
        ModTeacherUtils.teacherEditById(scanner, teacherService);

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
        ModTeacherUtils.showTeacherMenu(scanner, teacherService, facultyService, null, true);

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
        ModTeacherUtils.showTeacherMenu(scanner, teacherService, facultyService, null, true);

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
                "Senior Lecturer",  // new position
                "0"                 // finish
        );

        // When
        ModTeacherUtils.showTeacherMenu(scanner, teacherService, facultyService, null, true);

        // Then
        assertEquals("Senior Lecturer", teacherCs.getPosition());
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

