# language: pt
  Funcionalidade: Testar o endpoint de pedidos

    Cenario: Requisicao GET para /orders/
      Quando eu faço uma requisição GET para "/orders/"
      Entao o retorno deve conter "Hello World"