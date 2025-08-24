package com.github.vikunalabs.lib.apicore.exception.base;

import com.github.vikunalabs.lib.apicore.exception.util.ExceptionResponseMapper;
import com.github.vikunalabs.lib.apicore.model.APICode;
import com.github.vikunalabs.lib.apicore.model.APIResponse;

/**
 * Base class for all client error exceptions (HTTP 4xx status codes).
 * <p>
 * Represents errors where the client seems to have erred, such as invalid requests,
 * authentication failures, or missing resources. These exceptions should typically
 * be shown to the end user as they indicate correctable client-side issues.
 * </p>
 *
 */
public class ClientErrorsBaseException extends RuntimeException {
    /** The API code defining the error type and HTTP status. */
    private final APICode code;

    /** Additional structured error details. */
    private final Object details;

    /**
     * Constructs a new client error exception with the specified APICode and message.
     *
     * @param code the API code defining the error type and HTTP status
     * @param message the detailed error message
     */
    public ClientErrorsBaseException(APICode code, String message) {
        super(message);
        this.code = code;
        this.details = null;
    }

    /**
     * Constructs a new client error exception with the specified APICode, message, and cause.
     *
     * @param code the API code defining the error type and HTTP status
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public ClientErrorsBaseException(APICode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.details = null;
    }

    /**
     * Constructs a new client error exception with the specified APICode, message, and details.
     *
     * @param code the API code defining the error type and HTTP status
     * @param message the detailed error message
     * @param details additional structured error details
     */
    public ClientErrorsBaseException(APICode code, String message, Object details) {
        super(message);
        this.code = code;
        this.details = details;
    }

    /**
     * Constructs a new client error exception with the specified APICode, message, cause, and details.
     *
     * @param code the API code defining the error type and HTTP status
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     * @param details additional structured error details
     */
    public ClientErrorsBaseException(APICode code, String message, Throwable cause, Object details) {
        super(message, cause);
        this.code = code;
        this.details = details;
    }

    /**
     * Returns the APICode associated with this exception.
     *
     * @return the API code defining the error type and HTTP status
     */
    public APICode getCode() {
        return code;
    }

    /**
     * Returns additional structured details associated with this exception.
     *
     * @return additional error details, or null if no details are provided
     */
    public Object getDetails() {
        return details;
    }

    /**
     * Returns the HTTP status code associated with this exception.
     *
     * @return the HTTP status code from the associated APICode
     */
    public int getHttpStatus() {
        return code.getHttpStatus();
    }

    /**
     * Converts this exception to a standardized APIResponse.
     *
     * @param <T> the response type parameter
     * @return a properly formatted APIResponse for this exception
     */
    public <T> APIResponse<T> toResponse() {
        return ExceptionResponseMapper.toResponse(this);
    }
}
