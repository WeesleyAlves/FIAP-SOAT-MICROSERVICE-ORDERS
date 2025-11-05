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

    public static <T> ApiResponseDTO<T> send(Integer code, String message, T data) {
        return ApiResponseDTO.<T>builder()
            .status(code)
            .message(message)
            .data(data)
            .build();
    }

    public static <T> ApiResponseDTO<T> send(Integer code, String message) {
        return send(code, message, null);
    }

    public static <T> ApiResponseDTO<T> send(String message) {
        return send(200, message, null);
    }

    public static <T> ApiResponseDTO<T> send(Integer code) {
        return send(code, null, null);
    }
}