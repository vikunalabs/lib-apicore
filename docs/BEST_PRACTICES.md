
# Best Practices Guide

## 📋 Overview

This guide covers implementation best practices, anti-patterns to avoid, and recommendations for using the API Response Library effectively.

## ✅ Do's and Don'ts

### ✅ Do:

**Use Specific Exception Types**
```java
// ✅ Good - Specific exception type
throw new ResourceNotFoundException(
    BaseAPICode.RESOURCE_NOT_FOUND,
    "User not found",
    userId
);

// ❌ Bad - Generic exception
throw new RuntimeException("User not found: " + userId);
```

**Provide Meaningful Error Messages**
```java
// ✅ Good - Helpful message
throw new ValidationException(
    BaseAPICode.VALIDATION_FAILED,
    "Email must be a valid format",
    fieldErrors
);

// ❌ Bad - Vague message
throw new ValidationException(
    BaseAPICode.VALIDATION_FAILED,
    "Invalid data",
    fieldErrors
);
```

**Include Relevant Debug Details**
```java
// ✅ Good - Include debugging context
throw new BusinessRuleViolationException(
    BaseAPICode.BUSINESS_RULE_VIOLATION,
    "Insufficient inventory",
    Map.of(
        "productId", productId,
        "available", currentStock,
        "requested", requestedQuantity
    )
);
```

### ❌ Don't:

**Don't Catch Just to Re-throw Generic Exceptions**
```java
// ❌ Bad - Loss of context
try {
    return userService.getUser(id);
} catch (ResourceNotFoundException e) {
    throw new RuntimeException("Error getting user"); // Context lost!
}

// ✅ Good - Preserve original exception
try {
    return userService.getUser(id);
} catch (ResourceNotFoundException e) {
    throw e; // Let it propagate
}
```

**Don't Expose Sensitive Information**
```java
// ❌ Bad - Exposes sensitive data
throw new ValidationException(
    BaseAPICode.VALIDATION_FAILED,
    "Invalid password: " + actualPassword, // ❌
    fieldErrors
);

// ✅ Good - Generic error message
throw new ValidationException(
    BaseAPICode.VALIDATION_FAILED,
    "Password does not meet requirements",
    fieldErrors
);
```

**Don't Use Exceptions for Control Flow**
```java
// ❌ Bad - Using exceptions for normal flow
try {
    userService.createUser(user);
} catch (ResourceAlreadyExistsException e) {
    return userService.getUserByEmail(user.getEmail()); // This is normal flow!
}

// ✅ Good - Check first, then act
if (userService.existsByEmail(user.getEmail())) {
    return userService.getUserByEmail(user.getEmail());
} else {
    return userService.createUser(user);
}
```

## 🎯 Response Format Guidelines

### Success Responses
```java
// ✅ Good - Complete success response
return APIResponse.success(user, BaseAPICode.USER_CREATED);

// ✅ Better - With additional context
return APIResponse.<User>builder()
    .status(BaseAPICode.USER_CREATED)
    .data(user)
    .message("User created successfully")
    .build();
```

### Error Responses
```java
// ✅ Good - Complete error response
return APIResponse.error(
    new APIError("VALIDATION_FAILED", "Invalid input"),
    BaseAPICode.VALIDATION_FAILED
);

// ✅ Better - With field errors
return APIResponse.<Void>builder()
    .status(BaseAPICode.VALIDATION_FAILED)
    .error(APIError.builder()
        .code("VALIDATION_FAILED")
        .message("Please fix the following errors")
        .fieldError("email", "INVALID_FORMAT", "Must be valid email")
        .build())
    .build();
```

## 🏗️ Architecture Patterns

### Layered Exception Handling

**Service Layer** - Throw domain-specific exceptions:
```java
@Service
public class UserService {
    
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException(
                BaseAPICode.USER_ALREADY_EXISTS,
                "User email already registered",
                user.getEmail()
            );
        }
        return userRepository.save(user);
    }
}
```

**Controller Layer** - Convert to appropriate responses:
```java
@RestController
public class UserController {
    
    @PostMapping
    public ResponseEntity<APIResponse<User>> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(APIResponse.success(createdUser, BaseAPICode.USER_CREATED));
        } catch (ResourceAlreadyExistsException e) {
            throw e; // Let global handler process
        }
    }
}
```

**Global Handler** - Convert exceptions to standardized responses:
```java
@ControllerAdvice
public class GlobalExceptionHandler extends BaseResponseAdapter {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception ex) {
        APIResponse<?> response = handleException(ex);
        return ResponseEntity.status(response.status()).body(response);
    }
}
```

## 🔧 Validation Strategies

### Input Validation
```java
// ✅ Good - Validate early and comprehensively
@PostMapping
public ResponseEntity<APIResponse<User>> createUser(@Valid @RequestBody UserRequest request) {
    // Additional business validation
    if (!passwordService.meetsComplexity(request.getPassword())) {
        throw new ValidationException(
            BaseAPICode.VALIDATION_FAILED,
            "Password does not meet complexity requirements",
            List.of(new FieldError("password", "INVALID_COMPLEXITY", "Must contain special characters"))
        );
    }
    
    User user = userService.createUser(request.toUser());
    return ResponseEntity.ok(APIResponse.success(user, BaseAPICode.USER_CREATED));
}
```

### Business Rule Validation
```java
// ✅ Good - Validate business rules in service layer
@Service
public class OrderService {
    
    public Order createOrder(OrderRequest request) {
        // Check inventory
        if (productService.getStock(request.getProductId()) < request.getQuantity()) {
            throw new BusinessRuleViolationException(
                BaseAPICode.BUSINESS_RULE_VIOLATION,
                "Insufficient inventory",
                Map.of(
                    "productId", request.getProductId(),
                    "requested", request.getQuantity(),
                    "available", productService.getStock(request.getProductId())
                )
            );
        }
        
        return orderRepository.save(createOrder(request));
    }
}
```

## 📊 Performance Considerations

### Exception Cost Awareness
```java
// ❌ Bad - Expensive exception for normal flow
try {
    return userRepository.findByEmail(email);
} catch (EmptyResultDataAccessException e) {
    throw new ResourceNotFoundException(...); // This is expensive!
}

// ✅ Good - Use optional return for normal flow
return userRepository.findByEmail(email)
    .orElseThrow(() -> new ResourceNotFoundException(...)); // Only created when needed
```

### Response Size Optimization
```java
// ✅ Good - Minimize response size for lists
@GetMapping
public ResponseEntity<APIResponse<List<User>>> getUsers(
        @RequestParam(defaultValue = "20") int limit,
        @RequestParam(defaultValue = "0") int offset) {
    
    List<User> users = userService.getUsers(limit, offset);
    return ResponseEntity.ok(APIResponse.success(users, BaseAPICode.SUCCESS));
}

// ✅ Better - Add pagination metadata
public ResponseEntity<APIResponse<PageResponse<User>>> getUsers(...) {
    Page<User> page = userService.getUsers(limit, offset);
    PageResponse<User> response = new PageResponse<>(page);
    return ResponseEntity.ok(APIResponse.success(response, BaseAPICode.SUCCESS));
}
```

## 🔒 Security Best Practices

### Error Message Sanitization
```java
// ✅ Good - Sanitize error messages in production
@ControllerAdvice
public class SanitizingExceptionHandler extends BaseResponseAdapter {
    
    private final boolean isProduction;
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception ex) {
        if (isProduction) {
            ex = sanitizeException(ex);
        }
        return super.handleException(ex);
    }
    
    private Exception sanitizeException(Exception ex) {
        // Remove stack traces and sensitive data
        return new GenericException("An error occurred");
    }
}
```

### Input Validation
```java
// ✅ Good - Comprehensive input validation
@PostMapping
public ResponseEntity<APIResponse<User>> createUser(@Valid @RequestBody UserRequest request) {
    // Additional security validation
    if (containsMaliciousContent(request.getName())) {
        throw new ValidationException(
            BaseAPICode.VALIDATION_FAILED,
            "Invalid input detected",
            List.of(new FieldError("name", "INVALID_CONTENT", "Contains invalid characters"))
        );
    }
    
    User user = userService.createUser(request.toUser());
    return ResponseEntity.ok(APIResponse.success(user, BaseAPICode.USER_CREATED));
}
```

## 🧪 Testing Strategies

### Unit Testing
```java
@Test
void createUser_ThrowsWhenEmailExists() {
    // Given
    UserRequest request = new UserRequest("existing@example.com", "password");
    when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
    
    // When/Then
    assertThrows(ResourceAlreadyExistsException.class, () -> 
        userService.createUser(request));
}

@Test
void createUser_ReturnsSuccessResponse() {
    // Given
    UserRequest request = new UserRequest("new@example.com", "password");
    User savedUser = new User("123", "new@example.com");
    when(userRepository.save(any())).thenReturn(savedUser);
    
    // When
    APIResponse<User> response = userController.createUser(request);
    
    // Then
    assertThat(response.status()).isEqualTo(201);
    assertThat(response.data().getEmail()).isEqualTo("new@example.com");
}
```

### Integration Testing
```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    
    @Test
    void createUser_Returns201() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"secret\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }
}
```

## 📈 Monitoring and Logging

### Structured Logging
```java
@ControllerAdvice
public class LoggingExceptionHandler extends BaseResponseAdapter {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception ex) {
        if (ex instanceof ClientErrorsBaseException) {
            logger.warn("Client error: {}", ex.getMessage());
        } else {
            logger.error("Server error: {}", ex.getMessage(), ex);
        }
        
        return super.handleException(ex);
    }
}
```

### Metrics Collection
```java
@ControllerAdvice
public class MetricsExceptionHandler extends BaseResponseAdapter {
    
    private final MeterRegistry meterRegistry;
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception ex) {
        // Record metrics
        meterRegistry.counter("api.errors", "type", ex.getClass().getSimpleName())
            .increment();
        
        return super.handleException(ex);
    }
}
```

## 🔄 Versioning and Compatibility

### Backward Compatibility
```java
// ✅ Good - Maintain backward compatibility
@Deprecated
public ResponseEntity<APIResponse<User>> oldEndpoint() {
    // Old response format
    return ResponseEntity.ok(APIResponse.success(user, LegacyAPICode.SUCCESS));
}

public ResponseEntity<APIResponse<User>> newEndpoint() {
    // New response format
    return ResponseEntity.ok(APIResponse.success(user, BaseAPICode.SUCCESS));
}
```

### API Evolution
```java
// ✅ Good - Add new fields without breaking clients
public class UserResponse {
    private String id;
    private String email;
    private String name;
    
    // New field - optional for backward compatibility
    private Instant createdAt;
    
    // Getters and setters
}
```

## 🚀 Performance Optimization

### Response Caching
```java
// ✅ Good - Add caching headers
@GetMapping("/{id}")
public ResponseEntity<APIResponse<User>> getUser(@PathVariable String id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
        .eTag(user.getVersion().toString())
        .body(APIResponse.success(user, BaseAPICode.SUCCESS));
}
```

### Compression
```java
// ✅ Good - Enable compression in configuration
# application.yml
server:
  compression:
    enabled: true
    mime-types: application/json
    min-response-size: 1024
```

## 📚 Documentation

### API Documentation
```java
@Operation(summary = "Create a new user")
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "User created successfully",
        content = @Content(schema = @Schema(implementation = UserResponse.class))),
    @ApiResponse(responseCode = "400", description = "Validation failed",
        content = @Content(schema = @Schema(implementation = APIErrorResponse.class))),
    @ApiResponse(responseCode = "409", description = "User already exists")
})
@PostMapping
public ResponseEntity<APIResponse<User>> createUser(@RequestBody UserRequest request) {
    // implementation
}
```

### Error Documentation
```java
/**
 * @throws ResourceNotFoundException if user with given ID doesn't exist
 * @throws ValidationException if input validation fails
 * @throws BusinessRuleViolationException if business rules are violated
 */
public User getUser(String id) {
    // implementation
}
```

By following these best practices, you'll create robust, maintainable, and consistent APIs that are easy to use, monitor, and evolve over time.