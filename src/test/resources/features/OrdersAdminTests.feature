# language: pt
Funcionalidade: Testar os endpoints administrativos dos pedidos
  São testados os endpoints de reiniciar numero da fila,
  alterar status do pedido e listar todos os pedidos.


  Cenario: Reiniciar a numeração dos pedidos da fila para 1
    Dado que preciso reiniciar a fila de pedidos
    Quando eu realizar uma requisicao POST para "/admin/reset-queue-number"
    Entao deve retornar status 200
    E deve retornar um JSON com o schema "reset-queue-schema.json"


# TODO: ajustar isso aqui
#  Cenario: Atualizar o status de um pedido existente
#    Dado que existe um pedido com o seguinte o ID "10ca5645-26e0-4dda-bb3c-11a00009774b"
#    E os seguintes dados para a atualizacao:
#    """
#      {
#          "id": "10ca5645-26e0-4dda-bb3c-11a00009774b",
#          "status_id": 2
#      }
#    """
#    Quando eu realizar uma requisicao PATCH para "/admin/order/status"
#    Entao deve retornar status 200
#    E deve retornar um JSON com o schema "order-admin-schema.json"


  Cenario: Listar pedidos pela zona administrativa
    Dado que existem os seguintes pedidos:
      | id                                    | order_number  | status      | customer_id                           | notes         | price | created_at            | updated_at            |
      | ef891941-2291-4c46-9c6c-eaa949e090b5  | 1             | Finalizado  | 80204008-aeb9-4ece-8480-0a7817cbe91b  | Sem mostarda  | 48.80 | 2025-11-03T18:00:00Z  | 2025-11-03T18:00:00Z  |
      | 1f6f1a3f-f2fb-42da-938f-776f9cf7c24f  | 2             | Recebido    | e95bd7ef-ef12-4075-a3e4-1a24b3f34c38  | Sem cebola    | 24.40 | 2025-11-03T18:00:00Z  | 2025-11-03T18:00:00Z  |
    E a seguinte relacao de pedido produto:
      | order_id                              | product_id                            | quantity  | price   | total_value | name          |
      | ef891941-2291-4c46-9c6c-eaa949e090b5  | ffa98bf1-2eae-46bb-b7d5-216f3c2b8ae8  | 2         | 15.90   | 31.80       | X-Burger      |
      | ef891941-2291-4c46-9c6c-eaa949e090b5  | dd061d8e-4fbe-47da-a87f-87c90304b0ca  | 2         | 8.50    | 17.00       | Refrigerante  |
      | 1f6f1a3f-f2fb-42da-938f-776f9cf7c24f  | ffa98bf1-2eae-46bb-b7d5-216f3c2b8ae8  | 1         | 15.90   | 15.90       | X-Burger      |
      | 1f6f1a3f-f2fb-42da-938f-776f9cf7c24f  | dd061d8e-4fbe-47da-a87f-87c90304b0ca  | 1         | 8.50    | 8.50        | Refrigerante  |
    Quando eu realizar um requisicao GET para "/admin/orders"
    Entao deve retornar status 200
    E deve retornar um JSON com o schema "orders-admin-list-schema.json"