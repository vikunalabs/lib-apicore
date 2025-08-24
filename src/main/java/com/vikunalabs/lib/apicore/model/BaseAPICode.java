package com.vikunalabs.lib.apicore.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Standardized API response codes with HTTP status codes.
 * Messages are passed dynamically.
 */
@Getter
@Schema(description = "Standardized API response codes with HTTP status", enumAsRef = true)
public enum BaseAPICode implements APICode {
    // ========== SUCCESS CODES (2xx) ==========
    @Schema(description = "Generic success response")
    SUCCESS(200),

    @Schema(description = "Resource created successfully")
    RESOURCE_CREATED(201),

    @Schema(description = "Resource updated successfully")
    RESOURCE_UPDATED(200),

    @Schema(description = "Resource deleted successfully")
    RESOURCE_DELETED(200),

    @Schema(description = "Operation accepted for processing")
    REQUEST_ACCEPTED(202),

    // ========== USER DOMAIN CODES ==========
    @Schema(description = "User operation successful")
    USER_SUCCESS(200),

    @Schema(description = "User created successfully")
    USER_CREATED(201),

    @Schema(description = "User not found")
    USER_NOT_FOUND(404),

    @Schema(description = "User already exists")
    USER_ALREADY_EXISTS(409),

    @Schema(description = "User account issue")
    USER_ACCOUNT_ISSUE(403),

    // ========== VALIDATION ERRORS (4xx) ==========

    @Schema(description = "Bad request")
    BAD_REQUEST(400),

    @Schema(description = "Request parameter validation failed")
    VALIDATION_FAILED(400),

    @Schema(description = "Invalid parameter")
    INVALID_PARAMETER(400),

    @Schema(description = "Malformed request")
    MALFORMED_REQUEST(400),

    @Schema(description = "Missing parameter")
    MISSING_PARAMETER(400),

    @Schema(description = "Type mismatch")
    TYPE_MISMATCH(400),

    @Schema(description = "Invalid input format")
    INVALID_INPUT(400),

    @Schema(description = "Missing field")
    MISSING_FIELD(400),

    @Schema(description = "Field length issue")
    FIELD_LENGTH_ISSUE(400),

    @Schema(description = "Invalid format")
    INVALID_FORMAT(400),

    @Schema(description = "Value out of range")
    OUT_OF_RANGE(400),

    // ========== BUSINESS ERRORS (4xx) ==========
    @Schema(description = "Resource not found")
    RESOURCE_NOT_FOUND(404),

    @Schema(description = "Resource already exists")
    RESOURCE_EXISTS(409),

    @Schema(description = "Resource conflict")
    RESOURCE_CONFLICT(409),

    @Schema(description = "Operation not allowed")
    OPERATION_NOT_ALLOWED(403),

    @Schema(description = "Account disabled")
    ACCOUNT_DISABLED(403),

    @Schema(description = "Account locked")
    ACCOUNT_LOCKED(403),

    @Schema(description = "No action taken or request not processable")
    NO_ACTION_TAKEN(400),

    @Schema(description = "Request not processable")
    REQUEST_NOT_PROCESSABLE(400),

    @Schema(description = "Quota exceeded")
    QUOTA_EXCEEDED(429),

    @Schema(description = "Business rule violation")
    BUSINESS_RULE_VIOLATION(400),

    // ========== AUTHENTICATION (4xx) ==========
    @Schema(description = "Authentication required")
    UNAUTHENTICATED(401),

    @Schema(description = "Invalid credentials")
    INVALID_CREDENTIALS(401),

    @Schema(description = "Account issue")
    ACCOUNT_ISSUE(403),

    // ========== AUTHORIZATION (4xx) ==========
    @Schema(description = "Access denied")
    FORBIDDEN(403),

    @Schema(description = "Role restriction")
    ROLE_RESTRICTED(403),

    @Schema(description = "Scope restriction")
    SCOPE_RESTRICTED(403),

    // ========== RATE LIMITING (4xx) ==========
    @Schema(description = "Too many requests")
    RATE_LIMITED(429),

    @Schema(description = "Concurrent request limit")
    CONCURRENCY_LIMIT(429),

    // ========== SERVER ERRORS (5xx) ==========
    @Schema(description = "Internal server error")
    INTERNAL_ERROR(500),

    @Schema(description = "Service unavailable")
    SERVICE_UNAVAILABLE(503),

    @Schema(description = "Database error")
    DATABASE_ERROR(500),

    @Schema(description = "External service failure")
    EXTERNAL_SERVICE_FAILURE(502),

    @Schema(description = "Not implemented")
    NOT_IMPLEMENTED(501);

    private final int httpStatus;

    BaseAPICode(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public int getHttpStatus() {
        return this.httpStatus;
    }
}
