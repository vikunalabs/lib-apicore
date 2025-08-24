package com.github.vikunalabs.lib.apicore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Standard API response wrapper for all endpoints.
 *
 * <p>Note: Exactly one of 'data' or 'error' will be present:
 * <ul>
 * <li>'data' contains the response payload for successful requests (status 2xx)
 * <li>'error' contains error details for failed requests (status 4xx/5xx)
 * </ul>
 *
 * @param <T> the type of the response data
 * @param status HTTP status code
 * @param data response payload data (null for error responses)
 * @param error error details (null for success responses)
 * @param message human-readable status message
 * @param timestamp timestamp of response in ISO-8601 format
 */
@Schema(
        name = "APIResponse",
        description =
                """
                    Standard API response wrapper for all endpoints.

                    Note: Exactly one of 'data' or 'error' will be present:
                    - 'data' contains the response payload for successful requests (status 2xx)
                    - 'error' contains error details for failed requests (status 4xx/5xx)
                """,
        type = "object")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "data", "error", "message", "timestamp"})
public record APIResponse<T>(
        @Schema(
                        description = "HTTP Status Code",
                        type = "integer",
                        format = "int32",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                int status,
        @Schema(description = "Response payload data", nullable = true) T data,
        @Schema(description = "Error details if request failed", nullable = true, implementation = APIError.class)
                APIError error,
        @Schema(
                        description = "Human-readable status message",
                        requiredMode = Schema.RequiredMode.REQUIRED,
                        type = "string")
                String message,
        @Schema(
                        description = "Timestamp of response in ISO-8601 format",
                        type = "string",
                        format = "date-time",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Instant timestamp)
        implements Serializable {

    /**
     * Compact constructor that validates APIResponse invariants.
     * Ensures that exactly one of data or error is non-null and that
     * required fields are present.
     */
    public APIResponse {
        /**
         *
         * both data and error cannot exist together
         * */
        if (data != null && error != null) {
            throw new IllegalArgumentException("Both data and error cannot be non-null");
        }
        /*
         *
         * either data or error should exist, both cannot be null
         * */
        if (data == null && error == null) {
            throw new IllegalArgumentException("Either data or error must be non-null");
        }

        Objects.requireNonNull(message, "Message cannot be null");
        Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    // ========== SUCCESS FACTORY METHODS ==========

    /**
     * Create a success response with data and default message from APICode
     * Note: For HTTP 204 No Content, use framework-specific methods instead
     *
     * @param <T> the type of the response data
     * @param data the response data
     * @param code the API code
     * @return a success APIResponse
     */
    public static <T> APIResponse<T> success(T data, APICode code) {
        validateSuccessState(code.getHttpStatus());
        return new APIResponse<>(code.getHttpStatus(), data, null, getDefaultMessage(code), Instant.now());
    }

    /**
     * Create a success response with data and custom message
     * Note: For HTTP 204 No Content, use framework-specific methods instead
     *
     * @param <T> the type of the response data
     * @param data the response data
     * @param code the API code
     * @param message custom success message
     * @return a success APIResponse
     */
    public static <T> APIResponse<T> success(T data, APICode code, String message) {
        validateSuccessState(code.getHttpStatus());
        return new APIResponse<>(code.getHttpStatus(), data, null, message, Instant.now());
    }

    // ========== ERROR FACTORY METHODS ==========

    /**
     * Create an error response with APIError and default message from APICode
     *
     * @param <T> the type of the response data
     * @param error the API error
     * @param code the API code
     * @return an error APIResponse
     */
    public static <T> APIResponse<T> error(APIError error, APICode code) {
        validateErrorState(code.getHttpStatus());
        return new APIResponse<>(code.getHttpStatus(), null, error, getDefaultMessage(code), Instant.now());
    }

    /**
     * Create an error response with APIError and custom message
     *
     * @param <T> the type of the response data
     * @param error the API error
     * @param code the API code
     * @param message custom error message
     * @return an error APIResponse
     */
    public static <T> APIResponse<T> error(APIError error, APICode code, String message) {
        validateErrorState(code.getHttpStatus());
        return new APIResponse<>(code.getHttpStatus(), null, error, message, Instant.now());
    }

    // ========== VALIDATION METHODS ==========

    private static void validateSuccessState(int status) {
        if (status < 200 || status >= 300) {
            throw new IllegalArgumentException("Success factory method called with non-success status: " + status);
        }
        if (status == 204) {
            throw new IllegalArgumentException("HTTP 204 No Content cannot use APIResponse. "
                    + "Use framework-specific methods like ResponseEntity.noContent().build() instead.");
        }
    }

    private static void validateErrorState(int status) {
        if (status < 400 || status >= 600) {
            throw new IllegalArgumentException("Error factory method called with non-error status: " + status);
        }
    }

    // ========== BUILDER WITH GUIDANCE ==========

    /**
     * Creates a new builder instance for constructing APIResponse objects.
     *
     * @param <T> the type of the response data
     * @return a new Builder instance
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder class for constructing APIResponse instances with validation.
     *
     * @param <T> the type of the response data
     */
    public static class Builder<T> {
        private int status;
        private T data;
        private APIError error;
        private String message;
        private Instant timestamp = Instant.now();

        /**
         * Default constructor for Builder.
         */
        public Builder() {}

        /**
         * Sets the HTTP status code.
         *
         * @param status the HTTP status code
         * @return this builder instance
         */
        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }

        /**
         * Sets the status from an APICode and auto-fills message if not set.
         *
         * @param code the API code
         * @return this builder instance
         */
        public Builder<T> status(APICode code) {

            if (code.getHttpStatus() == 204) {
                throw new IllegalArgumentException("HTTP 204 cannot use APIResponse. "
                        + "Use framework-specific methods like ResponseEntity.noContent().build() instead.");
            }

            this.status = code.getHttpStatus();
            if (this.message == null) {
                this.message = getDefaultMessage(code);
            }
            return this;
        }

        /**
         * Sets the response data (clears error).
         *
         * @param data the response data
         * @return this builder instance
         */
        public Builder<T> data(T data) {
            this.data = data;
            this.error = null;
            return this;
        }

        /**
         * Sets the error (clears data).
         *
         * @param error the API error
         * @return this builder instance
         */
        public Builder<T> error(APIError error) {
            this.error = error;
            this.data = null;
            return this;
        }

        /**
         * Sets the response message.
         *
         * @param message the response message
         * @return this builder instance
         */
        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the response timestamp.
         *
         * @param timestamp the response timestamp
         * @return this builder instance
         */
        public Builder<T> timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Builds the APIResponse instance with validation.
         *
         * @return the constructed APIResponse
         */
        public APIResponse<T> build() {
            if (message == null) {
                throw new IllegalArgumentException("Message is required");
            }

            if (status == 204) {
                throw new IllegalArgumentException("HTTP 204 cannot use APIResponse");
            }

            return new APIResponse<>(status, data, error, message, timestamp);
        }
    }

    // ========== UTILITY METHODS ==========

    private static String getDefaultMessage(APICode code) {
        return code.name().toLowerCase().replace('_', ' ');
    }

    /**
     * Checks if this response represents a success (2xx status code).
     *
     * @return true if status is 2xx, false otherwise
     */
    public boolean isSuccess() {
        return status >= 200 && status < 300;
    }

    /**
     * Checks if this response contains an error.
     *
     * @return true if error is present, false otherwise
     */
    public boolean hasError() {
        return error != null;
    }

    /**
     * Gets the response data wrapped in an Optional.
     *
     * @return Optional containing the data, or empty if no data
     */
    @Schema(hidden = true)
    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    /**
     * Gets the error wrapped in an Optional.
     *
     * @return Optional containing the error, or empty if no error
     */
    @Schema(hidden = true)
    public Optional<APIError> getError() {
        return Optional.ofNullable(error);
    }
}
