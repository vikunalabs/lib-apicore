package com.github.vikunalabs.lib.apicore.exception.server;

import com.github.vikunalabs.lib.apicore.exception.base.ServerErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a gateway or proxy service times out waiting for a response
 * from an upstream server.
 * <p>
 * This exception should be used when the server acting as a gateway or proxy
 * does not receive a timely response from an upstream server it needed to access
 * in order to complete the request.
 * </p>
 *
 * Example:
 * <pre>
 * throw new GatewayTimeoutException(BaseAPICode.EXTERNAL_SERVICE_FAILURE,
 *     "Upstream service did not respond within timeout period", "user-profile-service");
 * </pre>
 */
public class GatewayTimeoutException extends ServerErrorsBaseException {

    /** The name of the upstream service that failed to respond within the timeout period. */
    private final String upstreamService;

    /**
     * Constructs a new gateway timeout exception.
     *
     * @param code the API code (typically BaseAPICode.EXTERNAL_SERVICE_FAILURE with 504 status)
     * @param message the detailed error message
     */
    public GatewayTimeoutException(APICode code, String message) {
        super(code, message);
        this.upstreamService = null;
    }

    /**
     * Constructs a new gateway timeout exception with upstream service details.
     *
     * @param code the API code (typically BaseAPICode.EXTERNAL_SERVICE_FAILURE with 504 status)
     * @param message the detailed error message
     * @param upstreamService the name of the upstream service that timed out
     */
    public GatewayTimeoutException(APICode code, String message, String upstreamService) {
        super(code, message);
        this.upstreamService = upstreamService;
    }

    /**
     * Constructs a new gateway timeout exception with cause.
     *
     * @param code the API code (typically BaseAPICode.EXTERNAL_SERVICE_FAILURE with 504 status)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public GatewayTimeoutException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
        this.upstreamService = null;
    }

    /**
     * Returns the name of the upstream service that timed out.
     *
     * @return the upstream service name, or null if not specified
     */
    public String getUpstreamService() {
        return upstreamService;
    }
}
