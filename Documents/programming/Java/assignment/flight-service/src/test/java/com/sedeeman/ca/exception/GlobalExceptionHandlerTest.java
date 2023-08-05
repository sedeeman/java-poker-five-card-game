package com.sedeeman.ca.exception;

import com.sedeeman.ca.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationException() {
        List<FieldError> fieldErrors = Collections.singletonList(new FieldError("objectName", "fieldName", "Field is required"));

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException((MethodParameter) null, bindingResult);

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponse.getStatus());
        assertEquals("Validation error", errorResponse.getMessage());
        assertEquals(Collections.singletonList("fieldName Field is required"), errorResponse.getErrors());
    }

    @Test
    void testHandleDataIntegrityViolationException() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Data integrity violation");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleDataIntegrityViolationException(exception);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        ErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getCode());
        assertEquals(HttpStatus.CONFLICT.getReasonPhrase(), errorResponse.getStatus());
        assertEquals("Data integrity violation", errorResponse.getMessage());
        assertEquals(Collections.singletonList("Data integrity violation"), errorResponse.getErrors());
    }

    @Test
    void testHandleDataLoadingException() {
        DataLoadingException exception = new DataLoadingException("Data loading error");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleDataLoadingException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getStatus());
        assertEquals("Data Loading Error", errorResponse.getMessage());
        assertEquals(Collections.singletonList("Data loading error"), errorResponse.getErrors());
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("An unexpected error");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getStatus());
        assertEquals("An error occurred", errorResponse.getMessage());
        assertEquals(Collections.singletonList("An unexpected error"), errorResponse.getErrors());
    }
}
