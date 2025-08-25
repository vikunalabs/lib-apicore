package com.github.vikunalabs.lib.apicore.model;

import lombok.Getter;

/**
 * Standardized API response codes with HTTP status codes.
 * Messages are passed dynamically.
 */
@Getter
public enum BaseAPICode implements APICode {
    // ========== SUCCESS CODES (2xx) ==========
    /** Generic success response */
    SUCCESS(200),

    /** Resource created successfully */
    RESOURCE_CREATED(201),

    /** Resource updated successfully */
    RESOURCE_UPDATED(200),

    /** Resource deleted successfully */
    RESOURCE_DELETED(200),

    /** Operation accepted for processing */
    REQUEST_ACCEPTED(202),

    // ========== USER DOMAIN CODES ==========
    /** User operation successful */
    USER_SUCCESS(200),

    /** User created successfully */
    USER_CREATED(201),

    /** User not found */
    USER_NOT_FOUND(404),

    /** User already exists */
    USER_ALREADY_EXISTS(409),

    /** User account issue */
    USER_ACCOUNT_ISSUE(403),

    // ========== VALIDATION ERRORS (4xx) ==========

    // Client Errors - Additional specific codes
    /** Authentication failed */
    AUTHENTICATION_FAILED(401),

    /** Authorization failed - insufficient permissions */
    AUTHORIZATION_FAILED(403),

    /** Concurrent modification detected */
    CONCURRENT_MODIFICATION(409),

    /** Resource version conflict */
    VERSION_CONFLICT(409),

    // Business Logic Errors
    /** Business rule violation */
    BUSINESS_RULE_VIOLATION(400),

    /** Invalid resource state for operation */
    INVALID_STATE(400),

    /** Operation not allowed */
    OPERATION_NOT_ALLOWED(403),

    /** Quota or limit exceeded */
    QUOTA_EXCEEDED(429),

    /** Domain validation failed */
    DOMAIN_VALIDATION_FAILED(400),

    /** Bad request */
    BAD_REQUEST(400),

    /** Request parameter validation failed */
    VALIDATION_FAILED(400),

    /** Invalid parameter */
    INVALID_PARAMETER(400),

    /** Malformed request */
    MALFORMED_REQUEST(400),

    /** Missing parameter */
    MISSING_PARAMETER(400),

    /** Type mismatch */
    TYPE_MISMATCH(400),

    /** Invalid input format */
    INVALID_INPUT(400),

    /** Missing field */
    MISSING_FIELD(400),

    /** Field length issue */
    FIELD_LENGTH_ISSUE(400),

    /** Invalid format */
    INVALID_FORMAT(400),

    /** Value out of range */
    OUT_OF_RANGE(400),

    // ========== BUSINESS ERRORS (4xx) ==========
    /** Resource not found */
    RESOURCE_NOT_FOUND(404),

    /** Resource already exists */
    RESOURCE_EXISTS(409),

    /** Resource conflict */
    RESOURCE_CONFLICT(409),

    /** Account disabled */
    ACCOUNT_DISABLED(403),

    /** Account locked */
    ACCOUNT_LOCKED(403),

    /** No action taken or request not processable */
    NO_ACTION_TAKEN(400),

    /** Request not processable */
    REQUEST_NOT_PROCESSABLE(400),

    // ========== AUTHENTICATION (4xx) ==========
    /** Authentication required */
    UNAUTHENTICATED(401),

    /** Invalid credentials */
    INVALID_CREDENTIALS(401),

    /** Account issue */
    ACCOUNT_ISSUE(403),

    // ========== AUTHORIZATION (4xx) ==========
    /** Access denied */
    FORBIDDEN(403),

    /** Role restriction */
    ROLE_RESTRICTED(403),

    /** Scope restriction */
    SCOPE_RESTRICTED(403),

    // ========== RATE LIMITING (4xx) ==========
    /** Too many requests */
    RATE_LIMITED(429),

    /** Concurrent request limit */
    CONCURRENCY_LIMIT(429),

    // ========== SERVER ERRORS (5xx) ==========
    /** Internal server error */
    INTERNAL_ERROR(500),

    /** External service failure */
    EXTERNAL_SERVICE_FAILURE(502),

    /** Database operation failed */
    DATABASE_ERROR(500),

    /** External service timeout */
    EXTERNAL_SERVICE_TIMEOUT(504),

    /** Service temporarily unavailable */
    SERVICE_UNAVAILABLE(503),

    /** Feature not implemented */
    NOT_IMPLEMENTED(501),

    /** Gateway timeout */
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
