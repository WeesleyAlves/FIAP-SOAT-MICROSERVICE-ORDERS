package br.com.fiap.techchallenge.orders.unit.api.handlers;

import br.com.fiap.techchallenge.orders.api.exceptions.EmptyOrderProductListException;
import br.com.fiap.techchallenge.orders.api.exceptions.OrderNotFoundException;
import br.com.fiap.techchallenge.orders.api.exceptions.OrderNumberSequenceNotInitializedException;
import br.com.fiap.techchallenge.orders.api.exceptions.OrderStatusNotFoundException;
import br.com.fiap.techchallenge.orders.api.handlers.GlobalExceptionHandler;
import br.com.fiap.techchallenge.orders.api.handlers.common.ApiResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private final String testMessage = "Mensagem de Teste";

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    // -------------------------------------------------------------------------
    // Testes de Exceções de Negócio (4xx e 5xx)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve tratar OrderNotFoundException (404)")
    void shouldHandleOrderNotFoundException() {
        OrderNotFoundException ex = new OrderNotFoundException(testMessage);

        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleOrderNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(testMessage, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Deve tratar OrderStatusNotFoundException (404)")
    void shouldHandleOrderStatusNotFoundException() {
        OrderStatusNotFoundException ex = new OrderStatusNotFoundException(testMessage);

        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleOrderStatusNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(testMessage, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Deve tratar EmptyOrderProductListException (400 BAD REQUEST)")
    void shouldHandleEmptyOrderProductListException() {
        EmptyOrderProductListException ex = new EmptyOrderProductListException(testMessage);

        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleEmptyOrderProductListException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals(testMessage, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException (400 BAD REQUEST)")
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException(testMessage);

        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals(testMessage, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Deve tratar OrderNumberSequenceNotInitializedException (500 INTERNAL SERVER ERROR)")
    void shouldHandleOrderNumberSequenceNotInitializedException() {
        OrderNumberSequenceNotInitializedException ex = new OrderNumberSequenceNotInitializedException(testMessage);

        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleOrderNumberSequenceNotInitializedException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals(testMessage, response.getBody().getMessage());
    }

    // -------------------------------------------------------------------------
    // Testes de Exceções do Spring/Web
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve tratar NoResourceFoundException (404)")
    void shouldHandleNoResourceFoundException() {
        NoResourceFoundException ex = mock(NoResourceFoundException.class);

        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleNoResourceFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException (405 METHOD NOT ALLOWED) e formatar mensagem")
    void shouldHandleValidationExceptions() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "campo", "não pode ser nulo");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleValidationExceptions(ex);

        String expectedMessage = "campo: não pode ser nulo";
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getBody().getStatus());
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Deve tratar HttpMessageConversionException (500) com mensagem genérica")
    void shouldHandleHttpMessageConversionException() {
        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleHttpMessageConversionException();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("Dados da requisição incorretos.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentTypeMismatchException (500) com mensagem genérica")
    void shouldHandleMethodArgumentTypeMismatchException() {
        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleMethodArgumentTypeMismatchException();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("Dados da requisição incorretos.", response.getBody().getMessage());
    }

    // -------------------------------------------------------------------------
    // Teste de Exceção Genérica
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve tratar Exception genérica (500) com a mensagem da exceção")
    void shouldHandleGenericException() {
        Exception ex = new Exception(testMessage);

        ResponseEntity<ApiResponseDTO<Void>> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals(testMessage, response.getBody().getMessage());
    }
}