# language: pt
Funcionalidade: Testar os endpoints publicos de pedidos
  São testados os endpoints de fila,
  criação de pedido e buscar pedido por ID.
  Tanto cenários de erro quanto de sucesso.

  Cenario: Consultar fila publica de pedidos
    Quando eu realizar um requisicao GET para "/orders/queue"
    Entao deve retornar status 200
    E deve retornar um JSON com o schema "orders-public-queue-schema.json"