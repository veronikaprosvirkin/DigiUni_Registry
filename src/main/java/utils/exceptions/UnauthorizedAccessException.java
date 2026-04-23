package utils.exceptions;

/**
 * Викинута, коли користувач не має прав доступу.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UnauthorizedAccessException insufficientRole(String requiredRole) {
        return new UnauthorizedAccessException("Insufficient permissions. Required role: " + requiredRole);
    }

    public static UnauthorizedAccessException userNotFound(String username) {
        return new UnauthorizedAccessException("User '" + username + "' not found or not authenticated");
    }
}

