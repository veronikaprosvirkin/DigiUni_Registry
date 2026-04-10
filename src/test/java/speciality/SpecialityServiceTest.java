package speciality;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import university.University;
import faculty.Faculty;
import user.UserService;

public class SpecialityServiceTest {
    private SpecialityService specialityService;
    private Faculty faculty;
    private Speciality spec1;
    private UserService userService;

    // Init data
    @BeforeEach
    void setUp() {
        University university = new University();
        specialityService = new SpecialityService(university);
        userService = UserService.createTestInstance();

        faculty = new Faculty("1", "Faculty of Informatics", "FI", "123", null);
        spec1 = new Speciality("1", "Software Engineering");
        Speciality spec2 = new Speciality("2", "Computer Science");

        faculty.getSpeciality().add(spec1);
        faculty.getSpeciality().add(spec2);
        university.getFaculties().add(faculty);
    }

    // Test add unique spec
    @Test
    void testAddNewSpecialitySuccess() {
        specialityService.addNewSpeciality("Cybersecurity", faculty, userService);

        assertEquals(3, faculty.getSpeciality().size());
        assertEquals("Cybersecurity", faculty.getSpeciality().get(2).getName());
    }

    // Test add duplicate spec
    @Test
    void testAddNewSpecialityDuplicate() {
        specialityService.addNewSpeciality("Software Engineering", faculty, userService);

        assertEquals(2, faculty.getSpeciality().size()); // Size should not change
    }

    // Test edit to unique name
    @Test
    void testEditSpecialityNameSuccess() {
        specialityService.editSpecialityName(spec1, "Applied SE", faculty, userService);
        assertEquals("Applied SE", spec1.getName());
    }

    // Test edit to duplicate name
    @Test
    void testEditSpecialityNameDuplicate() {
        specialityService.editSpecialityName(spec1, "Computer Science", faculty, userService);
        assertEquals("Software Engineering", spec1.getName()); // Should not change
    }

    // Test delete spec
    @Test
    void testDeleteSpeciality() {
        specialityService.deleteSpeciality(spec1, faculty, userService);

        assertEquals(1, faculty.getSpeciality().size());
        assertFalse(faculty.getSpeciality().contains(spec1));
    }
}