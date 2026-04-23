package utils.exceptions;

/**
 * Викинута, коли спроба додати дубльного вчителя.
 */
public class DuplicateTeacherException extends RuntimeException {
    public DuplicateTeacherException(String message) {
        super(message);
    }

    public DuplicateTeacherException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DuplicateTeacherException byId(String teacherId) {
        return new DuplicateTeacherException("Teacher with ID '" + teacherId + "' already exists");
    }

    public static DuplicateTeacherException byName(String name) {
        return new DuplicateTeacherException("Teacher with name '" + name + "' already exists");
    }
}

