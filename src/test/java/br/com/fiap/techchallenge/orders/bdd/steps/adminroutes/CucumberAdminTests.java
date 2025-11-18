package br.com.fiap.techchallenge.orders.bdd.steps.adminroutes;

import br.com.fiap.techchallenge.orders.infrastructure.adapters.OrderAdapter;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Suite
@SelectClasspathResource("features/OrdersAdminTests.feature")
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME,
    value = "br.com.fiap.techchallenge.orders.bdd.steps.adminroutes,br.com.fiap.techchallenge.orders.bdd.steps.common"
)
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberAdminTests {

    @MockitoBean
    private OrderAdapter orderAdapter;

}
