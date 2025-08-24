package com.github.vikunalabs.lib.apicore.exception.business;

import com.github.vikunalabs.lib.apicore.exception.base.BusinessLogicBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when an operation is not allowed due to business constraints
 * that are not covered by more specific exception types.
 * <p>
 * This exception should be used as a catch-all for business logic restrictions
 * that don't fit into more specific categories like quota exceeded or invalid state.
 * </p>
 *
 * Example:
 * <pre>
 * if (!user.hasPermission(Permission.DELETE_OTHER_USERS)) {
 *     throw new OperationNotAllowedException(BaseAPICode.OPERATION_NOT_ALLOWED,
 *         "User does not have permission to delete other users");
 * }
 * </pre>
 */
public class OperationNotAllowedException extends BusinessLogicBaseException {

    /** The operation that was not allowed to be performed. */
    private final String operation;
    /** The business constraint that prevented the operation from being performed. */
    private final String constraint;

    /**
     * Constructs a new operation not allowed exception.
     *
     * @param code the API code (typically BaseAPICode.OPERATION_NOT_ALLOWED)
     * @param message the detailed error message
     */
    public OperationNotAllowedException(APICode code, String message) {
        super(code, message);
        this.operation = null;
        this.constraint = null;
    }

    /**
     * Constructs a new operation not allowed exception with operation details.
     *
     * @param code the API code (typically BaseAPICode.OPERATION_NOT_ALLOWED)
     * @param message the detailed error message
     * @param operation the operation that was not allowed
     * @param constraint the business constraint that prevented the operation
     */
    public OperationNotAllowedException(APICode code, String message, String operation, String constraint) {
        super(code, message);
        this.operation = operation;
        this.constraint = constraint;
    }

    /**
     * Constructs a new operation not allowed exception with details.
     *
     * @param code the API code (typically BaseAPICode.OPERATION_NOT_ALLOWED)
     * @param message the detailed error message
     * @param details additional details about the operation restriction
     */
    public OperationNotAllowedException(APICode code, String message, Object details) {
        super(code, message, details);
        this.operation = null;
        this.constraint = null;
    }

    /**
     * Returns the operation that was not allowed.
     *
     * @return the operation name, or null if not specified
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Returns the business constraint that prevented the operation.
     *
     * @return the constraint description, or null if not specified
     */
    public String getConstraint() {
        return constraint;
    }
}
