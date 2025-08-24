package com.github.vikunalabs.lib.apicore.exception.client;

import com.github.vikunalabs.lib.apicore.exception.base.ClientErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a requested resource cannot be found.
 * <p>
 * This exception should be used when a client requests a resource by identifier
 * that does not exist in the system. The resource identifier should typically
 * be included in the exception details or message for debugging purposes.
 * </p>
 *
 * Example:
 * <pre>
 * throw new ResourceNotFoundException(BaseAPICode.RESOURCE_NOT_FOUND,
 *     "User with ID '123' not found", "user-123");
 * </pre>
 */
public class ResourceNotFoundException extends ClientErrorsBaseException {

    /**
     * Constructs a new resource not found exception.
     *
     * @param code the API code (typically BaseAPICode.RESOURCE_NOT_FOUND)
     * @param message the detailed error message
     */
    public ResourceNotFoundException(APICode code, String message) {
        super(code, message);
    }

    /**
     * Constructs a new resource not found exception with resource identifier details.
     *
     * @param code the API code (typically BaseAPICode.RESOURCE_NOT_FOUND)
     * @param message the detailed error message
     * @param resourceId the identifier of the resource that was not found
     */
    public ResourceNotFoundException(APICode code, String message, Object resourceId) {
        super(code, message, resourceId);
    }

    /**
     * Constructs a new resource not found exception with cause.
     *
     * @param code the API code (typically BaseAPICode.RESOURCE_NOT_FOUND)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public ResourceNotFoundException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
