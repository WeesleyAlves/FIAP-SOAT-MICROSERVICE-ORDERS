# language: pt
Funcionalidade: Testar os endpoints publicos de pedidos
  São testados os endpoints de fila,
  criação de pedido e buscar pedido por ID.
  Tanto cenários de erro quanto de sucesso.

  Cenario: Consultar fila publica de pedidos
    Quando eu realizar um requisicao GET para "/orders/queue"
    Entao deve retornar status 200
    E deve retornar um JSON com o schema "orders-public-queue-schema.json"

  Cenario: Consultar um pedido completo por ID
    Dado que existe um pedido com os seguintes dados:
      | id                                   | order_number | status   | customer_id                          | notes       | created_at           | updated_at           | payment_id                           | payment_qr_data        |
      | 10ca5645-26e0-4dda-bb3c-11a00009774b | 123          | RECEBIDO | 5ad5a64d-98a2-4e2b-bb29-2fa17f40f111 | Sem cebola  | 2025-11-03T18:00:00Z | 2025-11-03T18:05:00Z | b4e3b73a-55a7-46b3-8cb3-88077b08deac | qrcode-pix-1234567890  |
    E os seguintes produtos:
      | id                                   | name         | quantity | price | totalValue |
      | 4b31b7f4-b43a-4af1-a48d-69cceaa0d101 | X-Burger     | 2        | 15.90 | 31.80      |
      | 4b31b7f4-b43a-4af1-a48d-69cceaa0d102 | Refrigerante | 1        | 8.50  | 8.50       |
    Quando eu realizar um requisicao GET para "/orders/10ca5645-26e0-4dda-bb3c-11a00009774b"
    Entao deve retornar status 200
    E deve retornar um JSON com o schema "order-complete-schema.json"