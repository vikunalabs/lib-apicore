package com.github.vikunalabs.lib.apicore.exception.server;

import com.github.vikunalabs.lib.apicore.exception.base.ServerErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when the service is temporarily unavailable.
 * <p>
 * This exception should be used when the server is temporarily unable to handle
 * requests due to maintenance, overload, or other temporary conditions. Clients
 * should typically be instructed to retry after a delay.
 * </p>
 *
 * Example:
 * <pre>
 * throw new ServiceUnavailableException(BaseAPICode.SERVICE_UNAVAILABLE,
 *     "Service undergoing maintenance. Please try again in 30 minutes", 1800);
 * </pre>
 */
public class ServiceUnavailableException extends ServerErrorsBaseException {

    /**
     * Constructs a new service unavailable exception.
     *
     * @param code the API code (typically BaseAPICode.SERVICE_UNAVAILABLE)
     * @param message the detailed error message
     */
    public ServiceUnavailableException(APICode code, String message) {
        super(code, message);
    }

    /**
     * Constructs a new service unavailable exception with retry information.
     *
     * @param code the API code (typically BaseAPICode.SERVICE_UNAVAILABLE)
     * @param message the detailed error message
     * @param retryAfterSeconds number of seconds after which the client can retry
     */
    public ServiceUnavailableException(APICode code, String message, long retryAfterSeconds) {
        super(code, message, retryAfterSeconds);
    }

    /**
     * Constructs a new service unavailable exception with cause.
     *
     * @param code the API code (typically BaseAPICode.SERVICE_UNAVAILABLE)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public ServiceUnavailableException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * Returns the retry-after information if provided.
     *
     * @return the number of seconds to wait before retrying, or null if not specified
     */
    public Long getRetryAfterSeconds() {
        Object details = getDetails();
        return details instanceof Long ? (Long) details : null;
    }
}
