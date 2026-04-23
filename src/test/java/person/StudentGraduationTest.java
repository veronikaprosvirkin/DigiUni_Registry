package person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import faculty.Faculty;
import speciality.Speciality;
import university.University;
import utils.IdGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentGraduationTest {

    private University university;
    private Student studentOldEnrollment;
    private Faculty testFaculty;
    private Speciality testSpeciality;

    @BeforeEach
    void setUp() {
        university = new University();
        testFaculty = new Faculty(IdGenerator.generateFacultyId(university), "Engineering", "ENG", "contacts", null);
        university.getFaculties().add(testFaculty);
        testSpeciality = new Speciality(IdGenerator.generateSpecialityId(university), "Computer Science");
        testFaculty.getSpeciality().add(testSpeciality);

        // Create student enrolled 7+ years ago (should graduate)
        LocalDate pastEnrollmentDate = LocalDate.now().minusYears(8);
        studentOldEnrollment = new Student(
            IdGenerator.generateStudentId(university, pastEnrollmentDate.getYear()),
            "John",
            "Doe",
            "Jr",
            pastEnrollmentDate,
            1,
            testFaculty,
            testSpeciality,
            StudyForm.BUDGET
        );
    }

    @Test
    void testStudentWithCourseGreaterThan6ShouldGraduate() {
        // Trigger getCourse() which should auto-graduate
        int course = studentOldEnrollment.getCourse();
        
        // Course should be calculated
        assertTrue(course > 6, "Course should be > 6 for student enrolled 7+ years ago");
        
        // Status should be GRADUATED
        assertEquals(StudentStatus.GRADUATED, studentOldEnrollment.getStatus(), 
            "Student should be marked as GRADUATED when course > 6");
    }

    @Test
    void testGraduatedStudentDisplaysCourseAsGraduated() {
        // Trigger graduation
        studentOldEnrollment.getCourse();
        
        // getCourseDisplay() should show "Graduated"
        assertEquals("Graduated", studentOldEnrollment.getCourseDisplay(),
            "Graduated student should display 'Graduated' instead of course number");
    }

    @Test
    void testNewStudentShouldStayActive() {
        // Create student enrolled recently (1 year ago)
        LocalDate recentEnrollmentDate = LocalDate.now().minusYears(1);
        Student newStudent = new Student(
            IdGenerator.generateStudentId(university, recentEnrollmentDate.getYear()),
            "Jane",
            "Smith",
            "Jr",
            recentEnrollmentDate,
            1,
            testFaculty,
            testSpeciality,
            StudyForm.BUDGET
        );
        
        // Course should be 1-2
        int course = newStudent.getCourse();
        assertTrue(course <= 2, "Recently enrolled student should be on course 1-2");
        
        // Status should remain ACTIVE
        assertEquals(StudentStatus.ACTIVE, newStudent.getStatus(),
            "New student should remain ACTIVE");
    }

    @Test
    void testStatusChangeNotification() {
        // Create student in academic leave
        LocalDate recentEnrollmentDate = LocalDate.now().minusYears(1);
        Student student = new Student(
            IdGenerator.generateStudentId(university, recentEnrollmentDate.getYear()),
            "Bob",
            "Johnson",
            null,
            recentEnrollmentDate,
            1,
            testFaculty,
            testSpeciality,
            StudyForm.BUDGET
        );
        
        student.setStatus(StudentStatus.ACADEMIC_LEAVE);
        assertEquals(StudentStatus.ACADEMIC_LEAVE, student.getStatus());
    }
}

