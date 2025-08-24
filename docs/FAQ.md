
# Frequently Asked Questions

## 📋 General Questions

### Q: What is this library for?
**A:** This library provides standardized response formats for REST APIs, including success responses, error handling, validation errors, and consistent HTTP status code management across Java applications.

### Q: Which frameworks are supported?
**A:** The library supports Spring Boot, JAX-RS (Jersey, RESTEasy), and Servlet API. It can be extended to support other frameworks as well.

### Q: Is HTTP 204 supported?
**A:** No. HTTP 204 responses must not have a body, so this library will throw an exception if you try to create a response with HTTP 204. Use framework-native methods for HTTP 204 responses.

## 🚀 Installation & Setup

### Q: How do I add this library to my project?
**A:** Add the dependency to your build file:

```gradle
implementation 'com.yourcompany:api-response-library:x.x.x'
```

### Q: What are the minimum requirements?
**A:** Java 8+, Jackson databind, and one of the supported web frameworks.

### Q: How do I handle dependency conflicts?
**A:** The library has minimal dependencies. If you encounter conflicts, you can:
1. Check the specific version requirements
2. Use dependency exclusion in your build file
3. Contact support for specific conflict resolution

## 🎯 Usage Questions

### Q: How do I create a simple success response?
**A:** Use the factory method:
```java
return APIResponse.success(data, BaseAPICode.SUCCESS);
```

### Q: How do I handle validation errors?
**A:** Use the validation exception or builder:
```java
throw ValidationException.builder(BaseAPICode.VALIDATION_FAILED)
    .fieldError("email", "INVALID", "Must be valid email")
    .build();
```

### Q: Can I use custom HTTP status codes?
**A:** Yes, implement the `APICode` interface:
```java
public enum CustomAPICode implements APICode {
    MY_CUSTOM_CODE(418);
    // ...
}
```

## 🔧 Configuration

### Q: How do I configure global exception handling?
**A:** Extend `BaseResponseAdapter` in your exception handler:

```java
@ControllerAdvice
public class GlobalHandler extends BaseResponseAdapter {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception ex) {
        APIResponse<?> response = handleException(ex);
        return ResponseEntity.status(response.status()).body(response);
    }
}
```

### Q: How do I customize error messages?
**A:** You can:
1. Pass custom messages to exceptions
2. Override `getDefaultMessage()` in custom APICodes
3. Use message resolution with internationalization

## 🐛 Troubleshooting

### Q: I'm getting "HTTP 204 cannot have response body" error
**A:** This is intentional. Use framework methods for HTTP 204:
```java
// ✅ Correct
return ResponseEntity.noContent().build();

// ❌ Incorrect
return APIResponse.success(null, BaseAPICode.NO_CONTENT);
```

### Q: My custom exception isn't being handled properly
**A:** Ensure your exception extends one of the base exception classes or register it in your custom exception handler.

### Q: Jackson can't serialize my response
**A:** Make sure:
1. Your data objects are serializable
2. You have Jackson configured properly
3. You're not trying to serialize null data with HTTP 204

## 🏗️ Best Practices

### Q: Should I use builders or factory methods?
**A:** Use factory methods for simple cases, builders for complex responses with multiple field errors or custom metadata.

### Q: How should I handle sensitive data in errors?
**A:** Never expose sensitive information. Use generic error messages and sanitize exceptions in production.

### Q: Should I create custom exception types for every scenario?
**A:** No. Use existing exception types for common scenarios and create custom ones only for truly unique domain-specific cases.

## 🔄 Migration

### Q: How do I migrate from our current response format?
**A:** Follow these steps:
1. Add the library dependency
2. Update exception handling to use library exceptions
3. Replace response creation with APIResponse methods
4. Test thoroughly
5. Gradually migrate endpoints

### Q: Will this break existing clients?
**A:** If you maintain the same HTTP status codes and similar response structures, clients should not break. Test with your specific clients.

## 📊 Performance

### Q: What's the performance impact?
**A:** The library is lightweight and optimized. Exception handling has some overhead, but it's comparable to custom implementations.

### Q: How can I optimize response size?
**A:** Use compression, minimize included data, and avoid unnecessary metadata in responses.

## 🔒 Security

### Q: How do I prevent information leakage in errors?
**A:** Use a sanitizing exception handler in production:
```java
public class SanitizingHandler extends BaseResponseAdapter {
    @Override
    protected APIResponse<?> handleException(Exception ex) {
        if (isProduction()) {
            ex = sanitizeException(ex);
        }
        return super.handleException(ex);
    }
}
```

### Q: Are there any security vulnerabilities?
**A:** The library follows security best practices. Always keep it updated and monitor for security advisories.

## 🧪 Testing

### Q: How do I test responses?
**A:** Use the testing utilities:
```java
@Test
void testSuccessResponse() {
    APIResponse<User> response = controller.getUser("123");
    APIResponseTestUtils.assertSuccessResponse(response, 200);
}
```

### Q: How do I mock exception scenarios?
**A:** Mock the service to throw specific exceptions:
```java
when(userService.getUser(any())).thenThrow(
    new ResourceNotFoundException(...)
);
```

## 🌐 Internationalization

### Q: How do I support multiple languages?
**A:** Implement localized APICodes or use a message resolver in your exception handler.

### Q: Can I use different messages for the same error code?
**A:** Yes, by overriding the message based on locale or other context.

## 🔧 Extension

### Q: How do I add custom fields to responses?
**A:** Extend the APIResponse class or use the metadata support in builders.

### Q: Can I support other serialization formats?
**A:** Yes, implement custom serializers for XML, Protobuf, etc.

## 📈 Monitoring

### Q: How do I add metrics?
**A:** Use aspect-oriented programming or extend the response adapter to collect metrics.

### Q: How do I log responses?
**A:** Add logging in your exception handler or use response interceptors.

## 🤝 Community & Support

### Q: Where can I get help?
**A:** Check the GitHub issues, documentation, or create a new issue for specific problems.

### Q: How can I contribute?
**A:** Fork the repository, make improvements, and submit pull requests. See CONTRIBUTING.md for guidelines.

### Q: Are feature requests welcome?
**A:** Yes! Please create GitHub issues for feature requests and include use cases.

## 🔄 Versioning

### Q: What's the versioning strategy?
**A:** Semantic Versioning (SemVer): MAJOR.MINOR.PATCH

### Q: How often are updates released?
**A:** Regular updates for bug fixes, with major releases every few months for new features.

### Q: How do I stay updated?
**A:** Watch the GitHub repository or subscribe to release notifications.