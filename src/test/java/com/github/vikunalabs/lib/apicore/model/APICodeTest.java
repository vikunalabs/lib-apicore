package com.github.vikunalabs.lib.apicore.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class APICodeTest {

    // Test the default implementation of the interface
    @Test
    @DisplayName("Test default method implementations")
    void testDefaultMethodImplementations() {
        // Create a test implementation
        APICode testCode = new APICode() {
            @Override
            public String name() {
                return "TEST_CODE";
            }

            @Override
            public int getHttpStatus() {
                return 418;
            }
        };

        assertEquals("TEST_CODE", testCode.name());
        assertEquals(418, testCode.getHttpStatus());
        assertEquals("test code", testCode.getDefaultMessage());
        assertEquals("Custom message", testCode.getFormattedMessage("Custom message"));
        assertEquals("Hello World", testCode.getFormattedMessage("Hello %s", "World"));
    }

    @Test
    @DisplayName("Test default message formatting with underscores")
    void testDefaultMessageFormattingWithUnderscores() {
        APICode codeWithUnderscores = new APICode() {
            @Override
            public String name() {
                return "LONG_ERROR_CODE_NAME";
            }

            @Override
            public int getHttpStatus() {
                return 400;
            }
        };

        assertEquals("long error code name", codeWithUnderscores.getDefaultMessage());
    }
}
