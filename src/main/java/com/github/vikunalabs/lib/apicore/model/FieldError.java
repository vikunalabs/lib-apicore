package com.github.vikunalabs.lib.apicore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

// spotless:off
/**
 * Validation error for a specific field in request data.
 * Used to provide detailed feedback about what went wrong with individual fields.
 * 
 * @param field name of the invalid field
 * @param code machine-readable error code for this field
 * @param message human-readable error message for this field
 * @param rejectedValue rejected input value (may be omitted for security reasons)
 */
@Schema(
        name = "FieldError",
        description = "Validation error for a specific field",
        type = "object"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"field", "code", "message", "rejectedValue"})
//spotless:on
public record FieldError(
        // spotless:off
        @Schema(
                description = "Name of the invalid field",
                requiredMode = Schema.RequiredMode.REQUIRED
        ) String field,

        @Schema(
                description = "Machine-readable error code for this field",
                requiredMode = Schema.RequiredMode.REQUIRED
        ) String code,

        @Schema(
                description = "Human-readable error message for this field",
                requiredMode = Schema.RequiredMode.REQUIRED
        ) String message,

        @Schema(
                description = "Rejected input value (may be omitted for security reasons)",
                nullable = true
        ) Object rejectedValue) {
    
    /**
     * Convenience constructor without rejected value.
     * 
     * @param field the field name
     * @param code the error code
     * @param message the error message
     */
    public FieldError(String field, String code, String message) {
        this(field, code, message, null);
    }

    // ========== BUILDER PATTERN ==========

    /**
     * Creates a new FieldError builder instance.
     * 
     * @return a new FieldErrorBuilder
     */
    public static FieldErrorBuilder builder() {
        return new FieldErrorBuilder();
    }

    /**
     * Builder class for constructing FieldError instances.
     */
    public static class FieldErrorBuilder {
        /**
         * Default constructor for FieldErrorBuilder.
         */
        public FieldErrorBuilder() {}
        private String field;
        private String code;
        private String message;
        private Object rejectedValue;

        /**
         * Sets the field name.
         * 
         * @param field the name of the field with the error
         * @return this builder instance
         */
        public FieldErrorBuilder field(String field) {
            this.field = field;
            return this;
        }

        /**
         * Sets the error code.
         * 
         * @param code the machine-readable error code
         * @return this builder instance
         */
        public FieldErrorBuilder code(String code) {
            this.code = code;
            return this;
        }

        /**
         * Sets the error message.
         * 
         * @param message the human-readable error message
         * @return this builder instance
         */
        public FieldErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the rejected value.
         * 
         * @param rejectedValue the input value that was rejected
         * @return this builder instance
         */
        public FieldErrorBuilder rejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
            return this;
        }

        /**
         * Builds the FieldError instance.
         * 
         * @return the constructed FieldError
         */
        public FieldError build() {
            return new FieldError(field, code, message, rejectedValue);
        }
    }
}
//spotless:on
