package utils.exceptions;

/**
 * Викинута, коли реєстрація студента невалідна.
 */
public class InvalidEnrollmentException extends RuntimeException {
    public InvalidEnrollmentException(String message) {
        super(message);
    }

    public InvalidEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InvalidEnrollmentException missingFaculty() {
        return new InvalidEnrollmentException("Student must be enrolled in a faculty");
    }

    public static InvalidEnrollmentException missingSpeciality() {
        return new InvalidEnrollmentException("Student must be enrolled in a speciality");
    }

    public static InvalidEnrollmentException invalidGroup(int group) {
        return new InvalidEnrollmentException("Invalid group number: " + group);
    }
}

