package faculty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import person.Teacher;
import university.University;
import department.Department;
import user.UserService;
import utils.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;

class FacultyServiceTest {

    private University university;
    private FacultyService facultyService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        university = new University();
        facultyService = new FacultyService(university);
        userService = UserService.createTestInstance();
    }

    @Test
    void addNewFaculty_increasesListSize_andStoresFacultyWithGivenProperties() {
        Department dept = new Department(IdGenerator.generateDepartmentId(university), "Dept");
        Teacher dean = new Teacher(IdGenerator.generateTeacherId(university), "A", "B", "", "Prof", dept);

        facultyService.addNewFaculty("Test Faculty", "TF", "contact", dean, userService);
        assertEquals(1, university.getFaculties().size());
        Faculty f = university.getFaculties().get(0);
        assertEquals("Test Faculty", f.getName());
        assertEquals("TF", f.getShortName());
        assertEquals("contact", f.getContacts());
        assertEquals(dean, f.getDean());
    }

    @Test
    void addNewFaculty_duplicateName_isIgnored() {
        Department dept = new Department(IdGenerator.generateDepartmentId(university), "Dept");
        Teacher dean = new Teacher(IdGenerator.generateTeacherId(university), "A", "B", "", "Prof", dept);

        facultyService.addNewFaculty("Dup Faculty", "D1", "c", dean, userService);
        assertEquals(1, university.getFaculties().size());

        // try to add duplicate name (case-insensitive)
        facultyService.addNewFaculty("dup faculty", "D2", "c2", dean, userService);
        assertEquals(1, university.getFaculties().size(), "Duplicate faculty name should not be added");
    }

    @Test
    void deleteFaculty_removesItFromUniversity() {
        Department dept = new Department(IdGenerator.generateDepartmentId(university), "Dept");
        Teacher dean = new Teacher(IdGenerator.generateTeacherId(university), "A", "B", "", "Prof", dept);

        facultyService.addNewFaculty("ToDelete", "TD", "c", dean, userService);
        Faculty f = university.getFaculties().get(0);
        assertEquals(1, university.getFaculties().size());

        facultyService.deleteFaculty(f, userService);
        assertEquals(0, university.getFaculties().size());
    }

    @Test
    void editFacultyName_changesNameWhenUnique_andPreventsDuplicate() {
        Department dept = new Department(IdGenerator.generateDepartmentId(university), "Dept");
        Teacher dean = new Teacher(IdGenerator.generateTeacherId(university), "A", "B", "", "Prof", dept);

        facultyService.addNewFaculty("First", "F1", "c", dean, userService);
        facultyService.addNewFaculty("Second", "F2", "c2", dean, userService);

        Faculty first = university.getFaculties().get(0);
        Faculty second = university.getFaculties().get(1);

        facultyService.editFacultyName(first, "Renamed", userService);
        assertEquals("Renamed", first.getName());

        // try to rename second to the same as first
        facultyService.editFacultyName(second, "Renamed", userService);
        assertEquals("Second", second.getName(), "Rename to duplicate should not be applied");
    }
}

