package com.github.vikunalabs.lib.apicore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

/**
 * Standardized error response format for API failures.
 * <p>
 * Usage examples:
 * <pre>{@code
 * // Simple error
 * new APIError("NOT_FOUND", "Resource not found");
 *
 * // Validation error
 * new APIError("VALIDATION_FAILED", "Invalid data", fieldErrors);
 *
 * // Error with details
 * new APIError("DB_ERROR", "Database operation failed", "Connection timeout");
 * }</pre>
 *
 * @param code Machine-readable error code
 * @param message Human-readable error message
 * @param fieldErrors List of field-specific errors (optional)
 * @param details Additional debug information (optional)
 */
@Schema(name = "APIError", description = "Standard error response format for API failures", type = "object")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "message", "fieldErrors", "details"})
public record APIError(
        @Schema(
                        description = "Machine-readable error code from APICode enum",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String code,
        @Schema(description = "Human-readable error message", requiredMode = Schema.RequiredMode.REQUIRED)
                String message,
        @Schema(
                        description = "List of field-specific validation errors (only present for 400 errors)",
                        nullable = true,
                        type = "array",
                        implementation = FieldError.class)
                List<FieldError> fieldErrors,
        @Schema(description = "Additional debug information", nullable = true) String details) {

    /**
     * Constructor for simple errors without field errors or details.
     *
     * @param code the error code
     * @param message the error message
     */
    public APIError(String code, String message) {
        this(code, message, List.of(), null);
    }

    /**
     * Constructor for validation errors with field-specific errors.
     *
     * @param code the error code
     * @param message the error message
     * @param fieldErrors list of field-specific validation errors
     */
    public APIError(String code, String message, List<FieldError> fieldErrors) {
        this(code, message, fieldErrors, null);
    }

    /**
     * Constructor for errors with additional details but no field errors.
     *
     * @param code the error code
     * @param message the error message
     * @param details additional debug information
     */
    public APIError(String code, String message, String details) {
        this(code, message, List.of(), details);
    }

    /**
     * Creates a new APIError builder instance.
     *
     * @return a new APIErrorBuilder
     */
    public static APIErrorBuilder builder() {
        return new APIErrorBuilder();
    }

    /**
     * Builder class for constructing APIError instances.
     */
    public static class APIErrorBuilder {
        /**
         * Default constructor for APIErrorBuilder.
         */
        public APIErrorBuilder() {}

        private String code;
        private String message;
        private List<FieldError> fieldErrors;
        private String details;

        /**
         * Sets the error code.
         *
         * @param code the error code
         * @return this builder instance
         */
        public APIErrorBuilder code(String code) {
            this.code = code;
            return this;
        }

        /**
         * Sets the error message.
         *
         * @param message the error message
         * @return this builder instance
         */
        public APIErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the list of field errors.
         *
         * @param fieldErrors list of field-specific validation errors
         * @return this builder instance
         */
        public APIErrorBuilder fieldErrors(List<FieldError> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        /**
         * Adds a single field error to the list.
         *
         * @param fieldError the field error to add
         * @return this builder instance
         */
        public APIErrorBuilder fieldError(FieldError fieldError) {
            if (this.fieldErrors == null) {
                this.fieldErrors = new ArrayList<>();
            }
            this.fieldErrors.add(fieldError);
            return this;
        }

        /**
         * Adds a field error with the specified parameters.
         *
         * @param field the field name
         * @param code the error code
         * @param message the error message
         * @return this builder instance
         */
        public APIErrorBuilder fieldError(String field, String code, String message) {
            return fieldError(new FieldError(field, code, message));
        }

        /**
         * Adds a field error with the specified parameters including rejected value.
         *
         * @param field the field name
         * @param code the error code
         * @param message the error message
         * @param rejectedValue the rejected input value
         * @return this builder instance
         */
        public APIErrorBuilder fieldError(String field, String code, String message, Object rejectedValue) {
            return fieldError(new FieldError(field, code, message, rejectedValue));
        }

        /**
         * Sets additional details for the error.
         *
         * @param details additional debug information
         * @return this builder instance
         */
        public APIErrorBuilder details(String details) {
            this.details = details;
            return this;
        }

        /**
         * Builds the APIError instance.
         *
         * @return the constructed APIError
         */
        public APIError build() {
            // Ensure fieldErrors is never null
            List<FieldError> finalFieldErrors = fieldErrors != null ? fieldErrors : List.of();
            return new APIError(code, message, finalFieldErrors, details);
        }
    }
}
