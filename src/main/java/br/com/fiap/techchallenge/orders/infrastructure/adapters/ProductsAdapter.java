package br.com.fiap.techchallenge.orders.infrastructure.adapters;

import br.com.fiap.techchallenge.orders.application.dtos.in.ProductInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.ProductsDatasource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductsAdapter implements ProductsDatasource {
    @Value("${external.ms.products.getbyid}")
    private String productsBaseUrl;

    private final RestTemplate restTemplate;


    public ProductsAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private record ProductResponseDTO(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            boolean isActive,
            String imagePath,
            Long categoryId,
            String createdAt,
            String updatedAt
    ) {}

    @Override
    public List<ProductInDTO> getAllByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        String idsString = ids.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(","));

        String url = UriComponentsBuilder.fromUriString(productsBaseUrl)
                .queryParam("productIds", idsString)
                .toUriString();

        // Define o tipo de retorno esperado (Lista de ProductResponseDTO)
        // é necessário para lidar com Listas genéricas no RestTemplate
        ParameterizedTypeReference<List<ProductResponseDTO>> typeRef = new ParameterizedTypeReference<>() {};

        try {
            ResponseEntity<List<ProductResponseDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    typeRef
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().stream()
                        .map(prod -> new ProductInDTO(
                                prod.id(),
                                prod.name(),
                                prod.price()
                        )).toList();
            }

            return Collections.emptyList();

        } catch (Exception _) {
            return Collections.emptyList();
        }
    }
}