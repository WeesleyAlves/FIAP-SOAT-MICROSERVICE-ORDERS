package br.com.fiap.techchallenge.orders.infrastructure.adapters;

import br.com.fiap.techchallenge.orders.application.dtos.in.PaymentInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.PaymentDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.dtos.CreatePaymentDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentAdapter implements PaymentDatasource {

    @Value("${external.ms.payment.createPayment}")
    private String paymentCreateUrl;

    @Value("${external.ms.payment.getPayment}")
    private String paymentGetUrl;

    private final RestTemplate restTemplate;

    public PaymentAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Records internos para mapear o contrato da API externa (JSON)
    private record PaymentRequestDTO(
            @JsonProperty("order_id") String orderId,
            @JsonProperty("customer_id") String customerId,
            @JsonProperty("amount") BigDecimal amount
    ) {}

    private record PaymentResponseDTO(
            UUID id,
            @JsonProperty("qr_code_payload") String qrData
    ) {}

    @Override
    public PaymentInDTO createPayment(CreatePaymentDTO dto) {
        if (dto == null || paymentCreateUrl == null || paymentCreateUrl.isBlank()) {
            return null;
        }

        try {
            String customerIdStr = dto.customerId().map(UUID::toString).orElse(null);

            PaymentRequestDTO requestBody = new PaymentRequestDTO(
                    dto.orderId().toString(),
                    customerIdStr,
                    dto.amount()
            );

            ResponseEntity<PaymentResponseDTO> response = restTemplate.postForEntity(
                    paymentCreateUrl,
                    requestBody,
                    PaymentResponseDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return new PaymentInDTO(
                        response.getBody().id(),
                        response.getBody().qrData()
                );
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PaymentInDTO getPaymentByOrderId(UUID orderId) {
        if (orderId == null || paymentGetUrl == null || paymentGetUrl.isBlank()) {
            return null;
        }

        try {
            // Monta a URL concatenando o ID, similar ao UriComponentsBuilder do seu exemplo
            String url = UriComponentsBuilder.fromUriString(paymentGetUrl)
                    .pathSegment(orderId.toString())
                    .toUriString();

            ResponseEntity<PaymentResponseDTO> response = restTemplate.getForEntity(
                    url,
                    PaymentResponseDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return new PaymentInDTO(
                        response.getBody().id(),
                        response.getBody().qrData()
                );
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}