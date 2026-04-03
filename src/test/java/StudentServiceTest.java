import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    private StudentService studentService;
    private University university;
    private Speciality speciality;
    private Faculty faculty;
    private Student testStudent;
    private StudyForm studyForm;

    // Set up a test environment
    @BeforeEach
    void setUp() {

        university = new University();
        studentService = new StudentService(university);
        Teacher testDean = new Teacher(IdGenerator.generateTeacherId(), "Ivan", "Ivanov", "Ivanovych", "Dean", null);
        faculty = new Faculty("fc001", "Faculty of Computer Science", "FCS", "contacts", testDean);
        speciality = new Speciality("sp001","Software Engineering");
        faculty.getSpeciality().add(speciality);
        university.getFaculties().add(faculty);

        testStudent = new Student("st001", "Taras", "Shevchenko","sm", LocalDate.of(2026, 9, 1), 101, "Faculty of Computer Science", speciality,StudyForm.BUDGET);
        studentService.addStudentToSpeciality(testStudent, speciality, 101);
    }

    // Test addStudent method
    @Test
    void testAddStudent() {
        studentService.addStudent("Ivan", "Sirko","sm", LocalDate.of(2023, 9, 1), 102, StudyForm.BUDGET);
        List<Student> students = studentService.getAllStudents();
        assertEquals(2, students.size());
    }

    // Test addStudentToSpeciality method
    @Test
    void testAddStudentToSpeciality() {
        Student newStudent = new Student("st002", "Lesya", "Ukrainka","sm", LocalDate.of(2018, 9, 1), 101, "Faculty of Computer Science", speciality,StudyForm.BUDGET);
        studentService.addStudentToSpeciality(newStudent, speciality, 101);
        assertTrue(studentService.getAllStudents().contains(newStudent));
    }

    // Test moveStudentToGroup method
    @Test
    void testMoveStudentToGroup() {
        studentService.moveStudentToGroup(testStudent, 205);
        assertEquals(205, testStudent.getGroup());
    }

    // Test deleteStudent method
    @Test
    void testDeleteStudent() {
        studentService.deleteStudent(testStudent, speciality);
        assertFalse(studentService.getAllStudents().contains(testStudent));
    }

    // Test getAllStudents method
    @Test
    void testGetAllStudents() {
        List<Student> students = studentService.getAllStudents();
        assertEquals(1, students.size());
        assertTrue(students.contains(testStudent));
    }

    // Test findStudentsByFullName method
    @Test
    void testFindStudentsByFullName() {
        List<Student> found = studentService.findStudentsByFullName("Taras Shevchenko");
        assertTrue(found.contains(testStudent));
    }

    // Test findStudentsBySurname method
    @Test
    void testFindStudentsBySurname() {
        List<Student> found = studentService.findStudentsBySurname("Shevchenko");
        assertTrue(found.contains(testStudent));
    }

    // Test findStudentsByGroup method
    @ParameterizedTest
    @ValueSource(ints = {23,43,9})
    void testFindStudentsByGroup(int groupNumber) {
        Student testStudent = new Student("st003","Taras", "Shevchenko", "sm",LocalDate.of(2005, 9, 1), groupNumber, "Faculty of Computer Science", speciality,StudyForm.BUDGET);
        studentService.addStudentToSpeciality(testStudent, speciality, groupNumber);
        List<Student> found = studentService.findStudentsByGroup(groupNumber);
        assertTrue(found.contains(testStudent));
    }

    // Test findStudentsInSpecialityByGroup method
    @Test
    void testFindStudentsInSpecialityByGroup() {
        List<Student> found = studentService.findStudentsInSpecialityByGroup(speciality, 101);
        assertTrue(found.contains(testStudent));
    }

    // Test findStudentsByCourse method
    @Test
    void testFindStudentsByCourse() {
        List<Student> found = studentService.findStudentsByCourse(1);
        assertTrue(found.contains(testStudent));
    }

    // Test findStudentsBySpeciality method
    @Test
    void testFindStudentsBySpeciality() {
        List<Student> found = studentService.findStudentsBySpeciality(speciality);
        assertTrue(found.contains(testStudent));
    }
}