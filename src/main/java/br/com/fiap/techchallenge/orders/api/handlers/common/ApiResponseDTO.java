package br.com.fiap.techchallenge.orders.api.handlers.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return ApiResponseDTO.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> success(String message) {
        return success(message, null);
    }

    public static <T> ApiResponseDTO<T> error(int status, String message) {
        return ApiResponseDTO.<T>builder()
                .status(status)
                .message(message)
                .build();
    }
} 