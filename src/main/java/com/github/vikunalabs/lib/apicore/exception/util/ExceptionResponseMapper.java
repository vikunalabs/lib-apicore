package com.github.vikunalabs.lib.apicore.exception.util;

import com.github.vikunalabs.lib.apicore.exception.base.BusinessLogicBaseException;
import com.github.vikunalabs.lib.apicore.exception.base.ClientErrorsBaseException;
import com.github.vikunalabs.lib.apicore.exception.base.ServerErrorsBaseException;
import com.github.vikunalabs.lib.apicore.exception.client.ValidationException;
import com.github.vikunalabs.lib.apicore.model.*;
import java.util.List;

/**
 * Utility class for mapping exceptions to standardized API responses.
 * <p>
 * This class provides static methods to convert various exception types into
 * properly formatted APIResponse objects with appropriate HTTP status codes
 * and error details.
 * </p>
 */
public final class ExceptionResponseMapper {

    private ExceptionResponseMapper() {
        // Utility class - prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Maps a ClientErrorsBaseException to an APIResponse.
     *
     * @param <T> the response type parameter
     * @param ex the client error exception to map
     * @return a properly formatted APIResponse for client errors
     */
    public static <T> APIResponse<T> toResponse(ClientErrorsBaseException ex) {
        APIError.APIErrorBuilder errorBuilder =
                APIError.builder().code(ex.getCode().name()).message(ex.getMessage());

        // Handle ValidationException specially to extract field errors
        if (ex instanceof ValidationException validationEx) {
            List<FieldError> fieldErrors = validationEx.getFieldErrors();
            if (fieldErrors != null && !fieldErrors.isEmpty()) {
                errorBuilder.fieldErrors(fieldErrors);
            }
        } else if (ex.getDetails() != null) {
            String detailsString = convertDetailsToString(ex.getDetails());
            if (detailsString != null) {
                errorBuilder.details(detailsString);
            }
        }

        return APIResponse.error(errorBuilder.build(), ex.getCode(), ex.getMessage());
    }

    /**
     * Maps a ServerErrorsBaseException to an APIResponse.
     *
     * @param <T> the response type parameter
     * @param ex the server error exception to map
     * @return a properly formatted APIResponse for server errors
     */
    public static <T> APIResponse<T> toResponse(ServerErrorsBaseException ex) {
        APIError.APIErrorBuilder errorBuilder =
                APIError.builder().code(ex.getCode().name()).message(ex.getMessage());

        String detailsString = convertDetailsToString(ex.getDetails());
        if (detailsString != null) {
            errorBuilder.details(detailsString);
        }

        // Security consideration: In production, you might want to sanitize
        // server error messages to avoid exposing internal details
        String safeMessage = ex.getMessage();

        return APIResponse.error(errorBuilder.build(), ex.getCode(), safeMessage);
    }

    /**
     * Maps a BusinessLogicBaseException to an APIResponse.
     *
     * @param <T> the response type parameter
     * @param ex the business logic exception to map
     * @return a properly formatted APIResponse for business logic errors
     */
    public static <T> APIResponse<T> toResponse(BusinessLogicBaseException ex) {
        APIError.APIErrorBuilder errorBuilder =
                APIError.builder().code(ex.getCode().name()).message(ex.getMessage());

        String detailsString = convertDetailsToString(ex.getDetails());
        if (detailsString != null) {
            errorBuilder.details(detailsString);
        }

        return APIResponse.error(errorBuilder.build(), ex.getCode(), ex.getMessage());
    }

    /**
     * Maps a generic Throwable to an APIResponse for unexpected errors.
     * <p>
     * This method serves as a fallback for unhandled exceptions and should
     * return a generic internal server error response without exposing
     * sensitive implementation details.
     * </p>
     *
     * @param <T> the response type parameter
     * @param throwable the unexpected throwable to map
     * @return a generic internal server error response
     */
    public static <T> APIResponse<T> toResponse(Throwable throwable) {
        // Use a generic internal error code
        APICode internalErrorCode = getDefaultInternalErrorCode();

        APIError error = APIError.builder()
                .code(internalErrorCode.name())
                .message("An unexpected error occurred")
                .build();

        return APIResponse.error(error, internalErrorCode, "Internal server error");
    }

    /**
     * Maps an exception to an APIResponse with automatic type detection.
     *
     * @param <T> the response type parameter
     * @param exception the exception to map
     * @return a properly formatted APIResponse based on exception type
     */
    public static <T> APIResponse<T> toResponse(Exception exception) {
        if (exception instanceof ClientErrorsBaseException clientEx) {
            return toResponse(clientEx);
        } else if (exception instanceof ServerErrorsBaseException serverEx) {
            return toResponse(serverEx);
        } else if (exception instanceof BusinessLogicBaseException businessEx) {
            return toResponse(businessEx);
        } else {
            return toResponse((Throwable) exception);
        }
    }

    /**
     * Provides a default internal error code for unhandled exceptions.
     * This method can be overridden by subclasses or configured for different environments.
     *
     * @return the default APICode to use for internal errors
     */
    protected static APICode getDefaultInternalErrorCode() {
        return BaseAPICode.INTERNAL_ERROR;
    }

    /**
     * Converts details object to a string representation for APIError.
     * Handles null values and provides appropriate string conversion.
     *
     * @param details the details object to convert
     * @return string representation, or null if details is null
     */
    private static String convertDetailsToString(Object details) {
        if (details == null) {
            return null;
        }

        if (details instanceof String stringDetails) {
            return stringDetails;
        }

        // For non-String objects, use toString() but consider JSON serialization
        // for complex objects in a real implementation
        return details.toString();
    }

    /**
     * Checks if the given throwable is a client error.
     *
     * @param throwable the throwable to check
     * @return true if the throwable is a ClientErrorsBaseException, false otherwise
     */
    public static boolean isClientError(Throwable throwable) {
        return throwable instanceof ClientErrorsBaseException;
    }

    /**
     * Checks if the given throwable is a server error.
     *
     * @param throwable the throwable to check
     * @return true if the throwable is a ServerErrorsBaseException, false otherwise
     */
    public static boolean isServerError(Throwable throwable) {
        return throwable instanceof ServerErrorsBaseException;
    }

    /**
     * Checks if the given throwable is a business logic error.
     *
     * @param throwable the throwable to check
     * @return true if the throwable is a BusinessLogicBaseException, false otherwise
     */
    public static boolean isBusinessError(Throwable throwable) {
        return throwable instanceof BusinessLogicBaseException;
    }
}
