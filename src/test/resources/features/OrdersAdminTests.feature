# language: pt
Funcionalidade: Testar os endpoints administrativos dos pedidos
  São testados os endpoints de reiniciar numero da fila,
  alterar status do pedido e listar todos os pedidos.


  Cenario: Reiniciar a numeração dos pedidos da fila para 1
    Quando eu realizar uma requisicao POST para "/admin/reset-queue-number"
    Entao deve retornar status 200
    E deve retornar um JSON com o schema "reset-queue-schema.json"


  Cenario: Atualizar o status de um pedido existente
    Dado que existe um pedido com o seguinte o ID "10ca5645-26e0-4dda-bb3c-11a00009774b"
    E os seguintes dados para a atualizacao:
    """
      {
          "id": "10ca5645-26e0-4dda-bb3c-11a00009774b",
          "status_id": 2
      }
    """
    Quando eu realizar uma requisicao PATCH para "/admin/order/status"
    Entao deve retornar status 200
    E deve retornar um JSON com o schema "order-admin-schema.json"