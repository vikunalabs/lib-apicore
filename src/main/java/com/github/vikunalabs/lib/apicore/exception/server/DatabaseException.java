package com.github.vikunalabs.lib.apicore.exception.server;

import com.github.vikunalabs.lib.apicore.exception.base.ServerErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a database operation fails.
 * <p>
 * This exception should be used for database-related errors such as connection
 * failures, query timeouts, constraint violations, or other database-specific issues.
 * The original database exception should typically be included as the cause.
 * </p>
 *
 * Example:
 * <pre>
 * try {
 *     database.executeQuery(query);
 * } catch (SQLException e) {
 *     throw new DatabaseException(BaseAPICode.DATABASE_ERROR,
 *         "Failed to execute database query", e);
 * }
 * </pre>
 */
public class DatabaseException extends ServerErrorsBaseException {

    /** The specific database operation that failed (e.g., INSERT, UPDATE, DELETE). */
    private final String databaseOperation;

    /**
     * Constructs a new database exception.
     *
     * @param code the API code (typically BaseAPICode.DATABASE_ERROR)
     * @param message the detailed error message
     */
    public DatabaseException(APICode code, String message) {
        super(code, message);
        this.databaseOperation = null;
    }

    /**
     * Constructs a new database exception with cause.
     *
     * @param code the API code (typically BaseAPICode.DATABASE_ERROR)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public DatabaseException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
        this.databaseOperation = null;
    }

    /**
     * Constructs a new database exception with operation details.
     *
     * @param code the API code (typically BaseAPICode.DATABASE_ERROR)
     * @param message the detailed error message
     * @param databaseOperation the specific database operation that failed
     */
    public DatabaseException(APICode code, String message, String databaseOperation) {
        super(code, message);
        this.databaseOperation = databaseOperation;
    }

    /**
     * Constructs a new database exception with operation details and cause.
     *
     * @param code the API code (typically BaseAPICode.DATABASE_ERROR)
     * @param message the detailed error message
     * @param databaseOperation the specific database operation that failed
     * @param cause the underlying cause of this exception
     */
    public DatabaseException(APICode code, String message, String databaseOperation, Throwable cause) {
        super(code, message, cause);
        this.databaseOperation = databaseOperation;
    }

    /**
     * Returns the database operation that failed.
     *
     * @return the database operation name, or null if not specified
     */
    public String getDatabaseOperation() {
        return databaseOperation;
    }
}
