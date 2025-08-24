package com.github.vikunalabs.lib.apicore.model;

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
    /** Generic success response */
    @Schema(description = "Generic success response")
    SUCCESS(200),

    /** Resource created successfully */
    @Schema(description = "Resource created successfully")
    RESOURCE_CREATED(201),

    /** Resource updated successfully */
    @Schema(description = "Resource updated successfully")
    RESOURCE_UPDATED(200),

    /** Resource deleted successfully */
    @Schema(description = "Resource deleted successfully")
    RESOURCE_DELETED(200),

    /** Operation accepted for processing */
    @Schema(description = "Operation accepted for processing")
    REQUEST_ACCEPTED(202),

    // ========== USER DOMAIN CODES ==========
    /** User operation successful */
    @Schema(description = "User operation successful")
    USER_SUCCESS(200),

    /** User created successfully */
    @Schema(description = "User created successfully")
    USER_CREATED(201),

    /** User not found */
    @Schema(description = "User not found")
    USER_NOT_FOUND(404),

    /** User already exists */
    @Schema(description = "User already exists")
    USER_ALREADY_EXISTS(409),

    /** User account issue */
    @Schema(description = "User account issue")
    USER_ACCOUNT_ISSUE(403),

    // ========== VALIDATION ERRORS (4xx) ==========

    // Client Errors - Additional specific codes
    /** Authentication failed */
    @Schema(description = "Authentication failed")
    AUTHENTICATION_FAILED(401),

    /** Authorization failed - insufficient permissions */
    @Schema(description = "Authorization failed - insufficient permissions")
    AUTHORIZATION_FAILED(403),

    /** Concurrent modification detected */
    @Schema(description = "Concurrent modification detected")
    CONCURRENT_MODIFICATION(409),

    /** Resource version conflict */
    @Schema(description = "Resource version conflict")
    VERSION_CONFLICT(409),

    // Business Logic Errors
    /** Business rule violation */
    @Schema(description = "Business rule violation")
    BUSINESS_RULE_VIOLATION(400),

    /** Invalid resource state for operation */
    @Schema(description = "Invalid resource state for operation")
    INVALID_STATE(400),

    /** Operation not allowed */
    @Schema(description = "Operation not allowed")
    OPERATION_NOT_ALLOWED(403),

    /** Quota or limit exceeded */
    @Schema(description = "Quota or limit exceeded")
    QUOTA_EXCEEDED(429),

    /** Domain validation failed */
    @Schema(description = "Domain validation failed")
    DOMAIN_VALIDATION_FAILED(400),

    /** Bad request */
    @Schema(description = "Bad request")
    BAD_REQUEST(400),

    /** Request parameter validation failed */
    @Schema(description = "Request parameter validation failed")
    VALIDATION_FAILED(400),

    /** Invalid parameter */
    @Schema(description = "Invalid parameter")
    INVALID_PARAMETER(400),

    /** Malformed request */
    @Schema(description = "Malformed request")
    MALFORMED_REQUEST(400),

    /** Missing parameter */
    @Schema(description = "Missing parameter")
    MISSING_PARAMETER(400),

    /** Type mismatch */
    @Schema(description = "Type mismatch")
    TYPE_MISMATCH(400),

    /** Invalid input format */
    @Schema(description = "Invalid input format")
    INVALID_INPUT(400),

    /** Missing field */
    @Schema(description = "Missing field")
    MISSING_FIELD(400),

    /** Field length issue */
    @Schema(description = "Field length issue")
    FIELD_LENGTH_ISSUE(400),

    /** Invalid format */
    @Schema(description = "Invalid format")
    INVALID_FORMAT(400),

    /** Value out of range */
    @Schema(description = "Value out of range")
    OUT_OF_RANGE(400),

    // ========== BUSINESS ERRORS (4xx) ==========
    /** Resource not found */
    @Schema(description = "Resource not found")
    RESOURCE_NOT_FOUND(404),

    /** Resource already exists */
    @Schema(description = "Resource already exists")
    RESOURCE_EXISTS(409),

    /** Resource conflict */
    @Schema(description = "Resource conflict")
    RESOURCE_CONFLICT(409),

    /** Account disabled */
    @Schema(description = "Account disabled")
    ACCOUNT_DISABLED(403),

    /** Account locked */
    @Schema(description = "Account locked")
    ACCOUNT_LOCKED(403),

    /** No action taken or request not processable */
    @Schema(description = "No action taken or request not processable")
    NO_ACTION_TAKEN(400),

    /** Request not processable */
    @Schema(description = "Request not processable")
    REQUEST_NOT_PROCESSABLE(400),

    // ========== AUTHENTICATION (4xx) ==========
    /** Authentication required */
    @Schema(description = "Authentication required")
    UNAUTHENTICATED(401),

    /** Invalid credentials */
    @Schema(description = "Invalid credentials")
    INVALID_CREDENTIALS(401),

    /** Account issue */
    @Schema(description = "Account issue")
    ACCOUNT_ISSUE(403),

    // ========== AUTHORIZATION (4xx) ==========
    /** Access denied */
    @Schema(description = "Access denied")
    FORBIDDEN(403),

    /** Role restriction */
    @Schema(description = "Role restriction")
    ROLE_RESTRICTED(403),

    /** Scope restriction */
    @Schema(description = "Scope restriction")
    SCOPE_RESTRICTED(403),

    // ========== RATE LIMITING (4xx) ==========
    /** Too many requests */
    @Schema(description = "Too many requests")
    RATE_LIMITED(429),

    /** Concurrent request limit */
    @Schema(description = "Concurrent request limit")
    CONCURRENCY_LIMIT(429),

    // ========== SERVER ERRORS (5xx) ==========
    /** Internal server error */
    @Schema(description = "Internal server error")
    INTERNAL_ERROR(500),

    /** External service failure */
    @Schema(description = "External service failure")
    EXTERNAL_SERVICE_FAILURE(502),

    /** Database operation failed */
    @Schema(description = "Database operation failed")
    DATABASE_ERROR(500),

    /** External service timeout */
    @Schema(description = "External service timeout")
    EXTERNAL_SERVICE_TIMEOUT(504),

    /** Service temporarily unavailable */
    @Schema(description = "Service temporarily unavailable")
    SERVICE_UNAVAILABLE(503),

    /** Feature not implemented */
    @Schema(description = "Feature not implemented")
    NOT_IMPLEMENTED(501),

    /** Gateway timeout */
    @Schema(description = "Gateway timeout")
    GATEWAY_TIMEOUT(504);

    private final int httpStatus;

    BaseAPICode(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public int getHttpStatus() {
        return this.httpStatus;
    }
}
