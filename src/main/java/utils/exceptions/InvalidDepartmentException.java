package utils.exceptions;

/**
 * Викинута при помилці з кафедрою.
 */
public class InvalidDepartmentException extends RuntimeException {
    public InvalidDepartmentException(String message) {
        super(message);
    }

    public InvalidDepartmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InvalidDepartmentException notFound(String departmentId) {
        return new InvalidDepartmentException("Department with ID '" + departmentId + "' not found");
    }

    public static InvalidDepartmentException orphaned() {
        return new InvalidDepartmentException("Department is not assigned to any faculty");
    }
}

