package com.github.vikunalabs.lib.apicore.exception.business;

import com.github.vikunalabs.lib.apicore.exception.base.BusinessLogicBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when an operation is attempted on a resource that is not in
 * the appropriate state for that operation.
 * <p>
 * This exception should be used when the current state of a resource prevents
 * the requested operation from being performed, regardless of the input validity.
 * </p>
 *
 * Example:
 * <pre>
 * if (!order.getStatus().canBeCancelled()) {
 *     throw new InvalidStateException(BaseAPICode.OPERATION_NOT_ALLOWED,
 *         "Cannot cancel order in current state",
 *         Map.of("orderId", order.getId(), "currentStatus", order.getStatus()));
 * }
 * </pre>
 */
public class InvalidStateException extends BusinessLogicBaseException {

    /** The current state that prevents the operation from being performed. */
    private final String currentState;
    /** The required state for the operation to succeed. */
    private final String requiredState;

    /**
     * Constructs a new invalid state exception.
     *
     * @param code the API code (typically BaseAPICode.OPERATION_NOT_ALLOWED)
     * @param message the detailed error message
     */
    public InvalidStateException(APICode code, String message) {
        super(code, message);
        this.currentState = null;
        this.requiredState = null;
    }

    /**
     * Constructs a new invalid state exception with state information.
     *
     * @param code the API code (typically BaseAPICode.OPERATION_NOT_ALLOWED)
     * @param message the detailed error message
     * @param currentState the current state that caused the conflict
     * @param requiredState the required state for the operation to succeed
     */
    public InvalidStateException(APICode code, String message, String currentState, String requiredState) {
        super(code, message);
        this.currentState = currentState;
        this.requiredState = requiredState;
    }

    /**
     * Constructs a new invalid state exception with details.
     *
     * @param code the API code (typically BaseAPICode.OPERATION_NOT_ALLOWED)
     * @param message the detailed error message
     * @param details additional details about the state conflict
     */
    public InvalidStateException(APICode code, String message, Object details) {
        super(code, message, details);
        this.currentState = null;
        this.requiredState = null;
    }

    /**
     * Returns the current state that caused the conflict.
     *
     * @return the current state, or null if not specified
     */
    public String getCurrentState() {
        return currentState;
    }

    /**
     * Returns the required state for the operation to succeed.
     *
     * @return the required state, or null if not specified
     */
    public String getRequiredState() {
        return requiredState;
    }
}
