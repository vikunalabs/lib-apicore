package com.github.vikunalabs.lib.apicore.exception.business;

import com.github.vikunalabs.lib.apicore.exception.base.BusinessLogicBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a concurrent modification conflict is detected.
 * <p>
 * This exception should be used for optimistic locking violations where
 * multiple clients attempt to modify the same resource simultaneously,
 * and one of the modifications must fail to maintain data consistency.
 * </p>
 *
 * Example:
 * <pre>
 * if (currentVersion != expectedVersion) {
 *     throw new ConcurrentModificationException(BaseAPICode.RESOURCE_CONFLICT,
 *         "Resource was modified by another process",
 *         Map.of("expectedVersion", expectedVersion, "currentVersion", currentVersion));
 * }
 * </pre>
 */
public class ConcurrentModificationException extends BusinessLogicBaseException {

    /** The identifier of the resource that was concurrently modified. */
    private final String resourceId;
    /** The expected version/ETag value at the time of modification. */
    private final Object expectedVersion;
    /** The actual current version/ETag value found in the resource. */
    private final Object actualVersion;

    /**
     * Constructs a new concurrent modification exception.
     *
     * @param code the API code (typically BaseAPICode.RESOURCE_CONFLICT)
     * @param message the detailed error message
     */
    public ConcurrentModificationException(APICode code, String message) {
        super(code, message);
        this.resourceId = null;
        this.expectedVersion = null;
        this.actualVersion = null;
    }

    /**
     * Constructs a new concurrent modification exception with version details.
     *
     * @param code the API code (typically BaseAPICode.RESOURCE_CONFLICT)
     * @param message the detailed error message
     * @param resourceId the identifier of the resource that was concurrently modified
     * @param expectedVersion the expected version/ETag value
     * @param actualVersion the actual current version/ETag value
     */
    public ConcurrentModificationException(
            APICode code, String message, String resourceId, Object expectedVersion, Object actualVersion) {
        super(code, message);
        this.resourceId = resourceId;
        this.expectedVersion = expectedVersion;
        this.actualVersion = actualVersion;
    }

    /**
     * Constructs a new concurrent modification exception with details.
     *
     * @param code the API code (typically BaseAPICode.RESOURCE_CONFLICT)
     * @param message the detailed error message
     * @param details additional details about the concurrent modification
     */
    public ConcurrentModificationException(APICode code, String message, Object details) {
        super(code, message, details);
        this.resourceId = null;
        this.expectedVersion = null;
        this.actualVersion = null;
    }

    /**
     * Returns the resource identifier that was concurrently modified.
     *
     * @return the resource ID, or null if not specified
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Returns the expected version/ETag value.
     *
     * @return the expected version, or null if not specified
     */
    public Object getExpectedVersion() {
        return expectedVersion;
    }

    /**
     * Returns the actual current version/ETag value.
     *
     * @return the actual version, or null if not specified
     */
    public Object getActualVersion() {
        return actualVersion;
    }
}
