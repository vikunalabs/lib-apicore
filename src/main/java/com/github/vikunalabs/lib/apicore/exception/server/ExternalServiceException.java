package com.github.vikunalabs.lib.apicore.exception.server;

import com.github.vikunalabs.lib.apicore.exception.base.ServerErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when an external service or dependency fails.
 * <p>
 * This exception should be used when a call to an external service (database,
 * third-party API, messaging system, etc.) fails unexpectedly. The original
 * exception should typically be included as the cause for debugging purposes.
 * </p>
 *
 * Example:
 * <pre>
 * try {
 *     externalService.call();
 * } catch (ExternalServiceTimeoutException e) {
 *     throw new ExternalServiceException(BaseAPICode.EXTERNAL_SERVICE_FAILURE,
 *         "Payment gateway service unavailable", e);
 * }
 * </pre>
 */
public class ExternalServiceException extends ServerErrorsBaseException {

    /** The name of the external service that failed or became unavailable. */
    private final String serviceName;

    /**
     * Constructs a new external service exception.
     *
     * @param code the API code (typically BaseAPICode.EXTERNAL_SERVICE_FAILURE)
     * @param message the detailed error message
     */
    public ExternalServiceException(APICode code, String message) {
        super(code, message);
        this.serviceName = null;
    }

    /**
     * Constructs a new external service exception with cause.
     *
     * @param code the API code (typically BaseAPICode.EXTERNAL_SERVICE_FAILURE)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public ExternalServiceException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
        this.serviceName = null;
    }

    /**
     * Constructs a new external service exception with service name.
     *
     * @param code the API code (typically BaseAPICode.EXTERNAL_SERVICE_FAILURE)
     * @param message the detailed error message
     * @param serviceName the name of the external service that failed
     */
    public ExternalServiceException(APICode code, String message, String serviceName) {
        super(code, message);
        this.serviceName = serviceName;
    }

    /**
     * Constructs a new external service exception with service name and cause.
     *
     * @param code the API code (typically BaseAPICode.EXTERNAL_SERVICE_FAILURE)
     * @param message the detailed error message
     * @param serviceName the name of the external service that failed
     * @param cause the underlying cause of this exception
     */
    public ExternalServiceException(APICode code, String message, String serviceName, Throwable cause) {
        super(code, message, cause);
        this.serviceName = serviceName;
    }

    /**
     * Returns the name of the external service that failed.
     *
     * @return the service name, or null if not specified
     */
    public String getServiceName() {
        return serviceName;
    }
}
