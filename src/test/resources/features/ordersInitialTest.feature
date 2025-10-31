#language: pt
Funcionalidade: Testar endpoints simples

  Cenario: Deve retornar "Hello World" ao acessar /orders/
    Quando eu faço uma requisição GET para "/orders/"
    Então o status deve ser 200
    E o corpo deve conter "Hello World"

  Cenario: Deve retornar "tchau" ao acessar /orders/123
    Quando eu faço uma requisição GET para "/orders/123"
    Então o status deve ser 200
    E o corpo deve conter "123"