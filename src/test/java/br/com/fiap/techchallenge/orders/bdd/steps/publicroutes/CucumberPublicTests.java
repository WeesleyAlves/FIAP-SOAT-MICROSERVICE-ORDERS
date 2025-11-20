package br.com.fiap.techchallenge.orders.bdd.steps.publicroutes;

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
import org.springframework.boot.test.mock.mockito.MockBean;

@Suite
@SelectClasspathResource("features/OrdersPublicTests.feature")
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME,
    value = "br.com.fiap.techchallenge.orders.bdd.steps.publicroutes,br.com.fiap.techchallenge.orders.bdd.steps.common"
)
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberPublicTests {
    @MockBean
    private OrderAdapter orderAdapter;

    @MockBean
    private ProductsAdapter productsAdapter;

    @MockBean
    private PaymentAdapter paymentAdapter;

    @MockBean
    private InventoryAdapter inventoryAdapter;
}
