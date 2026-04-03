import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DepartmentServiceTest {
    private University university;
    private DepartmentService departmentService;
    private Faculty faculty;
    private Department dept1;
    private Department dept2;

    // Init data
    @BeforeEach
    void setUp() {
        university = new University();
        departmentService = new DepartmentService(university);

        faculty = new Faculty("1", "Faculty of Informatics", "FI", "123", null);
        dept1 = new Department("1", "Computer Science");
        dept2 = new Department("2", "Mathematics");

        faculty.getDepartments().add(dept1);
        faculty.getDepartments().add(dept2);
        university.getFaculties().add(faculty);
    }

    // Test get departments
    @Test
    void testGetDepartments() {
        List<Department> departments = departmentService.getDepartments(faculty);
        assertEquals(2, departments.size());
    }

    // Test add unique dept
    @Test
    void testAddNewDepartmentSuccess() {
        departmentService.addNewDepartment("Physics", faculty);
        List<Department> departments = departmentService.getDepartments(faculty);

        assertEquals(3, departments.size());
        assertEquals("Physics", departments.get(2).getName());
    }

    // Test add duplicate dept
    @Test
    void testAddNewDepartmentDuplicate() {
        departmentService.addNewDepartment("Computer Science", faculty);
        List<Department> departments = departmentService.getDepartments(faculty);

        assertEquals(2, departments.size()); // Size should not change
    }

    // Test edit to unique name
    @Test
    void testEditDepartmentNameSuccess() {
        departmentService.editDepartmentName(dept1, "Applied CS", faculty);
        assertEquals("Applied CS", dept1.getName());
    }

    // Test edit to duplicate name
    @Test
    void testEditDepartmentNameDuplicate() {
        departmentService.editDepartmentName(dept1, "Mathematics", faculty);
        assertEquals("Computer Science", dept1.getName()); // Should not change
    }

    // Test delete dept
    @Test
    void testDeleteDepartment() {
        departmentService.deleteDepartment(dept1, faculty);
        List<Department> departments = departmentService.getDepartments(faculty);

        assertEquals(1, departments.size());
        assertFalse(departments.contains(dept1));
    }
}