package com.github.vikunalabs.lib.apicore.exception.business;

import com.github.vikunalabs.lib.apicore.exception.base.BusinessLogicBaseException;
import com.github.vikunalabs.lib.apicore.model.APICode;

/**
 * Exception thrown when a business rule or domain constraint is violated.
 * <p>
 * This exception should be used for violations of core business logic that
 * cannot be determined through simple input validation. These typically
 * involve complex rules that require knowledge of the current system state
 * or relationships between multiple entities.
 * </p>
 *
 * Example:
 * <pre>
 * if (account.getBalance() &lt; transferAmount) {
 *     throw new BusinessRuleViolationException(BaseAPICode.BUSINESS_RULE_VIOLATION,
 *         "Insufficient funds for transfer",
 *         Map.of("currentBalance", account.getBalance(), "transferAmount", transferAmount));
 * }
 * </pre>
 */
public class BusinessRuleViolationException extends BusinessLogicBaseException {

    /** The name of the business rule that was violated. */
    private final String ruleName;

    /**
     * Constructs a new business rule violation exception.
     *
     * @param code the API code (typically BaseAPICode.BUSINESS_RULE_VIOLATION)
     * @param message the detailed error message
     */
    public BusinessRuleViolationException(APICode code, String message) {
        super(code, message);
        this.ruleName = null;
    }

    /**
     * Constructs a new business rule violation exception with rule name.
     *
     * @param code the API code (typically BaseAPICode.BUSINESS_RULE_VIOLATION)
     * @param message the detailed error message
     * @param ruleName the name of the business rule that was violated
     */
    public BusinessRuleViolationException(APICode code, String message, String ruleName) {
        super(code, message);
        this.ruleName = ruleName;
    }

    /**
     * Constructs a new business rule violation exception with details.
     *
     * @param code the API code (typically BaseAPICode.BUSINESS_RULE_VIOLATION)
     * @param message the detailed error message
     * @param details additional details about the rule violation
     */
    public BusinessRuleViolationException(APICode code, String message, Object details) {
        super(code, message, details);
        this.ruleName = null;
    }

    /**
     * Constructs a new business rule violation exception with rule name and details.
     *
     * @param code the API code (typically BaseAPICode.BUSINESS_RULE_VIOLATION)
     * @param message the detailed error message
     * @param ruleName the name of the business rule that was violated
     * @param details additional details about the rule violation
     */
    public BusinessRuleViolationException(APICode code, String message, String ruleName, Object details) {
        super(code, message, details);
        this.ruleName = ruleName;
    }

    /**
     * Returns the name of the business rule that was violated.
     *
     * @return the rule name, or null if not specified
     */
    public String getRuleName() {
        return ruleName;
    }
}
