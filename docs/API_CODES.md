
# API Codes Reference

## 📋 Overview

APICode is the core interface that defines standard HTTP status mappings and messages for consistent API responses.

## 🎯 BaseAPICode Enum

The library provides `BaseAPICode` enum with commonly used HTTP status codes:

### Success Codes (2xx)
```java
SUCCESS(200, "Operation completed successfully"),
RESOURCE_CREATED(201, "Resource created successfully"),
RESOURCE_UPDATED(200, "Resource updated successfully"),
RESOURCE_DELETED(200, "Resource deleted successfully"),
NO_CONTENT(204, "No content"); // Note: Special handling required
```

### Client Error Codes (4xx)
```java
BAD_REQUEST(400, "Bad request"),
UNAUTHORIZED(401, "Unauthorized"),
FORBIDDEN(403, "Forbidden"),
RESOURCE_NOT_FOUND(404, "Resource not found"),
METHOD_NOT_ALLOWED(405, "Method not allowed"),
VALIDATION_FAILED(400, "Validation failed"),
RESOURCE_EXISTS(409, "Resource already exists"),
QUOTA_EXCEEDED(429, "Quota exceeded")
```

### Server Error Codes (5xx)
```java
INTERNAL_ERROR(500, "Internal server error"),
SERVICE_UNAVAILABLE(503, "Service unavailable"),
GATEWAY_TIMEOUT(504, "Gateway timeout"),
EXTERNAL_SERVICE_ERROR(502, "External service error")
```

### Business Logic Codes
```java
BUSINESS_RULE_VIOLATION(400, "Business rule violation"),
INVALID_STATE(400, "Invalid state"),
CONCURRENT_MODIFICATION(409, "Concurrent modification detected")
```

## 🛠️ Custom APICode Implementation

### Creating Custom Codes

```java
public enum CustomAPICode implements APICode {
    // Success codes
    USER_REGISTERED(201, "User registered successfully"),
    PASSWORD_RESET(200, "Password reset successful"),
    
    // Error codes
    INVALID_CREDENTIALS(401, "Invalid credentials"),
    ACCOUNT_LOCKED(423, "Account locked"),
    PAYMENT_REQUIRED(402, "Payment required"),
    
    // Business codes
    INSUFFICIENT_FUNDS(400, "Insufficient funds"),
    ORDER_LIMIT_EXCEEDED(429, "Order limit exceeded");

    private final int httpStatus;
    private final String defaultMessage;

    CustomAPICode(int httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
```

### Using Custom Codes

```java
// Success response with custom code
return APIResponse.success(user, CustomAPICode.USER_REGISTERED);

// Error response with custom code
throw new BusinessRuleViolationException(
    CustomAPICode.INSUFFICIENT_FUNDS,
    "Insufficient funds for transaction",
    Map.of("currentBalance", 50.0, "requiredAmount", 100.0)
);
```

## 🔧 APICode Interface

### Interface Definition
```java
public interface APICode {
    int getHttpStatus();
    String getDefaultMessage();
    
    // Default methods
    default boolean isSuccess() {
        return getHttpStatus() >= 200 && getHttpStatus() < 300;
    }
    
    default boolean isError() {
        return !isSuccess();
    }
}
```

### Validation Rules
The library validates that:
- HTTP status codes are valid (100-599)
- Success codes (2xx) cannot have error data
- Error codes (4xx, 5xx) cannot have success data
- HTTP 204 cannot have any response body

## 📊 HTTP Status Code Guidelines

### When to Use Which Code

| Scenario | HTTP Status | APICode |
|----------|------------|---------|
| Successful operation | 200 | `SUCCESS` |
| Resource created | 201 | `RESOURCE_CREATED` |
| Resource deleted | 200* | `RESOURCE_DELETED` |
| No content | 204 | `NO_CONTENT` |
| Validation errors | 400 | `VALIDATION_FAILED` |
| Authentication required | 401 | `UNAUTHORIZED` |
| Permission denied | 403 | `FORBIDDEN` |
| Resource not found | 404 | `RESOURCE_NOT_FOUND` |
| Resource conflict | 409 | `RESOURCE_EXISTS` |
| Business rule violation | 400 | `BUSINESS_RULE_VIOLATION` |
| Server error | 500 | `INTERNAL_ERROR` |

*Note: For DELETE operations that return data, use 200 instead of 204.

## 🎯 Best Practices

### Code Selection
1. **Be specific**: Use the most appropriate status code
2. **Be consistent**: Use the same code for the same scenario across endpoints
3. **Use standard codes**: Prefer standard HTTP codes over custom ones
4. **Document exceptions**: Clearly document any non-standard usage

### Message Guidelines
1. **User-friendly**: Error messages should help users understand the issue
2. **Actionable**: Suggest how to resolve the issue when possible
3. **Consistent**: Use consistent language and format across endpoints
4. **Secure**: Don't expose sensitive information in error messages

### Custom Code Guidelines
1. **Extend, don't replace**: Use custom codes for domain-specific scenarios
2. **Document thoroughly**: Document each custom code's purpose and usage
3. **Maintain compatibility**: Don't change existing code meanings
4. **Limit scope**: Use custom codes sparingly; prefer standard codes when possible

## 🔄 Migration from Legacy Codes

### Step 1: Map Existing Codes
```java
// Legacy code mapping
public enum LegacyCode {
    OK(0),
    ERROR(1),
    NOT_FOUND(2);
    
    // Map to APICode
    public APICode toAPICode() {
        switch (this) {
            case OK: return BaseAPICode.SUCCESS;
            case ERROR: return BaseAPICode.INTERNAL_ERROR;
            case NOT_FOUND: return BaseAPICode.RESOURCE_NOT_FOUND;
            default: return BaseAPICode.INTERNAL_ERROR;
        }
    }
}
```

### Step 2: Gradual Migration
```java
// During migration phase
public APIResponse<User> getUser(String id) {
    try {
        User user = userService.getUser(id);
        return APIResponse.success(user, LegacyCode.OK.toAPICode());
    } catch (NotFoundException e) {
        return APIResponse.error(
            new APIError("NOT_FOUND", "User not found"),
            LegacyCode.NOT_FOUND.toAPICode()
        );
    }
}
```

## 📋 Response Format with APICodes

### Success Response
```json
{
  "status": 201,
  "data": {
    "id": "user-123",
    "email": "user@example.com"
  },
  "message": "User registered successfully",
  "timestamp": "2023-10-05T12:34:56.789Z"
}
```

### Error Response
```json
{
  "status": 400,
  "error": {
    "code": "INSUFFICIENT_FUNDS",
    "message": "Insufficient funds for transaction",
    "details": {
      "currentBalance": 50.0,
      "requiredAmount": 100.0
    }
  },
  "message": "Insufficient funds for transaction",
  "timestamp": "2023-10-05T12:34:56.789Z"
}
```

## 🚀 Advanced Usage

### Dynamic Message Resolution
```java
public enum LocalizedAPICode implements APICode {
    USER_CREATED(201) {
        @Override
        public String getDefaultMessage() {
            return MessageResolver.resolve("user.created");
        }
    };
    
    private final int httpStatus;
    
    LocalizedAPICode(int httpStatus) {
        this.httpStatus = httpStatus;
    }
    
    @Override
    public int getHttpStatus() {
        return httpStatus;
    }
}
```

### Code Registry Pattern
```java
public class APICodeRegistry {
    private static final Map<String, APICode> registry = new HashMap<>();
    
    static {
        register(BaseAPICode.values());
        register(CustomAPICode.values());
    }
    
    public static APICode getCode(String name) {
        return registry.get(name);
    }
    
    private static void register(APICode[] codes) {
        for (APICode code : codes) {
            registry.put(code.name(), code);
        }
    }
}
```

## ⚠️ Common Pitfalls

### 1. HTTP 204 Misuse
```java
// ❌ Wrong - HTTP 204 cannot have body
return APIResponse.success(null, BaseAPICode.NO_CONTENT);

// ✅ Correct - Use framework methods
return ResponseEntity.noContent().build();
```

### 2. Inconsistent Status Codes
```java
// ❌ Inconsistent - Same scenario, different codes
return APIResponse.error(error, BaseAPICode.BAD_REQUEST);
return APIResponse.error(error, BaseAPICode.VALIDATION_FAILED);

// ✅ Consistent - Same scenario, same code
return APIResponse.error(error, BaseAPICode.VALIDATION_FAILED);
return APIResponse.error(error, BaseAPICode.VALIDATION_FAILED);
```

### 3. Overly Specific Codes
```java
// ❌ Too specific - Hard to maintain
USER_NAME_TOO_SHORT(400),
USER_EMAIL_INVALID(400),
USER_PASSWORD_WEAK(400)

// ✅ Better - Use field errors with general code
VALIDATION_FAILED(400) // With field-specific details