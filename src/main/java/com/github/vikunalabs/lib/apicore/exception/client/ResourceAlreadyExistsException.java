package com.github.vikunalabs.lib.apicore.exception.client;

import com.github.vikunalabs.lib.apicore.exception.base.ClientErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when attempting to create a resource that already exists.
 * <p>
 * This exception should be used when a client attempts to create a resource
 * with an identifier or unique constraint that conflicts with an existing resource.
 * </p>
 *
 * Example:
 * <pre>
 * throw new ResourceAlreadyExistsException(BaseAPICode.RESOURCE_EXISTS,
 *     "User with email 'user@example.com' already exists", "user@example.com");
 * </pre>
 */
public class ResourceAlreadyExistsException extends ClientErrorsBaseException {

    /**
     * Constructs a new resource already exists exception.
     *
     * @param code the API code (typically BaseAPICode.RESOURCE_EXISTS)
     * @param message the detailed error message
     */
    public ResourceAlreadyExistsException(APICode code, String message) {
        super(code, message);
    }

    /**
     * Constructs a new resource already exists exception with conflict details.
     *
     * @param code the API code (typically BaseAPICode.RESOURCE_EXISTS)
     * @param message the detailed error message
     * @param conflictingValue the value that caused the conflict
     */
    public ResourceAlreadyExistsException(APICode code, String message, Object conflictingValue) {
        super(code, message, conflictingValue);
    }

    /**
     * Constructs a new resource already exists exception with cause.
     *
     * @param code the API code (typically BaseAPICode.RESOURCE_EXISTS)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public ResourceAlreadyExistsException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
