Feature: Test orders route

  Scenario: GET request for /orders/
    When I do a GET request to "/orders/"
    Then the return is "Hello World"
