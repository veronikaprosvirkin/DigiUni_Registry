package person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import university.University;
import faculty.Faculty;
import department.Department;

public class TeacherServiceTest {
    private University university;
    private TeacherService teacherService;
    private Department deptCS;
    private Department deptMath;

    // Init data
    @BeforeEach
    void setUp() {
        university = new University();
        teacherService = new TeacherService(university);

        Faculty faculty = new Faculty("1", "Faculty of Informatics", "FI", "123", null);
        deptCS = new Department("1", "Computer Science");
        deptMath = new Department("2", "Mathematics");

        faculty.getDepartments().add(deptCS);
        faculty.getDepartments().add(deptMath);
        university.getFaculties().add(faculty);

        // Add 2 teachers to CS, 1 to Math
        teacherService.addTeacher("Tomasz", "Zieliński", "Markow", "Docent", deptCS);
        teacherService.addTeacher("Anna", "Nowak", "Janivna", "Prof", deptCS);
        teacherService.addTeacher("Jan", "Kowalski", "Petrov", "Asystent", deptMath);
    }

    // Test add teacher
    @Test
    void testAddTeacher() {
        teacherService.addTeacher("Marek", "Wiśniewski", "Igorov", "Prof", deptCS);
        List<Teacher> csTeachers = teacherService.getTeachersByDepartment(deptCS);

        // 2 initial + 1 new
        assertEquals(3, csTeachers.size());
    }

    // Test delete teacher
    @Test
    void testDeleteTeacher() {
        List<Teacher> csTeachers = teacherService.getTeachersByDepartment(deptCS);
        Teacher toDelete = csTeachers.get(0);

        teacherService.deleteTeacher(toDelete, deptCS);
        csTeachers = teacherService.getTeachersByDepartment(deptCS);

        // 2 initial - 1 deleted
        assertEquals(1, csTeachers.size());
        assertFalse(csTeachers.contains(toDelete));
    }

    // Test get all
    @Test
    void testGetAllTeachers() {
        List<Teacher> allTeachers = teacherService.getAllTeachers();
        assertEquals(3, allTeachers.size());
    }

    // Test find by name
    @Test
    void testFindTeachersByFullName() {
        List<Teacher> found = teacherService.findTeachersByFullName("Tomasz");
        assertEquals(1, found.size());
        assertEquals("Tomasz", found.get(0).getOnlyName());
    }

    // Test get by dept
    @Test
    void testGetTeachersByDepartment() {
        List<Teacher> mathTeachers = teacherService.getTeachersByDepartment(deptMath);
        assertEquals(1, mathTeachers.size());
        assertEquals("Kowalski", mathTeachers.get(0).getSurname());
    }

    @Test
    void testDeleteDeanWithoutDepartment() {
        Faculty faculty = university.getFaculties().get(0);
        Teacher dean = new Teacher("t9999", "Dean", "Test", "X", "Dean", null);
        faculty.setDean(dean);

        teacherService.deleteTeacher(dean);

        assertNull(faculty.getDean());
    }

    @Test
    void testGetAllTeachersDoesNotDuplicateDeanAlsoPresentInDepartment() {
        Faculty faculty = university.getFaculties().get(0);
        Teacher sharedTeacher = deptCS.getTeachers().get(0);
        faculty.setDean(sharedTeacher);

        List<Teacher> allTeachers = teacherService.getAllTeachers();

        assertEquals(3, allTeachers.size());
    }

    @Test
    void testFindTeachersByFullNameDoesNotDuplicateDeanAlsoPresentInDepartment() {
        Faculty faculty = university.getFaculties().get(0);
        Teacher sharedTeacher = deptCS.getTeachers().get(0);
        faculty.setDean(sharedTeacher);

        List<Teacher> found = teacherService.findTeachersByFullName(sharedTeacher.getOnlyName());

        assertEquals(1, found.size());
    }
}