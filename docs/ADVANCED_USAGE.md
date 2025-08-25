# Advanced Usage Guide

## 📋 Overview

This guide covers advanced features, customization options, and extension points for the API Response Library.

## 🎯 Custom Serialization

### Custom APIResponse Serializer

```java
public class CustomAPIResponseSerializer extends JsonSerializer<APIResponse<?>> {
    
    @Override
    public void serialize(APIResponse<?> value, JsonGenerator gen, SerializerProvider serializers) 
            throws IOException {
        
        gen.writeStartObject();
        gen.writeNumberField("httpStatus", value.status());
        
        if (value.data() != null) {
            gen.writeObjectField("payload", value.data());
        }
        
        if (value.error() != null) {
            gen.writeObjectField("error", value.error());
        }
        
        gen.writeStringField("message", value.message());
        gen.writeStringField("timestamp", value.timestamp().toString());
        gen.writeEndObject();
    }
}
```

### Register Custom Serializer

```java
@Configuration
public class JacksonConfig {
    
    @Bean
    public Module apiResponseModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(APIResponse.class, new CustomAPIResponseSerializer());
        return module;
    }
}
```

## 🔧 Custom Exception Handling

### Extended BaseResponseAdapter

```java
public class CustomResponseAdapter extends BaseResponseAdapter {
    
    private final Map<Class<?>, APICode> exceptionToCodeMap = new HashMap<>();
    
    public CustomResponseAdapter() {
        // Map exceptions to specific APICodes
        exceptionToCodeMap.put(ResourceNotFoundException.class, BaseAPICode.RESOURCE_NOT_FOUND);
        exceptionToCodeMap.put(ValidationException.class, BaseAPICode.VALIDATION_FAILED);
        // Add more mappings...
    }
    
    @Override
    protected APICode resolveAPICode(Exception ex) {
        return exceptionToCodeMap.getOrDefault(ex.getClass(), BaseAPICode.INTERNAL_ERROR);
    }
    
    @Override
    protected APIResponse<?> handleException(Exception ex) {
        APIResponse<?> response = super.handleException(ex);
        
        // Add custom logging or metrics
        logException(ex, response);
        
        return response;
    }
}
```

## 🌐 Internationalization (i18n)

### Message Resolution

```java
public class LocalizedAPICode implements APICode {
    
    private final int httpStatus;
    private final String messageKey;
    
    public LocalizedAPICode(int httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
    
    @Override
    public int getHttpStatus() {
        return httpStatus;
    }
    
    @Override
    public String getDefaultMessage() {
        return MessageSourceHolder.getMessage(messageKey);
    }
}

// Usage
public enum CustomAPICode implements LocalizedAPICode {
    USER_CREATED(201, "user.created.message"),
    VALIDATION_FAILED(400, "validation.failed.message");
    
    CustomAPICode(int httpStatus, String messageKey) {
        super(httpStatus, messageKey);
    }
}
```

### Locale-Aware Responses

```java
public class LocalizedResponseAdapter extends BaseResponseAdapter {
    
    private final MessageSource messageSource;
    
    @Override
    protected APIResponse<?> handleException(Exception ex) {
        APIResponse<?> response = super.handleException(ex);
        
        // Resolve message based on locale
        String localizedMessage = resolveLocalizedMessage(ex, response);
        
        return APIResponse.builder()
            .status(response.status())
            .data(response.data())
            .error(response.error())
            .message(localizedMessage)
            .timestamp(response.timestamp())
            .build();
    }
    
    private String resolveLocalizedMessage(Exception ex, APIResponse<?> response) {
        Locale locale = LocaleContextHolder.getLocale();
        if (ex instanceof LocalizedException) {
            return messageSource.getMessage(
                ((LocalizedException) ex).getMessageKey(),
                ((LocalizedException) ex).getArgs(),
                locale
            );
        }
        return response.message();
    }
}
```

## 📊 Pagination Support

### Pagination Response Wrapper

```java
public class PageResponse<T> {
    private List<T> content;
    private PageMetadata metadata;
    
    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.metadata = new PageMetadata(
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
    
    // Getters, equals, hashCode, toString
    
    public static class PageMetadata {
        private final int page;
        private final int size;
        private final long totalElements;
        private final int totalPages;
        
        // Constructor, getters, etc.
    }
}

// Usage
@GetMapping
public ResponseEntity<APIResponse<PageResponse<User>>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
    
    Page<User> userPage = userService.getUsers(page, size);
    PageResponse<User> response = new PageResponse<>(userPage);
    
    return ResponseEntity.ok(APIResponse.success(response, BaseAPICode.SUCCESS));
}
```

## 🔍 Enhanced Validation

### Bean Validation Integration

```java
public class ValidatedAPIResponse<T> extends APIResponse<T> {
    
    private final Set<ConstraintViolation<?>> violations;
    
    public ValidatedAPIResponse(APIResponse<T> response, Set<ConstraintViolation<?>> violations) {
        super(response.status(), response.data(), response.error(), response.message(), response.timestamp());
        this.violations = violations;
    }
    
    public boolean isValid() {
        return violations.isEmpty();
    }
    
    public Set<ConstraintViolation<?>> getViolations() {
        return violations;
    }
}

// Usage with validation
public <T> ValidatedAPIResponse<T> validateResponse(APIResponse<T> response) {
    Set<ConstraintViolation<?>> violations = validator.validate(response);
    return new ValidatedAPIResponse<>(response, violations);
}
```

## 📈 Metrics and Monitoring

### Response Metrics Collector

```java
public class MetricsResponseInterceptor {
    
    private final MeterRegistry meterRegistry;
    private final Timer responseTimer;
    
    public <T> APIResponse<T> intercept(Supplier<APIResponse<T>> supplier, String endpoint) {
        return responseTimer.record(() -> {
            try {
                APIResponse<T> response = supplier.get();
                recordMetrics(response, endpoint, "success");
                return response;
            } catch (Exception ex) {
                recordMetrics(null, endpoint, "error");
                throw ex;
            }
        });
    }
    
    private void recordMetrics(APIResponse<?> response, String endpoint, String status) {
        meterRegistry.counter("api.responses", 
            "endpoint", endpoint,
            "status", status,
            "http_status", response != null ? String.valueOf(response.status()) : "error"
        ).increment();
    }
}

// Usage
@GetMapping("/{id}")
public ResponseEntity<APIResponse<User>> getUser(@PathVariable String id) {
    APIResponse<User> response = metricsInterceptor.intercept(
        () -> APIResponse.success(userService.getUser(id), BaseAPICode.SUCCESS),
        "GET /api/users/{id}"
    );
    return ResponseEntity.ok(response);
}
```

## 🔒 Security Enhancements

### Response Sanitization

```java
public class SanitizingResponseAdapter extends BaseResponseAdapter {
    
    private final SensitiveDataScrubber scrubber;
    
    @Override
    protected APIResponse<?> handleException(Exception ex) {
        APIResponse<?> response = super.handleException(ex);
        
        if (response.error() != null) {
            APIError sanitizedError = sanitizeError(response.error());
            return APIResponse.builder()
                .status(response.status())
                .error(sanitizedError)
                .message(sanitizeMessage(response.message()))
                .timestamp(response.timestamp())
                .build();
        }
        
        return response;
    }
    
    private APIError sanitizeError(APIError error) {
        return APIError.builder()
            .code(error.code())
            .message(sanitizeMessage(error.message()))
            .details(scrubber.scrub(error.details()))
            .fieldErrors(error.fieldErrors().stream()
                .map(this::sanitizeFieldError)
                .collect(Collectors.toList()))
            .build();
    }
}
```

## 🧪 Testing Utilities

### Response Test Helpers

```java
public class APIResponseTestUtils {
    
    public static <T> void assertSuccessResponse(APIResponse<T> response, int expectedStatus) {
        assertThat(response.status()).isEqualTo(expectedStatus);
        assertThat(response.data()).isNotNull();
        assertThat(response.error()).isNull();
        assertThat(response.message()).isNotBlank();
        assertThat(response.timestamp()).isBeforeOrEqualTo(Instant.now());
    }
    
    public static void assertErrorResponse(APIResponse<?> response, int expectedStatus, String expectedErrorCode) {
        assertThat(response.status()).isEqualTo(expectedStatus);
        assertThat(response.data()).isNull();
        assertThat(response.error()).isNotNull();
        assertThat(response.error().code()).isEqualTo(expectedErrorCode);
        assertThat(response.message()).isNotBlank();
    }
    
    public static void assertFieldError(APIResponse<?> response, String fieldName, String errorCode) {
        assertThat(response.error().fieldErrors())
            .anyMatch(fe -> fe.field().equals(fieldName) && fe.code().equals(errorCode));
    }
}

// Usage in tests
@Test
void createUser_ReturnsSuccessResponse() {
    APIResponse<User> response = userController.createUser(request);
    
    APIResponseTestUtils.assertSuccessResponse(response, 201);
    assertThat(response.data().getEmail()).isEqualTo("test@example.com");
}
```

## 🔄 Async and Reactive Support

### Reactive Response Handling

```java
@RestController
public class ReactiveUserController {
    
    @GetMapping("/{id}")
    public Mono<ResponseEntity<APIResponse<User>>> getUser(@PathVariable String id) {
        return userService.getUserReactive(id)
            .map(user -> ResponseEntity.ok(APIResponse.success(user, BaseAPICode.SUCCESS)))
            .onErrorResume(ResourceNotFoundException.class, ex -> 
                Mono.just(ResponseEntity.status(404)
                    .body(APIResponse.error(
                        new APIError("NOT_FOUND", ex.getMessage()),
                        BaseAPICode.RESOURCE_NOT_FOUND
                    )))
            );
    }
}
```

### WebFlux Integration

```java
@Configuration
public class WebFluxConfig {
    
    @Bean
    public WebExceptionHandler webExceptionHandler() {
        return (exchange, ex) -> {
            APIResponse<?> response = handleException(ex);
            exchange.getResponse().setStatusCode(HttpStatus.valueOf(response.status()));
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            
            return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory()
                    .wrap(serializeResponse(response).getBytes()))
            );
        };
    }
}
```

## 📊 Custom Response Formats

### XML Response Support

```java
@Configuration
public class XmlResponseConfig {
    
    @Bean
    public HttpMessageConverter<APIResponse<?>> xmlMessageConverter() {
        MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_XML));
        
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(APIResponse.class, APIError.class, FieldError.class);
        converter.setMarshaller(marshaller);
        converter.setUnmarshaller(marshaller);
        
        return converter;
    }
}

// Annotate response classes for XML
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class APIResponse<T> {
    // XML annotations...
}
```

## 🚀 Performance Optimizations

### Response Caching

```java
public class CachingResponseHandler {
    
    private final Cache<ResponseCacheKey, APIResponse<?>> responseCache;
    
    public <T> APIResponse<T> getCachedResponse(ResponseCacheKey key, Supplier<APIResponse<T>> supplier) {
        APIResponse<?> cached = responseCache.getIfPresent(key);
        if (cached != null) {
            return (APIResponse<T>) cached;
        }
        
        APIResponse<T> response = supplier.get();
        responseCache.put(key, response);
        return response;
    }
}

// Usage
@GetMapping("/{id}")
public ResponseEntity<APIResponse<User>> getUser(@PathVariable String id) {
    ResponseCacheKey key = new ResponseCacheKey("user", id);
    APIResponse<User> response = cachingHandler.getCachedResponse(key, 
        () -> APIResponse.success(userService.getUser(id), BaseAPICode.SUCCESS));
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
        .body(response);
}
```

### Response Compression

```java
@Configuration
public class CompressionConfig {
    
    @Bean
    public FilterRegistrationBean<CompressionFilter> compressionFilter() {
        FilterRegistrationBean<CompressionFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CompressionFilter());
        registration.addUrlPatterns("/api/*");
        registration.setName("apiCompressionFilter");
        return registration;
    }
}

public class CompressionFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        if (response instanceof HttpServletResponse httpResponse) {
            httpResponse.setHeader("Content-Encoding", "gzip");
            chain.doFilter(request, new GZIPResponseWrapper(httpResponse));
        } else {
            chain.doFilter(request, response);
        }
    }
}
```

## 🔧 Extension Points

### Custom Response Builder

```java
public class ExtendedAPIResponseBuilder<T> {
    
    private final APIResponse.Builder<T> delegate;
    private Map<String, Object> metadata;
    
    public ExtendedAPIResponseBuilder(APICode status) {
        this.delegate = APIResponse.builder(status);
        this.metadata = new HashMap<>();
    }
    
    public ExtendedAPIResponseBuilder<T> metadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }
    
    public APIResponse<T> build() {
        APIResponse<T> response = delegate.build();
        
        // Create extended response with metadata
        return new ExtendedAPIResponse<>(
            response.status(),
            response.data(),
            response.error(),
            response.message(),
            response.timestamp(),
            metadata
        );
    }
}

// Usage
return new ExtendedAPIResponseBuilder<User>(BaseAPICode.SUCCESS)
    .data(user)
    .metadata("version", "1.2.0")
    .metadata("processedBy", "service-v1")
    .build();
```

These advanced techniques will help you customize and extend the library to fit your specific requirements while maintaining consistency and performance.