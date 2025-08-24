package com.github.vikunalabs.lib.apicore.exception.server;

import com.github.vikunalabs.lib.apicore.exception.base.ServerErrorsBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a requested functionality is not implemented.
 * <p>
 * This exception should be used when an API endpoint or feature is defined
 * but has not yet been implemented. This is useful during development or
 * for marking endpoints that are planned but not yet available.
 * </p>
 *
 * Example:
 * <pre>
 * throw new NotImplementedException(BaseAPICode.NOT_IMPLEMENTED,
 *     "Batch processing feature is not yet implemented");
 * </pre>
 */
public class NotImplementedException extends ServerErrorsBaseException {

    /** The name of the feature or functionality that is not yet implemented. */
    private final String featureName;

    /**
     * Constructs a new not implemented exception.
     *
     * @param code the API code (typically BaseAPICode.NOT_IMPLEMENTED)
     * @param message the detailed error message
     */
    public NotImplementedException(APICode code, String message) {
        super(code, message);
        this.featureName = null;
    }

    /**
     * Constructs a new not implemented exception with feature name.
     *
     * @param code the API code (typically BaseAPICode.NOT_IMPLEMENTED)
     * @param message the detailed error message
     * @param featureName the name of the feature that is not implemented
     */
    public NotImplementedException(APICode code, String message, String featureName) {
        super(code, message);
        this.featureName = featureName;
    }

    /**
     * Constructs a new not implemented exception with cause.
     *
     * @param code the API code (typically BaseAPICode.NOT_IMPLEMENTED)
     * @param message the detailed error message
     * @param cause the underlying cause of this exception
     */
    public NotImplementedException(APICode code, String message, Throwable cause) {
        super(code, message, cause);
        this.featureName = null;
    }

    /**
     * Returns the name of the feature that is not implemented.
     *
     * @return the feature name, or null if not specified
     */
    public String getFeatureName() {
        return featureName;
    }
}
