package com.github.vikunalabs.lib.apicore.exception.client;

import com.github.vikunalabs.lib.apicore.exception.base.ClientErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;
import com.github.vikunalabs.lib.apicore.model.FieldError;
import java.util.List;

/**
 * Exception thrown when request validation fails.
 * <p>
 * This exception should contain detailed field-level validation errors
 * that describe what specific fields failed validation and why.
 * </p>
 *
 * Example:
 * <pre>
 * List&lt;FieldError&gt; fieldErrors = List.of(
 *     new FieldError("email", "INVALID_FORMAT", "Must be a valid email address"),
 *     new FieldError("password", "TOO_SHORT", "Password must be at least 8 characters")
 * );
 *
 * throw new ValidationException(BaseAPICode.VALIDATION_FAILED,
 *     "Request validation failed", fieldErrors);
 * </pre>
 */
public class ValidationException extends ClientErrorsBaseException {

    /**
     * Constructs a new validation exception.
     *
     * @param code the API code (typically BaseAPICode.VALIDATION_FAILED)
     * @param message the detailed error message
     */
    public ValidationException(APICode code, String message) {
        super(code, message);
    }

    /**
     * Constructs a new validation exception with field errors.
     *
     * @param code the API code (typically BaseAPICode.VALIDATION_FAILED)
     * @param message the detailed error message
     * @param fieldErrors the list of field-specific validation errors
     */
    public ValidationException(APICode code, String message, List<FieldError> fieldErrors) {
        super(code, message, fieldErrors);
    }

    /**
     * Constructs a new validation exception with cause.
     *
     * @param code the API code (typically BaseAPICode.VALIDATION_FAILED)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public ValidationException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * Returns the field errors associated with this validation exception.
     *
     * @return the list of field errors, or null if no field errors are provided
     */
    @SuppressWarnings("unchecked")
    public List<FieldError> getFieldErrors() {
        return (List<FieldError>) getDetails();
    }
}
