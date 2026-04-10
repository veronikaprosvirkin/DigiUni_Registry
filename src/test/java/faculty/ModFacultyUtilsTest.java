package faculty;

import department.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import person.Teacher;
import person.TeacherService;
import university.University;
import user.UserService;
import utils.IdGenerator;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ModFacultyUtilsTest {

    private University university;
    private FacultyService facultyService;
    private TeacherService teacherService;
    private Department dept;

    @BeforeEach
    void setUp() {
        university = new University();
        facultyService = new FacultyService(university);
        teacherService = new TeacherService(university);
        dept = new Department(IdGenerator.generateDepartmentId(), "Dept");
    }

    private Scanner scannerFrom(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void facultyAddFaculty_addsWhenDeanSelectedById() {
        // Create a teacher inside a department of a faculty to be found by TeacherService
        // We need a faculty->department->teacher structure
        Faculty existing = new Faculty(IdGenerator.generateFacultyId(), "Existing", "E", "c", null);
        existing.getDepartments().add(dept);
        university.getFaculties().add(existing);

        Teacher t = new Teacher(IdGenerator.generateTeacherId(), "John", "Smith", "", "Dr", dept);
        // add teacher to department via service
        teacherService.addTeacher(t);
        UserService userService = UserService.createTestInstance();

        // Input sequence:
        // Faculty name
        // short name (blank to accept generated)
        // contact
        // select dean: 1 (search by id)
        // teacher id
        // pause (Enter)
        String input = String.join("\n",
                "New Faculty",
                "",
                "contact",
                "1",
                t.getId(),
                "", // pause
                ""  // extra newline to ensure pause() has input
        );

        Scanner scanner = scannerFrom(input);
        ModFacultyUtils.facultyAddFaculty(scanner, facultyService, teacherService, userService);

        assertEquals(2, university.getFaculties().size());
        Faculty added = university.getFaculties().stream().filter(f -> f.getName().equals("New Faculty")).findFirst().orElse(null);
        assertNotNull(added);
        assertEquals(t, added.getDean());
    }

    @Test
    void facultyManageExistingFacultyEditContacts_updatesContacts() throws Exception {
        Faculty f = new Faculty(IdGenerator.generateFacultyId(), "Name", "S", "old", null);
        university.getFaculties().add(f);
        // build input: new contacts then pause
        // use a contacts string without special symbols (dash is not allowed by InputUtils.readLine)
        String input = String.join("\n", "new contacts", "", "");
        Scanner scanner = scannerFrom(input);

        // facultyManageExistingFacultyEditContacts is private; invoke via reflection
        java.lang.reflect.Method m = ModFacultyUtils.class.getDeclaredMethod("facultyManageExistingFacultyEditContacts", Scanner.class, FacultyService.class, Faculty.class);
        m.setAccessible(true);
        m.invoke(null, scanner, facultyService, f);

        assertEquals("new contacts", f.getContacts());
    }

    @Test
    void facultyManageExistingFacultyDelete_deletesOnYes_andKeepsOnNo() throws Exception {
        Faculty f = new Faculty(IdGenerator.generateFacultyId(), "ToDel", "S", "c", null);
        university.getFaculties().add(f);

        // confirm delete (y)
        Scanner yes = scannerFrom("y\n\n");
        java.lang.reflect.Method del = ModFacultyUtils.class.getDeclaredMethod("facultyManageExistingFacultyDelete",
                Scanner.class, FacultyService.class, Faculty.class, UserService.class);
        del.setAccessible(true);
        del.invoke(null, yes, facultyService, f, UserService.createTestInstance());
        assertEquals(0, university.getFaculties().size());

        // add again and cancel (n)
        Faculty f2 = new Faculty(IdGenerator.generateFacultyId(), "Keep", "K", "c", null);
        university.getFaculties().add(f2);
        Scanner no = scannerFrom("n\n\n");
        del.invoke(null, no, facultyService, f2, UserService.createTestInstance());
        assertEquals(1, university.getFaculties().size());
    }

    @Test
    void facultyManageExistingFacultyRename_updatesName_whenUnique_andPreventsDuplicate() throws Exception {
        Faculty f1 = new Faculty(IdGenerator.generateFacultyId(), "One", "O", "c", null);
        Faculty f2 = new Faculty(IdGenerator.generateFacultyId(), "Two", "T", "c", null);
        university.getFaculties().add(f1);
        university.getFaculties().add(f2);
        UserService userService = UserService.createTestInstance();

        // rename f1 to NewName
        Scanner sc1 = scannerFrom("NewName\n\n");
        java.lang.reflect.Method rename = ModFacultyUtils.class.getDeclaredMethod("facultyManageExistingFacultyRename",
                Scanner.class, FacultyService.class, Faculty.class, UserService.class);
        rename.setAccessible(true);
        rename.invoke(null, sc1, facultyService, f1, userService);
        assertEquals("NewName", f1.getName());

        // attempt to rename f2 to NewName (duplicate) - should be prevented
        Scanner sc2 = scannerFrom("NewName\n\n");
        rename.invoke(null, sc2, facultyService, f2, userService);
        assertEquals("Two", f2.getName());
    }

    @Test
    void facultyManageExistingFacultyRenameShort_updatesShortName() throws Exception {
        Faculty f = new Faculty(IdGenerator.generateFacultyId(), "Name", "OLD", "c", null);
        university.getFaculties().add(f);
        Scanner sc = scannerFrom("NEWSHORT\n\n");
        java.lang.reflect.Method renameShort = ModFacultyUtils.class.getDeclaredMethod("facultyManageExistingFacultyRenameShort",
                Scanner.class, FacultyService.class, Faculty.class);
        renameShort.setAccessible(true);
        renameShort.invoke(null, sc, facultyService, f);
        assertEquals("NEWSHORT", f.getShortName());
    }
}






