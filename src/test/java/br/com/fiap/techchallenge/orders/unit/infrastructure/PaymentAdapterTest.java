package br.com.fiap.techchallenge.orders.unit.infrastructure;

import br.com.fiap.techchallenge.orders.application.dtos.in.PaymentInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.PaymentAdapter;
import br.com.fiap.techchallenge.orders.infrastructure.dtos.CreatePaymentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentAdapterTest {

    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final PaymentAdapter adapter = new PaymentAdapter(restTemplate);

    @Test
    @DisplayName("deve retornar null quando CreatePaymentDTO for null")
    void shouldReturnNullWhenDtoNull() {
        PaymentInDTO result = adapter.createPayment(null);
        assertNull(result);
    }

    @Test
    @DisplayName("deve retornar null quando URLs não estiverem configuradas")
    void shouldReturnNullWhenUrlsAreBlank() {
        ReflectionTestUtils.setField(adapter, "paymentCreateUrl", "");
        CreatePaymentDTO dto = new CreatePaymentDTO(UUID.randomUUID(), Optional.empty(), BigDecimal.TEN);

        PaymentInDTO result = adapter.createPayment(dto);
        assertNull(result);
    }

    @Test
    @DisplayName("deve criar pagamento com sucesso quando API responder 201")
    void shouldCreatePaymentSuccessfully() throws Exception {
        // Arrange
        ReflectionTestUtils.setField(adapter, "paymentCreateUrl", "http://localhost/payments");
        UUID orderId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        CreatePaymentDTO inputDto = new CreatePaymentDTO(orderId, Optional.empty(), BigDecimal.TEN);

        // Instanciação do Record privado via Reflexão (Igual ao seu exemplo base)
        Object recordInstance = createPaymentResponseInstance(paymentId, "mock-qr-code");

        ResponseEntity<Object> response = new ResponseEntity<>(recordInstance, HttpStatus.CREATED);

        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn((ResponseEntity) response);

        // Act
        PaymentInDTO result = adapter.createPayment(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(paymentId, result.id());
        assertEquals("mock-qr-code", result.qrData());
        verify(restTemplate).postForEntity(eq("http://localhost/payments"), any(), any());
    }

    @Test
    @DisplayName("deve buscar pagamento por orderId com sucesso")
    void shouldGetPaymentByOrderIdSuccessfully() throws Exception {
        // Arrange
        String baseUrl = "http://localhost/get-payment";
        ReflectionTestUtils.setField(adapter, "paymentGetUrl", baseUrl);
        UUID orderId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();

        Object recordInstance = createPaymentResponseInstance(paymentId, "qr-data-get");
        ResponseEntity<Object> response = new ResponseEntity<>(recordInstance, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any())).thenReturn((ResponseEntity) response);

        PaymentInDTO result = adapter.getPaymentByOrderId(orderId);

        assertNotNull(result);
        assertEquals(paymentId, result.id());

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).getForEntity(urlCaptor.capture(), any());

        assertTrue(urlCaptor.getValue().contains(baseUrl + "/" + orderId));
    }

    @Test
    @DisplayName("deve retornar null quando a API de busca lançar exceção")
    void shouldReturnNullWhenGetApiThrows() {
        ReflectionTestUtils.setField(adapter, "paymentGetUrl", "http://localhost/api");
        when(restTemplate.getForEntity(anyString(), any())).thenThrow(new RuntimeException("timeout"));

        PaymentInDTO result = adapter.getPaymentByOrderId(UUID.randomUUID());

        assertNull(result);
    }

    @Test
    @DisplayName("deve montar o corpo da requisição corretamente no create")
    void shouldPostCorrectBody() {
        ReflectionTestUtils.setField(adapter, "paymentCreateUrl", "http://api/create");
        UUID orderId = UUID.randomUUID();
        CreatePaymentDTO inputDto = new CreatePaymentDTO(orderId, Optional.empty(), BigDecimal.valueOf(50.0));

        adapter.createPayment(inputDto);

        ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(restTemplate).postForEntity(anyString(), bodyCaptor.capture(), any());

        Object capturedBody = bodyCaptor.getValue();
        assertEquals(orderId.toString(), ReflectionTestUtils.getField(capturedBody, "orderId"));
        assertEquals(BigDecimal.valueOf(50.0), ReflectionTestUtils.getField(capturedBody, "amount"));
    }

    private Object createPaymentResponseInstance(UUID id, String qrData) throws Exception {
        Class<?> recordClass = Class.forName(
                "br.com.fiap.techchallenge.orders.infrastructure.adapters.PaymentAdapter$PaymentResponseDTO"
        );
        Constructor<?> constructor = recordClass.getDeclaredConstructor(UUID.class, String.class);
        constructor.setAccessible(true);
        return constructor.newInstance(id, qrData);
    }
}