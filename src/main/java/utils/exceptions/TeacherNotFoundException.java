package utils.exceptions;

/**
 * Викинута, коли вчитель не знайдено в системі.
 */
public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(String message) {
        super(message);
    }

    public TeacherNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static TeacherNotFoundException byId(String teacherId) {
        return new TeacherNotFoundException("Teacher with ID '" + teacherId + "' not found");
    }

    public static TeacherNotFoundException byName(String name) {
        return new TeacherNotFoundException("Teacher with name '" + name + "' not found");
    }
}

