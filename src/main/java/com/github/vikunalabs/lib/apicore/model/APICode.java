package com.github.vikunalabs.lib.apicore.model;

/**
 * Interface defining standard API response codes with HTTP status mapping.
 * Implementations provide consistent error and success codes across the API.
 */
public interface APICode {
    /**
     * Returns the name of this API code.
     *
     * @return the name of the code
     */
    String name();

    /**
     * Gets the HTTP status code associated with this API code.
     *
     * @return the HTTP status code
     */
    int getHttpStatus();

    /**
     * Returns a default human-readable message for this code.
     * Used when no specific message is provided for exceptions.
     *
     * @return the default message for this code
     */
    default String getDefaultMessage() {
        return name().toLowerCase().replace('_', ' ');
    }

    /**
     * Get a formatted message with dynamic content
     * @param message the dynamic message content
     * @return the complete message
     */
    default String getFormattedMessage(String message) {
        return message;
    }

    /**
     * Get a formatted message with template replacement
     * @param template the message template with placeholders
     * @param args values to insert into the template
     * @return formatted message
     */
    default String getFormattedMessage(String template, Object... args) {
        return String.format(template, args);
    }
}
