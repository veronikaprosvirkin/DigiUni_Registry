package utils;

import org.junit.jupiter.api.Test;
import utils.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionsTest {

    @Test
    void testTeacherNotFoundException() {
        Exception exception = assertThrows(TeacherNotFoundException.class, () -> {
            throw TeacherNotFoundException.byId("t001");
        });
        assertTrue(exception.getMessage().contains("t001"));
    }

    @Test
    void testStudentNotFoundException() {
        Exception exception = assertThrows(StudentNotFoundException.class, () -> {
            throw StudentNotFoundException.byId("s001");
        });
        assertTrue(exception.getMessage().contains("s001"));
    }

    @Test
    void testFacultyNotFoundException() {
        Exception exception = assertThrows(FacultyNotFoundException.class, () -> {
            throw FacultyNotFoundException.byId("f001");
        });
        assertTrue(exception.getMessage().contains("f001"));
    }

    @Test
    void testInvalidEnrollmentException() {
        Exception exception = assertThrows(InvalidEnrollmentException.class, () -> {
            throw InvalidEnrollmentException.invalidGroup(-1);
        });
        assertTrue(exception.getMessage().contains("group"));
    }

    @Test
    void testDuplicateTeacherException() {
        Exception exception = assertThrows(DuplicateTeacherException.class, () -> {
            throw DuplicateTeacherException.byId("t001");
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testInvalidDepartmentException() {
        Exception exception = assertThrows(InvalidDepartmentException.class, () -> {
            throw InvalidDepartmentException.notFound("d001");
        });
        assertTrue(exception.getMessage().contains("d001"));
    }

    @Test
    void testUnauthorizedAccessException() {
        Exception exception = assertThrows(UnauthorizedAccessException.class, () -> {
            throw UnauthorizedAccessException.insufficientRole("ADMIN");
        });
        assertTrue(exception.getMessage().contains("ADMIN"));
    }
}

