# Framework Integration Guide

## 📋 Overview

This guide covers integrating the API Response Library with various Java frameworks including Spring Boot, JAX-RS, and Servlet API.

## 🍃 Spring Boot Integration

### Global Exception Handler

```java
import com.github.vikunalabs.lib.apicore.integration.BaseResponseAdapter;
import com.github.vikunalabs.lib.apicore.model.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class APIExceptionHandler extends BaseResponseAdapter {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleAnyException(Exception ex) {
        APIResponse<?> response = handleException(ex);
        return ResponseEntity.status(response.status()).body(response);
    }
}
```

### Controller Examples

**Success Responses:**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<User>> getUser(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(APIResponse.success(user, BaseAPICode.SUCCESS));
    }

    @PostMapping
    public ResponseEntity<APIResponse<User>> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(APIResponse.success(createdUser, BaseAPICode.RESOURCE_CREATED));
    }
}
```

**Error Handling in Controllers:**
```java
@DeleteMapping("/{id}")
public ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable String id) {
    try {
        userService.deleteUser(id);
        return ResponseEntity.ok(APIResponse.success(null, BaseAPICode.RESOURCE_DELETED));
    } catch (ResourceNotFoundException ex) {
        throw ex; // Let global handler process
    }
}
```

### Validation Integration

```java
@PostMapping("/register")
public ResponseEntity<APIResponse<User>> registerUser(@Valid @RequestBody RegisterRequest request) {
    // Validation passed, process request
    User user = userService.register(request);
    return ResponseEntity.ok(APIResponse.success(user, BaseAPICode.USER_CREATED));
}
```

## 🌐 JAX-RS Integration

### Exception Mapper

```java
import com.github.vikunalabs.lib.apicore.integration.BaseResponseAdapter;
import com.github.vikunalabs.lib.apicore.model.APIResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper extends BaseResponseAdapter 
    implements ExceptionMapper<Throwable> {
    
    @Override
    public Response toResponse(Throwable throwable) {
        APIResponse<?> response = handleException(throwable);
        return Response.status(response.status()).entity(response).build();
    }
}
```

### Resource Examples

```java
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") String id) {
        User user = userService.getUserById(id);
        APIResponse<User> response = APIResponse.success(user, BaseAPICode.SUCCESS);
        return Response.ok(response).build();
    }

    @POST
    public Response createUser(User user) {
        User createdUser = userService.createUser(user);
        APIResponse<User> response = APIResponse.success(createdUser, BaseAPICode.RESOURCE_CREATED);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
```

## 🖥️ Servlet API Integration

### Exception Filter

```java
import com.github.vikunalabs.lib.apicore.integration.BaseResponseAdapter;
import com.github.vikunalabs.lib.apicore.model.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.*;
import java.io.IOException;

public class APIExceptionFilter extends BaseResponseAdapter implements Filter {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            handleServletException(ex, (HttpServletResponse) response);
        }
    }
    
    private void handleServletException(Exception ex, HttpServletResponse response) throws IOException {
        APIResponse<?> apiResponse = handleException(ex);
        
        response.setStatus(apiResponse.status());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
```

### Servlet Configuration

**web.xml:**
```xml
<filter>
    <filter-name>APIExceptionFilter</filter-name>
    <filter-class>com.yourapp.APIExceptionFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>APIExceptionFilter</filter-name>
    <url-pattern>/api/*</url-pattern>
</filter-mapping>
```

## 🔧 Spring Configuration

### Response Entity Configuration

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public APIExceptionHandler apiExceptionHandler() {
        return new APIExceptionHandler();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Ensure Jackson is configured properly
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
```

### Custom Serialization (Optional)

```java
@Configuration
public class JacksonConfig {

    @Bean
    public Module apiResponseModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(APIResponse.class, new APIResponseSerializer());
        return module;
    }
}
```

## 🎯 Framework-Specific Best Practices

### Spring Boot
- Use `@ControllerAdvice` for global exception handling
- Lever Spring's validation framework with `@Valid`
- Use `ResponseEntity` for full control over HTTP responses
- Consider using Spring's `ProblemDetail` for standard compliance

### JAX-RS
- Register exception mappers as `@Provider` classes
- Use JAX-RS native validation annotations
- Consider CDI injection for service components

### Servlet API
- Configure filters in `web.xml` or using `@WebFilter`
- Handle character encoding properly
- Consider using Servlet 3.0+ async support

## 🚀 Performance Considerations

### Response Caching
```java
// Spring Boot - Add cache headers
@GetMapping("/{id}")
public ResponseEntity<APIResponse<User>> getUser(@PathVariable String id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
        .body(APIResponse.success(user, BaseAPICode.SUCCESS));
}
```

### Compression
Ensure your web server or framework is configured for response compression:
- Spring Boot: `server.compression.enabled=true`
- Tomcat: `compression="on"` in server.xml
- Nginx: `gzip on;`

## 🔒 Security Considerations

### Error Message Sanitization
```java
@ControllerAdvice
public class SanitizingExceptionHandler extends BaseResponseAdapter {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception ex) {
        // Sanitize error messages in production
        if (isProduction()) {
            ex = sanitizeException(ex);
        }
        APIResponse<?> response = handleException(ex);
        return ResponseEntity.status(response.status()).body(response);
    }
    
    private Exception sanitizeException(Exception ex) {
        // Remove sensitive information from error messages
        return ex;
    }
}
```

## 📊 Monitoring and Logging

### Structured Logging
```java
@ControllerAdvice
public class LoggingExceptionHandler extends BaseResponseAdapter {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception ex) {
        logger.error("API Exception occurred: {}", ex.getMessage(), ex);
        
        APIResponse<?> response = handleException(ex);
        return ResponseEntity.status(response.status()).body(response);
    }
}
```

## 🧪 Testing Integration

### Spring Boot Test
```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Test
    void getUser_ReturnsSuccessResponse() throws Exception {
        mockMvc.perform(get("/api/users/123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.id").value("123"));
    }
}
```

### JUnit 5 with Mockito
```java
@Test
void createUser_ThrowsValidationException() {
    when(userService.createUser(any())).thenThrow(
        new ValidationException(BaseAPICode.VALIDATION_FAILED, "Invalid data")
    );

    assertThrows(ValidationException.class, () -> 
        userController.createUser(new UserRequest()));
}
```

## 🔄 Migration Tips

### From Legacy Error Handling
1. Replace custom error response classes with `APIResponse`
2. Convert exception handling to use library exceptions
3. Update tests to expect new response format
4. Gradually migrate endpoints while maintaining backward compatibility

### Version Compatibility
- Ensure Jackson version compatibility
- Check Spring Boot version support
- Verify Servlet API compatibility
