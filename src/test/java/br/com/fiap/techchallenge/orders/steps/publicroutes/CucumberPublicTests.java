package br.com.fiap.techchallenge.orders.steps.publicroutes;

import br.com.fiap.techchallenge.orders.infrastructure.adapters.InventoryAdapter;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.OrderAdapter;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.PaymentAdapter;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.ProductsAdapter;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Suite
@SelectClasspathResource("features/OrdersPublicTests.feature")
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME,
    value = "br.com.fiap.techchallenge.orders.steps.publicroutes,br.com.fiap.techchallenge.orders.steps.common"
)
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberPublicTests {
    @MockitoBean
    private OrderAdapter orderAdapter;

    @MockitoBean
    private ProductsAdapter productsAdapter;

    @MockitoBean
    private PaymentAdapter paymentAdapter;

    @MockitoBean
    private InventoryAdapter inventoryAdapter;
}
