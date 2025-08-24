package com.github.vikunalabs.lib.apicore.exception.business;

import com.github.vikunalabs.lib.apicore.exception.base.BusinessLogicBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a domain entity or value object fails validation
 * that requires business context beyond simple format checking.
 * <p>
 * This exception should be used for domain-specific validation rules that
 * involve business logic, such as checking business rules during entity
 * construction or modification.
 * </p>
 *
 * Example:
 * <pre>
 * if (email.isCorporate() &amp;&amp; !email.isVerified()) {
 *     throw new DomainValidationException(BaseAPICode.BUSINESS_RULE_VIOLATION,
 *         "Corporate emails must be verified before use",
 *         Map.of("email", email.getValue(), "isCorporate", true, "isVerified", false));
 * }
 * </pre>
 */
public class DomainValidationException extends BusinessLogicBaseException {

    /** The type of domain entity that failed validation. */
    private final String entityType;
    /** The specific field or property that failed validation. */
    private final String fieldName;

    /**
     * Constructs a new domain validation exception.
     *
     * @param code the API code (typically BaseAPICode.BUSINESS_RULE_VIOLATION or BaseAPICode.INVALID_INPUT)
     * @param message the detailed error message
     */
    public DomainValidationException(APICode code, String message) {
        super(code, message);
        this.entityType = null;
        this.fieldName = null;
    }

    /**
     * Constructs a new domain validation exception with entity context.
     *
     * @param code the API code (typically BaseAPICode.BUSINESS_RULE_VIOLATION or BaseAPICode.INVALID_INPUT)
     * @param message the detailed error message
     * @param entityType the type of entity that failed validation
     * @param fieldName the specific field that failed validation, if applicable
     */
    public DomainValidationException(APICode code, String message, String entityType, String fieldName) {
        super(code, message);
        this.entityType = entityType;
        this.fieldName = fieldName;
    }

    /**
     * Constructs a new domain validation exception with details.
     *
     * @param code the API code (typically BaseAPICode.BUSINESS_RULE_VIOLATION or BaseAPICode.INVALID_INPUT)
     * @param message the detailed error message
     * @param details additional details about the validation failure
     */
    public DomainValidationException(APICode code, String message, Object details) {
        super(code, message, details);
        this.entityType = null;
        this.fieldName = null;
    }

    /**
     * Returns the type of entity that failed validation.
     *
     * @return the entity type, or null if not specified
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Returns the specific field that failed validation.
     *
     * @return the field name, or null if not applicable
     */
    public String getFieldName() {
        return fieldName;
    }
}
