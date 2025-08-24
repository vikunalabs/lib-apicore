package com.github.vikunalabs.lib.apicore.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Interface defining standard API response codes with HTTP status mapping.
 * Implementations provide consistent error and success codes across the API.
 */
@Schema(description = "API response code interface defining standard codes with HTTP status mapping")
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
