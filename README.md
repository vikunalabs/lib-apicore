# ✅ **Revised README.md**

```markdown
# API Response Standardization Library

A lightweight Java library for standardizing REST API responses across projects with comprehensive builder support.

## Features

- ✅ Standardized success/error response format
- ✅ OpenAPI/Swagger documentation support  
- ✅ HTTP status code validation (blocks HTTP 204)
- ✅ Type-safe generic responses
- ✅ Jackson serialization support
- ✅ Fluent builder pattern for complex responses
- ✅ Comprehensive validation to prevent invalid states

## Installation

```gradle
implementation 'com.yourcompany:api-response-library:1.0.0'
```

## Quick Start

### Success Responses

**Factory Methods:**
```java
// Basic success with data
return APIResponse.success(user, BaseAPICode.USER_CREATED);

// Success with custom message
return APIResponse.success(user, BaseAPICode.SUCCESS, "User operation completed");
```

**Builder Pattern:**
```java
// Simple success with builder
return APIResponse.<User>builder()
    .status(BaseAPICode.USER_CREATED) // Auto-sets message from APICode
    .data(user)
    .build();

// Success with custom timestamp and message
return APIResponse.<User>builder()
    .status(BaseAPICode.SUCCESS)
    .data(user)
    .message("Custom success message")
    .timestamp(Instant.now().minusMinutes(5))
    .build();
```

### Error Responses

**Factory Methods:**
```java
// Simple error
APIError error = new APIError("NOT_FOUND", "User not found");
return APIResponse.error(error, BaseAPICode.RESOURCE_NOT_FOUND);

// Error with custom message
return APIResponse.error(error, BaseAPICode.VALIDATION_FAILED, "Validation failed");
```

**Builder Pattern:**
```java
// Simple error with builder
return APIResponse.<Void>builder()
    .status(BaseAPICode.VALIDATION_FAILED)
    .error(APIError.builder()
        .code("VALIDATION_FAILED")
        .message("Invalid input")
        .build())
    .build();

// Error with custom message
return APIResponse.<Void>builder()
    .status(BaseAPICode.NOT_FOUND)
    .error(APIError.builder()
        .code("USER_NOT_FOUND")
        .message("User not found")
        .build())
    .message("Custom error message") // Override auto-generated message
    .build();
```

### Validation Errors with Field Details

**Traditional Approach:**
```java
List<FieldError> fieldErrors = List.of(
    new FieldError("email", "INVALID_FORMAT", "Must be valid email")
);
APIError validationError = new APIError("VALIDATION_FAILED", "Invalid input", fieldErrors);
return APIResponse.error(validationError, BaseAPICode.VALIDATION_FAILED);
```

**Fluent Builder Approach (Recommended):**
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

**Advanced FieldError Builder:**
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

## Important: HTTP 204 No Content

**HTTP 204 No Content responses cannot use this library** as they must not have any response body. The library will throw `IllegalArgumentException` if you attempt to use HTTP 204.

### ✅ Correct Usage

```java
// Spring Boot
return ResponseEntity.noContent().build();

// JAX-RS  
return Response.noContent().build();

// Servlet
response.setStatus(204);
response.getWriter().flush();
```

### ❌ Incorrect Usage (Will Throw Exception)

```java
// These will all throw IllegalArgumentException:
return APIResponse.success(null, BaseAPICode.NO_CONTENT);
return APIResponse.success(data, BaseAPICode.NO_CONTENT);
return APIResponse.<Void>builder().status(BaseAPICode.NO_CONTENT).build();
```

For delete operations that need to return data, use HTTP 200 instead:

```java
return APIResponse.success(deletionResult, BaseAPICode.RESOURCE_DELETED);
```

## Available APICodes

Use `BaseAPICode` enum for standard HTTP status mappings:

- `SUCCESS(200)` - Generic success
- `RESOURCE_CREATED(201)` - Resource created
- `RESOURCE_DELETED(200)` - Resource deleted (with optional data)
- `VALIDATION_FAILED(400)` - Validation error
- `RESOURCE_NOT_FOUND(404)` - Resource not found
- `INTERNAL_ERROR(500)` - Server error

## Custom APICodes

Implement the `APICode` interface for custom status codes:

```java
public enum CustomAPICode implements APICode {
    CUSTOM_SUCCESS(200),
    CUSTOM_ERROR(400);
    
    private final int httpStatus;
    
    CustomAPICode(int httpStatus) {
        this.httpStatus = httpStatus;
    }
    
    @Override
    public int getHttpStatus() {
        return httpStatus;
    }
}
```

## Response Format

### Success Response
```json
{
  "status": 200,
  "data": {
    "id": 123,
    "name": "John Doe",
    "email": "john@example.com"
  },
  "message": "user created",
  "timestamp": "2023-10-05T12:34:56.789Z"
}
```

### Error Response
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
      },
      {
        "field": "password",
        "code": "TOO_SHORT",
        "message": "Password must be at least 8 characters",
        "rejectedValue": "short"
      }
    ],
    "details": "Additional debug information"
  },
  "message": "Invalid input data",
  "timestamp": "2023-10-05T12:34:56.789Z"
}
```

### Empty Success Response (for operations like DELETE)
```json
{
  "status": 200,
  "data": null,
  "message": "resource deleted",
  "timestamp": "2023-10-05T12:34:56.789Z"
}
```

## Validation & Error Handling

The library includes comprehensive validation:

- ✅ Prevents HTTP 204 usage with clear error messages
- ✅ Ensures data and error are mutually exclusive
- ✅ Validates status code ranges (success vs error)
- ✅ Requires non-null message and timestamp
- ✅ Provides immediate feedback through exceptions

## Best Practices

1. **Use factory methods** for simple cases
2. **Use builder pattern** for complex responses with field errors
3. **Never use HTTP 204** with this library - use framework methods instead
4. **Leverage APICode auto-messages** for consistency
5. **Use FieldError builders** for clean validation error construction

## Framework Integration

### Spring Boot Example
```java
@PostMapping("/users")
public ResponseEntity<APIResponse<User>> createUser(@RequestBody User user) {
    try {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(APIResponse.success(createdUser, BaseAPICode.USER_CREATED));
    } catch (ValidationException e) {
        APIError error = APIError.builder()
            .code("VALIDATION_FAILED")
            .message("Invalid user data")
            .fieldErrors(e.getFieldErrors())
            .build();
        return ResponseEntity.badRequest()
            .body(APIResponse.error(error, BaseAPICode.VALIDATION_FAILED));
    }
}

@DeleteMapping("/users/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build(); // ✅ Correct HTTP 204 handling
}
```

## License

MIT License - feel free to use in your projects!
```

## 🎯 Key Improvements:

1. **✅ Comprehensive Builder Examples**: Added multiple builder pattern examples
2. **✅ Better Organization**: Structured sections for different use cases
3. **✅ More Practical Examples**: Real-world usage scenarios
4. **✅ Enhanced Error Examples**: Showcased field error builders
5. **✅ Framework Integration**: Added Spring Boot examples
6. **✅ Best Practices Section**: Clear guidance on when to use what
7. **✅ Better JSON Examples**: More realistic response examples
8. **✅ Validation Section**: Explains the comprehensive validation features

The README now fully reflects the powerful builder capabilities you've implemented! 🚀