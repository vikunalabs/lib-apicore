package com.github.vikunalabs.lib.apicore.exception.client;

import com.github.vikunalabs.lib.apicore.exception.base.ClientErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a client is authenticated but not authorized to perform an operation.
 * <p>
 * This exception should be used when a client has valid authentication credentials
 * but lacks the necessary permissions, roles, or scopes to access the requested resource.
 * </p>
 *
 * Example:
 * <pre>
 * throw new UnauthorizedException(BaseAPICode.FORBIDDEN,
 *     "User does not have permission to access this resource");
 * </pre>
 */
public class UnauthorizedException extends ClientErrorsBaseException {

    /**
     * Constructs a new unauthorized exception.
     *
     * @param code the API code (typically BaseAPICode.FORBIDDEN)
     * @param message the detailed error message
     */
    public UnauthorizedException(APICode code, String message) {
        super(code, message);
    }

    /**
     * Constructs a new unauthorized exception with cause.
     *
     * @param code the API code (typically BaseAPICode.FORBIDDEN)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public UnauthorizedException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * Constructs a new unauthorized exception with additional details.
     *
     * @param code the API code (typically BaseAPICode.FORBIDDEN)
     * @param message the detailed error message
     * @param details additional authorization details
     */
    public UnauthorizedException(APICode code, String message, Object details) {
        super(code, message, details);
    }
}
