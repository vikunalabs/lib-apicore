package com.github.vikunalabs.lib.apicore.exception.client;

import com.github.vikunalabs.lib.apicore.exception.base.ClientErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when authentication fails.
 * <p>
 * This exception should be used when a client cannot be authenticated,
 * such as invalid credentials, missing authentication tokens, or expired sessions.
 * </p>
 *
 * Example:
 * <pre>
 * throw new AuthenticationException(BaseAPICode.UNAUTHENTICATED,
 *     "Invalid authentication token");
 * </pre>
 */
public class AuthenticationException extends ClientErrorsBaseException {

    /**
     * Constructs a new authentication exception.
     *
     * @param code the API code (typically BaseAPICode.UNAUTHENTICATED or BaseAPICode.INVALID_CREDENTIALS)
     * @param message the detailed error message
     */
    public AuthenticationException(APICode code, String message) {
        super(code, message);
    }

    /**
     * Constructs a new authentication exception with cause.
     *
     * @param code the API code (typically BaseAPICode.UNAUTHENTICATED or BaseAPICode.INVALID_CREDENTIALS)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public AuthenticationException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * Constructs a new authentication exception with additional details.
     *
     * @param code the API code (typically BaseAPICode.UNAUTHENTICATED or BaseAPICode.INVALID_CREDENTIALS)
     * @param message the detailed error message
     * @param details additional authentication details
     */
    public AuthenticationException(APICode code, String message, Object details) {
        super(code, message, details);
    }
}
