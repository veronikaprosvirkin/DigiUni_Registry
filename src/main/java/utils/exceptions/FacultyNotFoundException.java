package utils.exceptions;

/**
 * Викинута, коли факультет не знайдено в системі.
 */
public class FacultyNotFoundException extends RuntimeException {
    public FacultyNotFoundException(String message) {
        super(message);
    }

    public FacultyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static FacultyNotFoundException byId(String facultyId) {
        return new FacultyNotFoundException("Faculty with ID '" + facultyId + "' not found");
    }

    public static FacultyNotFoundException byName(String name) {
        return new FacultyNotFoundException("Faculty with name '" + name + "' not found");
    }
}

