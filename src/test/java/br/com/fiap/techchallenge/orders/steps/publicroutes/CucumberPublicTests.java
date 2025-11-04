package br.com.fiap.techchallenge.orders.steps.publicroutes;

import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

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
}
