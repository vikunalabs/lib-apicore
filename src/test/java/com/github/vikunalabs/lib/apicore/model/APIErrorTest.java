package com.github.vikunalabs.lib.apicore.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class APIErrorTest {

    @Nested
    @DisplayName("Constructors")
    class Constructors {

        @Test
        @DisplayName("Create simple APIError without field errors or details")
        void createSimpleAPIError() {
            APIError error = new APIError("NOT_FOUND", "Resource not found");

            assertEquals("NOT_FOUND", error.code());
            assertEquals("Resource not found", error.message());
            assertTrue(error.fieldErrors().isEmpty());
            assertNull(error.details());
        }

        @Test
        @DisplayName("Create APIError with field errors")
        void createAPIErrorWithFieldErrors() {
            FieldError fieldError = new FieldError("email", "INVALID", "Invalid email");
            APIError error = new APIError("VALIDATION_FAILED", "Validation failed", List.of(fieldError));

            assertEquals("VALIDATION_FAILED", error.code());
            assertEquals("Validation failed", error.message());
            assertEquals(1, error.fieldErrors().size());
            assertEquals(fieldError, error.fieldErrors().get(0));
            assertNull(error.details());
        }

        @Test
        @DisplayName("Create APIError with details")
        void createAPIErrorWithDetails() {
            APIError error = new APIError("DB_ERROR", "Database error", "Connection timeout");

            assertEquals("DB_ERROR", error.code());
            assertEquals("Database error", error.message());
            assertTrue(error.fieldErrors().isEmpty());
            assertEquals("Connection timeout", error.details());
        }

        @Test
        @DisplayName("Create APIError with all parameters")
        void createAPIErrorWithAllParameters() {
            FieldError fieldError = new FieldError("username", "TAKEN", "Username already taken");
            APIError error = new APIError("CONFLICT", "Resource conflict", List.of(fieldError), "Additional info");

            assertEquals("CONFLICT", error.code());
            assertEquals("Resource conflict", error.message());
            assertEquals(1, error.fieldErrors().size());
            assertEquals("Additional info", error.details());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Build simple APIError using builder")
        void buildSimpleAPIErrorUsingBuilder() {
            APIError error = APIError.builder()
                    .code("UNAUTHORIZED")
                    .message("Authentication required")
                    .build();

            assertEquals("UNAUTHORIZED", error.code());
            assertEquals("Authentication required", error.message());
            assertTrue(error.fieldErrors().isEmpty());
            assertNull(error.details());
        }

        @Test
        @DisplayName("Build APIError with field errors using builder")
        void buildAPIErrorWithFieldErrorsUsingBuilder() {
            APIError error = APIError.builder()
                    .code("VALIDATION_FAILED")
                    .message("Invalid input")
                    .fieldError("email", "INVALID", "Invalid email format")
                    .fieldError("password", "TOO_SHORT", "Password too short", "short")
                    .build();

            assertEquals("VALIDATION_FAILED", error.code());
            assertEquals("Invalid input", error.message());
            assertEquals(2, error.fieldErrors().size());
            assertNull(error.details());
        }

        @Test
        @DisplayName("Build APIError with details using builder")
        void buildAPIErrorWithDetailsUsingBuilder() {
            APIError error = APIError.builder()
                    .code("INTERNAL_ERROR")
                    .message("Internal server error")
                    .details("Database connection failed")
                    .build();

            assertEquals("INTERNAL_ERROR", error.code());
            assertEquals("Internal server error", error.message());
            assertTrue(error.fieldErrors().isEmpty());
            assertEquals("Database connection failed", error.details());
        }

        @Test
        @DisplayName("Build APIError with list of field errors")
        void buildAPIErrorWithListOfFieldErrors() {
            FieldError error1 = new FieldError("field1", "ERROR1", "Error 1");
            FieldError error2 = new FieldError("field2", "ERROR2", "Error 2");

            APIError error = APIError.builder()
                    .code("MULTI_ERROR")
                    .message("Multiple errors")
                    .fieldErrors(List.of(error1, error2))
                    .build();

            assertEquals("MULTI_ERROR", error.code());
            assertEquals("Multiple errors", error.message());
            assertEquals(2, error.fieldErrors().size());
        }

        @Test
        @DisplayName("Builder ensures fieldErrors is never null")
        void builderEnsuresFieldErrorsIsNeverNull() {
            APIError error = APIError.builder().code("TEST").message("Test").build();

            assertNotNull(error.fieldErrors());
            assertTrue(error.fieldErrors().isEmpty());
        }
    }

    @Test
    @DisplayName("APIError with null values")
    void apiErrorWithNullValues() {
        APIError error = new APIError(null, null, null, null);

        assertNull(error.code());
        assertNull(error.message());
        assertNotNull(error.fieldErrors()); // Should be empty list, not null
        assertTrue(error.fieldErrors().isEmpty());
        assertNull(error.details());
    }
}
