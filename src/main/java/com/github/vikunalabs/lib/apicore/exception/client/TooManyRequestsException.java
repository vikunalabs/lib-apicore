package com.github.vikunalabs.lib.apicore.exception.client;

import com.github.vikunalabs.lib.apicore.exception.base.ClientErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a client exceeds rate limits.
 * <p>
 * This exception should be used when a client has made too many requests
 * within a specific time period and rate limiting is enforced.
 * </p>
 *
 * Example:
 * <pre>
 * throw new TooManyRequestsException(BaseAPICode.RATE_LIMITED,
 *     "Rate limit exceeded. Please try again in 60 seconds");
 * </pre>
 */
public class TooManyRequestsException extends ClientErrorsBaseException {

    /**
     * Constructs a new too many requests exception.
     *
     * @param code the API code (typically BaseAPICode.RATE_LIMITED)
     * @param message the detailed error message
     */
    public TooManyRequestsException(APICode code, String message) {
        super(code, message);
    }

    /**
     * Constructs a new too many requests exception with retry information.
     *
     * @param code the API code (typically BaseAPICode.RATE_LIMITED)
     * @param message the detailed error message
     * @param retryAfterSeconds number of seconds after which the client can retry
     */
    public TooManyRequestsException(APICode code, String message, long retryAfterSeconds) {
        super(code, message, retryAfterSeconds);
    }

    /**
     * Constructs a new too many requests exception with cause.
     *
     * @param code the API code (typically BaseAPICode.RATE_LIMITED)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public TooManyRequestsException(APICode code, String message, Throwable cause) {
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
