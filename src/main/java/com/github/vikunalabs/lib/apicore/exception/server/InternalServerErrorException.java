package com.github.vikunalabs.lib.apicore.exception.server;

import com.github.vikunalabs.lib.apicore.exception.base.ServerErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when an unexpected internal server error occurs.
 * <p>
 * This exception should be used as a catch-all for unexpected server-side errors
 * that don't fit into more specific categories. It typically indicates a bug or
 * unhandled condition in the server code.
 * </p>
 *
 * <p><b>Security Note:</b> The detailed message should not be exposed to end users
 * in production environments to avoid leaking sensitive implementation details.</p>
 *
 * Example:
 * <pre>
 * throw new InternalServerErrorException(BaseAPICode.INTERNAL_ERROR,
 *     "An unexpected error occurred while processing the request");
 * </pre>
 */
public class InternalServerErrorException extends ServerErrorsBaseException {

    /**
     * Constructs a new internal server error exception.
     *
     * @param code the API code (typically BaseAPICode.INTERNAL_ERROR)
     * @param message the detailed error message
     */
    public InternalServerErrorException(APICode code, String message) {
        super(code, message);
    }

    /**
     * Constructs a new internal server error exception with cause.
     *
     * @param code the API code (typically BaseAPICode.INTERNAL_ERROR)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public InternalServerErrorException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * Constructs a new internal server error exception with additional details.
     *
     * @param code the API code (typically BaseAPICode.INTERNAL_ERROR)
     * @param message the detailed error message
     * @param details additional error details for debugging
     */
    public InternalServerErrorException(APICode code, String message, Object details) {
        super(code, message, details);
    }
}
