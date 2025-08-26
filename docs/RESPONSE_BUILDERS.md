# Response Builders Guide

## 📋 Overview

The library provides fluent builders for creating standardized API responses with comprehensive validation and type safety.

## 🎯 Success Responses

### Factory Methods

```java
// Basic success with data
return APIResponse.success(user, BaseAPICode.USER_CREATED);

// Success with custom message
return APIResponse.success(user, BaseAPICode.SUCCESS, "User operation completed");
```

### Builder Pattern

**Success:**
```java
return APIResponse.<User>builder()
    .status(BaseAPICode.USER_CREATED) // Auto-sets message from APICode
    .build();
```

**Simple Success:**
```java
return APIResponse.<User>builder()
    .status(BaseAPICode.USER_CREATED) // Auto-sets message from APICode
    .data(user)
    .build();
```

**Custom Success:**
```java
return APIResponse.<User>builder()
    .status(BaseAPICode.SUCCESS)
    .data(user)
    .message("Custom success message")
    .timestamp(Instant.now().minusMinutes(5))
    .build();
```

## 🚨 Error Responses

### Factory Methods

```java
// Simple error
APIError error = new APIError("NOT_FOUND", "User not found");
return APIResponse.error(error, BaseAPICode.RESOURCE_NOT_FOUND);

// Error with custom message
return APIResponse.error(error, BaseAPICode.VALIDATION_FAILED, "Validation failed");
```

### Builder Pattern

**Simple Error:**
```java
return APIResponse.<Void>builder()
    .status(BaseAPICode.VALIDATION_FAILED)
    .error(APIError.builder()
        .code("VALIDATION_FAILED")
        .message("Invalid input")
        .build())
    .build();
```

**Error with Custom Message:**
```java
return APIResponse.<Void>builder()
    .status(BaseAPICode.NOT_FOUND)
    .error(APIError.builder()
        .code("USER_NOT_FOUND")
        .message("User not found")
        .build())
    .message("Custom error message") // Override auto-generated message
    .build();
```

## 📋 Validation Errors

### Traditional Approach

```java
List<FieldError> fieldErrors = List.of(
    new FieldError("email", "INVALID_FORMAT", "Must be valid email")
);
APIError validationError = new APIError("VALIDATION_FAILED", "Invalid input", fieldErrors);
return APIResponse.error(validationError, BaseAPICode.VALIDATION_FAILED);
```

### Fluent Builder (Recommended)

```java
return APIResponse.<Void>builder()
    .status(BaseAPICode.VALIDATION_FAILED)
    .error(APIError.builder()
        .code("VALIDATION_FAILED")
        .message("Invalid input")
        .fieldError("email", "INVALID_FORMAT", "Must be valid email")
        .fieldError("password", "TOO_SHORT", "Password must be at least 8 characters", "short")
        .build())
    .build();
```

### Advanced FieldError Builder

```java
return APIResponse.<Void>builder()
    .status(BaseAPICode.VALIDATION_FAILED)
    .error(APIError.builder()
        .code("VALIDATION_FAILED")
        .message("Invalid input")
        .fieldError(FieldError.builder()
            .field("email")
            .code("INVALID_FORMAT")
            .message("Must be valid email")
            .rejectedValue("invalid-email")
            .build())
        .fieldError(FieldError.builder()
            .field("password")
            .code("TOO_SHORT")
            .message("Password must be at least 8 characters")
            .rejectedValue("short")
            .build())
        .build())
    .build();
```

## 🏗️ APIError Builder Methods

### Basic Error Building

```java
APIError.builder()
    .code("INTERNAL_ERROR")
    .message("Something went wrong")
    .details("Additional debug information")
    .build();
```

### With Field Errors

```java
APIError.builder()
    .code("VALIDATION_FAILED")
    .message("Invalid input data")
    .fieldError("username", "REQUIRED", "Username is required")
    .fieldError("email", "INVALID", "Must be valid email", "invalid@")
    .fieldErrors(existingFieldErrorsList)
    .build();
```

### With Additional Details

```java
APIError.builder()
    .code("BUSINESS_RULE_VIOLATION")
    .message("Insufficient balance")
    .detail("currentBalance", 50.0)
    .detail("requiredAmount", 100.0)
    .detail("accountId", "acc-123")
    .build();
```

## 🔄 Chaining Builders

```java
// Complex response with chained builders
return APIResponse.<Void>builder()
    .status(BaseAPICode.VALIDATION_FAILED)
    .message("Registration validation failed")
    .error(APIError.builder()
        .code("VALIDATION_FAILED")
        .message("Please fix the following errors")
        .fieldError(FieldError.builder()
            .field("email")
            .code("DUPLICATE")
            .message("Email already registered")
            .rejectedValue("user@example.com")
            .build())
        .fieldError(FieldError.builder()
            .field("password")
            .code("COMPLEXITY")
            .message("Must contain special character")
            .rejectedValue("simplepassword")
            .build())
        .detail("suggestion", "Try a different email or reset password")
        .build())
    .timestamp(Instant.now())
    .build();
```

## ✅ Validation Rules

The builders enforce these validations:

1. **Mutual Exclusivity**: `data` and `error` cannot both be set
2. **HTTP Status Validation**: Prevents invalid status codes
3. **HTTP 204 Block**: Cannot use builders for no-content responses
4. **Required Fields**: Message and timestamp are mandatory
5. **Field Error Consistency**: Field errors require field, code, and message

## 🎯 Best Practices

1. **Use builders** for complex responses with multiple field errors
2. **Use factory methods** for simple success/error responses
3. **Leverage APICode messages** for consistency across endpoints
4. **Include rejected values** in field errors for better client debugging
5. **Use details map** for additional context in error responses

## Example Response Outputs

### Success Response
```json
{
  "status": 201,
  "data": {
    "id": "user-123",
    "email": "user@example.com",
    "name": "John Doe"
  },
  "message": "user created",
  "timestamp": "2023-10-05T12:34:56.789Z"
}
```

### Validation Error Response
```json
{
  "status": 400,
  "error": {
    "code": "VALIDATION_FAILED",
    "message": "Invalid input data",
    "fieldErrors": [
      {
        "field": "email",
        "code": "INVALID_FORMAT",
        "message": "Must be valid email",
        "rejectedValue": "invalid-email"
      }
    ]
  },
  "message": "Invalid input data",
  "timestamp": "2023-10-05T12:34:56.789Z"
}
```