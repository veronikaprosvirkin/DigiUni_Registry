package utils.exceptions;

/**
 * Викинута, коли студент не знайдено в системі.
 */
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static StudentNotFoundException byId(String studentId) {
        return new StudentNotFoundException("Student with ID '" + studentId + "' not found");
    }

    public static StudentNotFoundException byName(String name) {
        return new StudentNotFoundException("Student with name '" + name + "' not found");
    }

    public static StudentNotFoundException byGroup(int groupNumber) {
        return new StudentNotFoundException("No students found in group " + groupNumber);
    }
}

