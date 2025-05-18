package app.exceptions;

import java.sql.SQLException;

/**
 * Exception wrapper for database-related errors.
 */
public class DatabaseException extends Exception {
    /**
     * Constructs a new DatabaseException with a detail message.
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new DatabaseException with a detail message and cause.
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DatabaseException with a SQLException cause and detail message.
     */
    public DatabaseException(SQLException cause, String message) {
        super(message, cause);
    }
}
