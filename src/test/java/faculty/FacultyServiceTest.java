package faculty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import person.Teacher;
import university.University;
import department.Department;
import person.TeacherService;
import utils.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;

class FacultyServiceTest {

    private University university;
    private FacultyService facultyService;

    @BeforeEach
    void setUp() {
        university = new University();
        facultyService = new FacultyService(university);
    }

    @Test
    void addNewFaculty_increasesListSize_andStoresFacultyWithGivenProperties() {
        Department dept = new Department(IdGenerator.generateDepartmentId(), "Dept");
        Teacher dean = new Teacher(IdGenerator.generateTeacherId(), "A", "B", "", "Prof", dept);

        facultyService.addNewFaculty("Test Faculty", "TF", "contact", dean);
        assertEquals(1, university.getFaculties().size());
        Faculty f = university.getFaculties().get(0);
        assertEquals("Test Faculty", f.getName());
        assertEquals("TF", f.getShortName());
        assertEquals("contact", f.getContacts());
        assertEquals(dean, f.getDean());
    }

    @Test
    void addNewFaculty_duplicateName_isIgnored() {
        Department dept = new Department(IdGenerator.generateDepartmentId(), "Dept");
        Teacher dean = new Teacher(IdGenerator.generateTeacherId(), "A", "B", "", "Prof", dept);

        facultyService.addNewFaculty("Dup Faculty", "D1", "c", dean);
        assertEquals(1, university.getFaculties().size());

        // try to add duplicate name (case-insensitive)
        facultyService.addNewFaculty("dup faculty", "D2", "c2", dean);
        assertEquals(1, university.getFaculties().size(), "Duplicate faculty name should not be added");
    }

    @Test
    void deleteFaculty_removesItFromUniversity() {
        Department dept = new Department(IdGenerator.generateDepartmentId(), "Dept");
        Teacher dean = new Teacher(IdGenerator.generateTeacherId(), "A", "B", "", "Prof", dept);

        facultyService.addNewFaculty("ToDelete", "TD", "c", dean);
        Faculty f = university.getFaculties().get(0);
        assertEquals(1, university.getFaculties().size());

        facultyService.deleteFaculty(f);
        assertEquals(0, university.getFaculties().size());
    }

    @Test
    void editFacultyName_changesNameWhenUnique_andPreventsDuplicate() {
        Department dept = new Department(IdGenerator.generateDepartmentId(), "Dept");
        Teacher dean = new Teacher(IdGenerator.generateTeacherId(), "A", "B", "", "Prof", dept);

        facultyService.addNewFaculty("First", "F1", "c", dean);
        facultyService.addNewFaculty("Second", "F2", "c2", dean);

        Faculty first = university.getFaculties().get(0);
        Faculty second = university.getFaculties().get(1);

        facultyService.editFacultyName(first, "Renamed");
        assertEquals("Renamed", first.getName());

        // try to rename second to the same as first
        facultyService.editFacultyName(second, "Renamed");
        assertEquals("Second", second.getName(), "Rename to duplicate should not be applied");
    }
}

