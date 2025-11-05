package br.com.fiap.techchallenge.orders.api.handlers;


import br.com.fiap.techchallenge.orders.api.exceptions.EmptyOrderProductListException;
import br.com.fiap.techchallenge.orders.api.exceptions.OrderNotFoundException;
import br.com.fiap.techchallenge.orders.api.exceptions.OrderNumberSequenceNotInitializedException;
import br.com.fiap.techchallenge.orders.api.exceptions.OrderStatusNotFoundException;
import br.com.fiap.techchallenge.orders.api.handlers.common.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleOrderNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponseDTO.send(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(OrderStatusNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleOrderStatusNotFoundException(OrderStatusNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponseDTO.send(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(EmptyOrderProductListException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleEmptyOrderProductListException(EmptyOrderProductListException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseDTO.send(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(OrderNumberSequenceNotInitializedException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleOrderNumberSequenceNotInitializedException(OrderNumberSequenceNotInitializedException ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseDTO.send(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("Erro de validação");

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ApiResponseDTO.send(HttpStatus.METHOD_NOT_ALLOWED.value(), errorMessage));
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleHttpMessageConversionException(HttpMessageConversionException ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseDTO.send(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Dados da requisição incorretos."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseDTO.send(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Dados da requisição incorretos."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseDTO.send(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseDTO.send(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponseDTO.send(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }
}