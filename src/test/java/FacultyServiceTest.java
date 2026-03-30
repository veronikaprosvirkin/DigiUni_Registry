import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class FacultyServiceTest {
    private University university;
    private FacultyService facultyService;
    private Teacher dummyDean;

    // Init data
    @BeforeEach
    void setUp() {
        university = new University();
        facultyService = new FacultyService(university);
        dummyDean = new Teacher("1", "Tomasz", "Zieliński", "Markow", "Docent", null);

        // Add initial faculty
        facultyService.addNewFaculty("FI", "123-45-67", dummyDean);
    }

    // Test get all faculties
    @Test
    void testGetFaculties() {
        List<Faculty> faculties = facultyService.getFaculties();

        assertEquals(1, faculties.size());
        assertEquals("FI", faculties.get(0).getName());
    }

    // Test add unique faculty
    @Test
    void testAddNewFacultySuccess() {
        facultyService.addNewFaculty("FENS", "987-65-43", null);
        List<Faculty> faculties = facultyService.getFaculties();

        assertEquals(2, faculties.size());
        assertEquals("FENS", faculties.get(1).getName());
    }

    // Test add duplicate faculty
    @Test
    void testAddNewFacultyDuplicate() {
        facultyService.addNewFaculty("FI", "000-00-00", null);
        List<Faculty> faculties = facultyService.getFaculties();

        assertEquals(1, faculties.size()); // Should not add
    }

    // Test delete faculty
    @Test
    void testDeleteFaculty() {
        Faculty faculty = facultyService.getFaculties().get(0);
        facultyService.deleteFaculty(faculty);

        List<Faculty> faculties = facultyService.getFaculties();
        assertEquals(0, faculties.size());
    }

    // Test edit to unique name
    @Test
    void testEditFacultyNameSuccess() {
        Faculty faculty = facultyService.getFaculties().get(0);
        facultyService.editFacultyName(faculty, "FENS");

        assertEquals("FENS", faculty.getName());
    }

    // Test edit to duplicate name
    @Test
    void testEditFacultyNameDuplicate() {
        facultyService.addNewFaculty("FENS", "987-65-43", null);
        Faculty facultyFI = facultyService.getFaculties().get(0); // "FI"

        facultyService.editFacultyName(facultyFI, "FENS");

        assertEquals("FI", facultyFI.getName()); // Should not change
    }
}