package com.github.vikunalabs.lib.apicore.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BaseAPICodeTest {

    @Nested
    @DisplayName("HTTP Status Codes")
    class HttpStatusCodes {

        @Test
        @DisplayName("Success codes have 2xx status")
        void successCodesHave2xxStatus() {
            assertEquals(200, BaseAPICode.SUCCESS.getHttpStatus());
            assertEquals(201, BaseAPICode.RESOURCE_CREATED.getHttpStatus());
            assertEquals(202, BaseAPICode.REQUEST_ACCEPTED.getHttpStatus());
        }

        @Test
        @DisplayName("Validation error codes have 4xx status")
        void validationErrorCodesHave4xxStatus() {
            assertEquals(400, BaseAPICode.BAD_REQUEST.getHttpStatus());
            assertEquals(400, BaseAPICode.VALIDATION_FAILED.getHttpStatus());
            assertEquals(404, BaseAPICode.RESOURCE_NOT_FOUND.getHttpStatus());
        }

        @Test
        @DisplayName("Server error codes have 5xx status")
        void serverErrorCodesHave5xxStatus() {
            assertEquals(500, BaseAPICode.INTERNAL_ERROR.getHttpStatus());
            assertEquals(503, BaseAPICode.SERVICE_UNAVAILABLE.getHttpStatus());
            assertEquals(504, BaseAPICode.GATEWAY_TIMEOUT.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("Default Messages")
    class DefaultMessages {

        @Test
        @DisplayName("Get default message from enum name")
        void getDefaultMessageFromEnumName() {
            assertEquals("success", BaseAPICode.SUCCESS.getDefaultMessage());
            assertEquals("resource not found", BaseAPICode.RESOURCE_NOT_FOUND.getDefaultMessage());
            assertEquals("internal error", BaseAPICode.INTERNAL_ERROR.getDefaultMessage());
        }

        @Test
        @DisplayName("Get formatted message with dynamic content")
        void getFormattedMessageWithDynamicContent() {
            String message = BaseAPICode.USER_NOT_FOUND.getFormattedMessage("User with ID 123 not found");
            assertEquals("User with ID 123 not found", message);
        }

        @Test
        @DisplayName("Get formatted message with template replacement")
        void getFormattedMessageWithTemplateReplacement() {
            String message = BaseAPICode.USER_NOT_FOUND.getFormattedMessage("User %s not found", "test@example.com");
            assertEquals("User test@example.com not found", message);
        }
    }

    @Test
    @DisplayName("Enum values are accessible")
    void enumValuesAreAccessible() {
        assertNotNull(BaseAPICode.valueOf("SUCCESS"));
        assertNotNull(BaseAPICode.valueOf("BAD_REQUEST"));
        assertNotNull(BaseAPICode.valueOf("INTERNAL_ERROR"));
    }

    @Test
    @DisplayName("APICode interface methods work correctly")
    void apiCodeInterfaceMethodsWorkCorrectly() {
        APICode code = BaseAPICode.SUCCESS;

        assertEquals("SUCCESS", code.name());
        assertEquals(200, code.getHttpStatus());
        assertEquals("success", code.getDefaultMessage());
    }
}
