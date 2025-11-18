package br.com.fiap.techchallenge.orders.steps.common;

import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderProductItemOutDTO;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.OrderAdapter;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

public class CommonSteps {
    @Autowired
    private TestContext context;

    @Autowired
    private OrderAdapter orderAdapter;

    @Dado("que existem os seguintes pedidos:")
    public void queExistemOsSeguintesPedidos(DataTable dataTable){
        var listOrders = dataTable.asMaps().stream()
                .map(map -> new CompleteOrderDTO(
                        UUID.fromString( map.get("id") ),
                        Integer.parseInt( map.get("order_number") ),
                        map.get("status"),
                        UUID.fromString( map.get("customer_id") ),
                        map.get("notes"),
                        new BigDecimal( map.get("price") ),
                        OffsetDateTime.parse(map.get("created_at")).toLocalDateTime(),
                        OffsetDateTime.parse(map.get("updated_at")).toLocalDateTime(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                ))
                .toList();

        context.setExistingOrders(listOrders);
    }

    @E("a seguinte relacao de pedido produto:")
    public void aSeguinteRelacaoDePedidoProduto(DataTable dataTable){
        List<CompleteOrderDTO> updatedOrders = new ArrayList<>();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<Map<String, Object>> products = new ArrayList<>();

        for (Map<String, String> row : rows) {
            Map<String, Object> product = new HashMap<>();

            product.put("order_id", row.get("order_id"));
            product.put("product_id", row.get("product_id"));
            product.put("name", row.get("name"));
            product.put("quantity", Integer.parseInt(row.get("quantity")));
            product.put("price", Double.parseDouble(row.get("price")));
            product.put("total_value", Double.parseDouble(row.get("total_value")));

            products.add(product);
        }

        for (CompleteOrderDTO order : context.getExistingOrders() ) {

            List<OrderProductItemOutDTO> productsForOrder = new ArrayList<>();

            for (Map<String, Object> product : products) {
                var uuid = UUID.fromString( (String) product.get("order_id") );
                if (order.id().equals( uuid )) {
                    productsForOrder.add( new OrderProductItemOutDTO(
                            UUID.fromString( product.get("product_id").toString() ),
                            product.get("name").toString(),
                            new BigDecimal( product.get("price").toString() ),
                            Integer.parseInt( product.get("quantity").toString() ),
                            new BigDecimal( product.get("total_value").toString() )
                    ) );
                }
            }

            CompleteOrderDTO updated = order.withProducts(productsForOrder);

            updatedOrders.add(updated);
        }

        Mockito.when(orderAdapter.findOrderByStatusIn( any() ))
            .thenReturn(updatedOrders);

        Mockito.when(orderAdapter.findAllOrders())
            .thenReturn(updatedOrders);
    }
}
