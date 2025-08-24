package com.github.vikunalabs.lib.apicore.exception.business;

import com.github.vikunalabs.lib.apicore.exception.base.BusinessLogicBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a quota or limit is exceeded for a user, account, or system.
 * <p>
 * This exception should be used when business-imposed limits are reached,
 * such as API call quotas, storage limits, subscription limits, or other
 * business-defined constraints.
 * </p>
 *
 * Example:
 * <pre>
 * if (user.getProjects().size() >= user.getPlan().getMaxProjects()) {
 *     throw new QuotaExceededException(BaseAPICode.QUOTA_EXCEEDED,
 *         "Project limit exceeded for current plan",
 *         Map.of("currentUsage", user.getProjects().size(), "maxAllowed", user.getPlan().getMaxProjects()));
 * }
 * </pre>
 */
public class QuotaExceededException extends BusinessLogicBaseException {

    /** The type of quota that was exceeded (e.g., API calls, storage, projects). */
    private final String quotaType;
    /** The current usage amount that exceeded the limit. */
    private final long currentUsage;
    /** The maximum allowed amount for this quota type. */
    private final long maxAllowed;

    /**
     * Constructs a new quota exceeded exception.
     *
     * @param code the API code (typically BaseAPICode.QUOTA_EXCEEDED)
     * @param message the detailed error message
     */
    public QuotaExceededException(APICode code, String message) {
        super(code, message);
        this.quotaType = null;
        this.currentUsage = -1;
        this.maxAllowed = -1;
    }

    /**
     * Constructs a new quota exceeded exception with quota details.
     *
     * @param code the API code (typically BaseAPICode.QUOTA_EXCEEDED)
     * @param message the detailed error message
     * @param quotaType the type of quota that was exceeded
     * @param currentUsage the current usage amount
     * @param maxAllowed the maximum allowed amount
     */
    public QuotaExceededException(APICode code, String message, String quotaType, long currentUsage, long maxAllowed) {
        super(code, message);
        this.quotaType = quotaType;
        this.currentUsage = currentUsage;
        this.maxAllowed = maxAllowed;
    }

    /**
     * Constructs a new quota exceeded exception with details.
     *
     * @param code the API code (typically BaseAPICode.QUOTA_EXCEEDED)
     * @param message the detailed error message
     * @param details additional details about the quota violation
     */
    public QuotaExceededException(APICode code, String message, Object details) {
        super(code, message, details);
        this.quotaType = null;
        this.currentUsage = -1;
        this.maxAllowed = -1;
    }

    /**
     * Returns the type of quota that was exceeded.
     *
     * @return the quota type, or null if not specified
     */
    public String getQuotaType() {
        return quotaType;
    }

    /**
     * Returns the current usage amount.
     *
     * @return the current usage, or -1 if not specified
     */
    public long getCurrentUsage() {
        return currentUsage;
    }

    /**
     * Returns the maximum allowed amount.
     *
     * @return the maximum allowed, or -1 if not specified
     */
    public long getMaxAllowed() {
        return maxAllowed;
    }
}
