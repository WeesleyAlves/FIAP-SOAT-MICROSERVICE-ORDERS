package br.com.fiap.techchallenge.orders.infrastructure.adapters;

import br.com.fiap.techchallenge.orders.application.dtos.in.PaymentInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.PaymentDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.dtos.CreatePaymentDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
public class PaymentAdapter implements PaymentDatasource {

    @Value("${external.ms.payment.createPayment}")
    private String paymentCreateUrl;

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectWriter JSON_WRITER = OBJECT_MAPPER.writer();

    private record PaymentRequestDTO(
            @JsonProperty("order_id") String orderId,
            @JsonProperty("customer_id") String customerId,
            @JsonProperty("amount") BigDecimal amount
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record PaymentResponseDTO(
            @JsonProperty("id") UUID id,
            @JsonProperty("qr_code_payload") String qrData
    ) {}

    @Override
    public PaymentInDTO createPayment(CreatePaymentDTO dto) {
        if (dto == null) {
            return null;
        }

        if (paymentCreateUrl == null || paymentCreateUrl.isBlank()) {
            return null;
        }

        try {
            String customerIdStr = dto.customerId().map(UUID::toString).orElse(null);

            PaymentRequestDTO bodyDto = new PaymentRequestDTO(
                    dto.orderId().toString(),
                    customerIdStr,
                    dto.amount()
            );

            String bodyJson = JSON_WRITER.writeValueAsString(bodyDto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(paymentCreateUrl))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                String responseBody = response.body();

                if (responseBody != null && !responseBody.isBlank()) {
                    PaymentResponseDTO paymentResponse =
                            OBJECT_MAPPER.readValue(responseBody, PaymentResponseDTO.class);

                    return new PaymentInDTO(
                            paymentResponse.id(),
                            paymentResponse.qrData()
                    );
                }
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PaymentInDTO getPaymentByOrderId(UUID orderId) {
        return new PaymentInDTO(
                UUID.randomUUID(),
                "qr_code_" + orderId
        );
    }
}
