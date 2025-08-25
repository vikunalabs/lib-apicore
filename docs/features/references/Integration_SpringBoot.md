# Framework Integration Guide

## Spring Boot

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

## JAX-RS

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

## Servlet API

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