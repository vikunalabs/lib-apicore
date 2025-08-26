package com.github.vikunalabs.lib.apicore.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class APIResponseTest {

    @Nested
    @DisplayName("Success Factory Methods")
    class SuccessFactoryMethods {

        @Test
        @DisplayName("Success with data and default message")
        void successWithDataAndDefaultMessage() {
            APIResponse<String> response = APIResponse.success("test-data", BaseAPICode.SUCCESS);

            assertEquals(200, response.status());
            assertEquals("test-data", response.data());
            assertNull(response.error());
            assertEquals("success", response.message());
            assertNotNull(response.timestamp());
            assertTrue(response.isSuccess());
            assertFalse(response.hasError());
        }

        @Test
        @DisplayName("Success with data and custom message")
        void successWithDataAndCustomMessage() {
            APIResponse<String> response = APIResponse.success("test-data", BaseAPICode.SUCCESS, "Custom message");

            assertEquals(200, response.status());
            assertEquals("test-data", response.data());
            assertNull(response.error());
            assertEquals("Custom message", response.message());
            assertTrue(response.isSuccess());
        }

        @Test
        @DisplayName("Success with no data and custom message")
        void successWithNoDataAndCustomMessage() {
            APIResponse<Void> response = APIResponse.success(BaseAPICode.SUCCESS, "Account confirmed");

            assertEquals(200, response.status());
            assertNull(response.data());
            assertNull(response.error());
            assertEquals("Account confirmed", response.message());
            assertTrue(response.isSuccess());
        }

        @Test
        @DisplayName("Success with no data and default message")
        void successWithNoDataAndDefaultMessage() {
            APIResponse<Void> response = APIResponse.success(BaseAPICode.SUCCESS);

            assertEquals(200, response.status());
            assertNull(response.data());
            assertNull(response.error());
            assertEquals("success", response.message());
            assertTrue(response.isSuccess());
        }

        @Test
        @DisplayName("Success with null data should be allowed")
        void successWithNullData() {
            APIResponse<String> response = APIResponse.success(null, BaseAPICode.SUCCESS, "No data");

            assertEquals(200, response.status());
            assertNull(response.data());
            assertNull(response.error());
            assertEquals("No data", response.message());
            assertTrue(response.isSuccess());
        }

        @Test
        @DisplayName("Throw exception for non-success status in success factory")
        void throwExceptionForNonSuccessStatus() {
            assertThrows(IllegalArgumentException.class, () -> {
                APIResponse.success("data", BaseAPICode.BAD_REQUEST);
            });
        }

        @Test
        @DisplayName("Throw exception for 204 status")
        void throwExceptionFor204Status() {
            assertThrows(IllegalArgumentException.class, () -> {
                APIResponse.success("data", BaseAPICode.NO_ACTION_TAKEN);
            });
        }
    }

    @Nested
    @DisplayName("Error Factory Methods")
    class ErrorFactoryMethods {

        @Test
        @DisplayName("Error with APIError and default message")
        void errorWithAPIErrorAndDefaultMessage() {
            APIError apiError = new APIError("VALIDATION_FAILED", "Validation failed");
            APIResponse<String> response = APIResponse.error(apiError, BaseAPICode.BAD_REQUEST);

            assertEquals(400, response.status());
            assertNull(response.data());
            assertEquals(apiError, response.error());
            assertEquals("bad request", response.message());
            assertFalse(response.isSuccess());
            assertTrue(response.hasError());
        }

        @Test
        @DisplayName("Error with APIError and custom message")
        void errorWithAPIErrorAndCustomMessage() {
            APIError apiError = new APIError("NOT_FOUND", "Resource not found");
            APIResponse<String> response =
                    APIResponse.error(apiError, BaseAPICode.RESOURCE_NOT_FOUND, "Custom error message");

            assertEquals(404, response.status());
            assertNull(response.data());
            assertEquals(apiError, response.error());
            assertEquals("Custom error message", response.message());
            assertFalse(response.isSuccess());
        }

        @Test
        @DisplayName("Throw exception for null error in error factory")
        void throwExceptionForNullError() {
            assertThrows(IllegalArgumentException.class, () -> {
                APIResponse.error(null, BaseAPICode.BAD_REQUEST);
            });
        }

        @Test
        @DisplayName("Throw exception for non-error status in error factory")
        void throwExceptionForNonErrorStatus() {
            APIError apiError = new APIError("TEST", "Test error");
            assertThrows(IllegalArgumentException.class, () -> {
                APIResponse.error(apiError, BaseAPICode.SUCCESS);
            });
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPattern {

        @Test
        @DisplayName("Build success response with data using builder")
        void buildSuccessResponseWithData() {
            APIResponse<String> response = APIResponse.<String>builder()
                    .status(BaseAPICode.SUCCESS)
                    .data("test-data")
                    .message("Success message")
                    .build();

            assertEquals(200, response.status());
            assertEquals("test-data", response.data());
            assertNull(response.error());
            assertEquals("Success message", response.message());
            assertTrue(response.isSuccess());
        }

        @Test
        @DisplayName("Build success response without data using builder")
        void buildSuccessResponseWithoutData() {
            APIResponse<Void> response = APIResponse.<Void>builder()
                    .status(200)
                    .message("Success message")
                    .build();

            assertEquals(200, response.status());
            assertNull(response.data());
            assertNull(response.error());
            assertEquals("Success message", response.message());
            assertTrue(response.isSuccess());
        }

        @Test
        @DisplayName("Build error response using builder")
        void buildErrorResponse() {
            APIError apiError = new APIError("ERROR", "Error occurred");
            APIResponse<String> response = APIResponse.<String>builder()
                    .status(BaseAPICode.BAD_REQUEST)
                    .error(apiError)
                    .message("Error message")
                    .build();

            assertEquals(400, response.status());
            assertNull(response.data());
            assertEquals(apiError, response.error());
            assertEquals("Error message", response.message());
            assertFalse(response.isSuccess());
        }

        @Test
        @DisplayName("Throw exception for success response with error")
        void throwExceptionForSuccessResponseWithError() {
            APIError apiError = new APIError("ERROR", "Error occurred");
            assertThrows(IllegalArgumentException.class, () -> {
                APIResponse.<String>builder()
                        .status(200)
                        .error(apiError)
                        .message("Message")
                        .build();
            });
        }

        @Test
        @DisplayName("Throw exception for error response without error")
        void throwExceptionForErrorResponseWithoutError() {
            assertThrows(IllegalArgumentException.class, () -> {
                APIResponse.<String>builder()
                        .status(400)
                        .data("data")
                        .message("Message")
                        .build();
            });
        }

        @Test
        @DisplayName("Throw exception for error response with data")
        void throwExceptionForErrorResponseWithData() {
            APIError apiError = new APIError("ERROR", "Error occurred");
            assertThrows(IllegalArgumentException.class, () -> {
                APIResponse.<String>builder()
                        .status(400)
                        .error(apiError)
                        .data("data")
                        .message("Message")
                        .build();
            });
        }

        @Test
        @DisplayName("Use withData convenience method")
        void useWithDataConvenienceMethod() {
            APIResponse<String> response = APIResponse.withData("test-data")
                    .status(BaseAPICode.SUCCESS)
                    .message("Success")
                    .build();

            assertEquals(200, response.status());
            assertEquals("test-data", response.data());
            assertEquals("Success", response.message());
        }
    }

    @Nested
    @DisplayName("Utility Methods")
    class UtilityMethods {

        @Test
        @DisplayName("Get data as Optional")
        void getDataAsOptional() {
            APIResponse<String> response = APIResponse.success("test-data", BaseAPICode.SUCCESS);
            Optional<String> data = response.getData();

            assertTrue(data.isPresent());
            assertEquals("test-data", data.get());
        }

        @Test
        @DisplayName("Get empty Optional for null data")
        void getEmptyOptionalForNullData() {
            APIResponse<Void> response = APIResponse.success(BaseAPICode.SUCCESS, "No data");
            Optional<Void> data = response.getData();

            assertFalse(data.isPresent());
        }

        @Test
        @DisplayName("Get error as Optional")
        void getErrorAsOptional() {
            APIError apiError = new APIError("ERROR", "Error occurred");
            APIResponse<String> response = APIResponse.error(apiError, BaseAPICode.BAD_REQUEST);
            Optional<APIError> error = response.getError();

            assertTrue(error.isPresent());
            assertEquals(apiError, error.get());
        }

        @Test
        @DisplayName("Get empty Optional for null error")
        void getEmptyOptionalForNullError() {
            APIResponse<String> response = APIResponse.success("data", BaseAPICode.SUCCESS);
            Optional<APIError> error = response.getError();

            assertFalse(error.isPresent());
        }
    }

    @Test
    @DisplayName("Constructor validation for null message and timestamp")
    void constructorValidationForNullMessageAndTimestamp() {
        assertThrows(NullPointerException.class, () -> {
            new APIResponse<>(200, "data", null, null, Instant.now());
        });

        assertThrows(NullPointerException.class, () -> {
            new APIResponse<>(400, null, new APIError("ERROR", "Error"), "message", null);
        });
    }
}
