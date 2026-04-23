package faculty;

import department.Department;
import org.junit.jupiter.api.Test;
import person.Teacher;
import university.University;
import utils.IdGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FacultyTest {

    @Test
    void toString_includesDeanWhenPresent_andShowsNotAssignedWhenNull() {
        University university = new University();
        Department dept = new Department(IdGenerator.generateDepartmentId(university), "Dept");
        Teacher dean = new Teacher(IdGenerator.generateTeacherId(university), "John", "Doe", "", "Prof", dept);

        Faculty withDean = new Faculty("f001", "Engineering Faculty", "EF", "Contacts", dean);
        String s = withDean.toString();
        assertTrue(s.contains("[f001]"));
        assertTrue(s.contains("Engineering Faculty"));
        // Teacher.getFullName() returns "surname name"
        assertTrue(s.contains("Doe John"));

        Faculty noDean = new Faculty("f002", "Math Faculty", "MF", "Contacts2", null);
        String s2 = noDean.toString();
        assertTrue(s2.contains("Not assigned"));
    }

    @Test
    void getDisplayInfo_returnsCodeAndName() {
        Faculty f = new Faculty("f010", "Physics", "P", "c", null);
        assertEquals("[Code: f010] Physics", f.getDisplayInfo());
    }

    @Test
    void setName_updatesNameReturnedByGetName() {
        Faculty f = new Faculty("f020", "Old Name", "ON", "c", null);
        assertEquals("Old Name", f.getName());
        f.setName("New Name");
        assertEquals("New Name", f.getName());
    }
}
