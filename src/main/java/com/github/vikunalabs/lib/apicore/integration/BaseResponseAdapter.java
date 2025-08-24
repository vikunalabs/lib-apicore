package com.github.vikunalabs.lib.apicore.integration;

import com.github.vikunalabs.lib.apicore.exception.base.BusinessLogicBaseException;
import com.github.vikunalabs.lib.apicore.exception.base.ClientErrorsBaseException;
import com.github.vikunalabs.lib.apicore.exception.base.ServerErrorsBaseException;
import com.github.vikunalabs.lib.apicore.exception.util.ExceptionResponseMapper;
import com.github.vikunalabs.lib.apicore.model.APIResponse;

/**
 * Base class for framework integration without framework dependencies.
 * <p>
 * Extend this class in your framework-specific modules to handle exception
 * to response conversion without polluting the core library.
 * </p>
 */
public abstract class BaseResponseAdapter {

    /**
     * Creates a new BaseResponseAdapter instance.
     */
    protected BaseResponseAdapter() {}

    /**
     * Converts an exception to an APIResponse with proper HTTP status.
     * This method can be called from framework-specific exception handlers.
     *
     * @param exception the exception to convert
     * @return standardized APIResponse
     */
    public APIResponse<?> handleException(Throwable exception) {
        return ExceptionResponseMapper.toResponse(exception);
    }

    /**
     * Gets the HTTP status code from an exception.
     * Useful for framework-specific response building.
     *
     * @param exception the exception
     * @return HTTP status code
     */
    public int getHttpStatusFromException(Throwable exception) {
        if (exception instanceof ClientErrorsBaseException clientEx) {
            return clientEx.getHttpStatus();
        } else if (exception instanceof ServerErrorsBaseException serverEx) {
            return serverEx.getHttpStatus();
        } else if (exception instanceof BusinessLogicBaseException businessEx) {
            return businessEx.getHttpStatus();
        }

        // Default to internal server error for unknown exceptions
        return 500;
    }
}
