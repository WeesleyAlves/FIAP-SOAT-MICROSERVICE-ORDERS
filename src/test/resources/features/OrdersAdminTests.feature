# language: pt
Funcionalidade: Testar os endpoints administrativos dos pedidos
  São testados os endpoints de reiniciar numero da fila,
  alterar status do pedido e listar todos os pedidos.

  Cenario: Reiniciar a numeração dos pedidos da fila para 1
    Quando eu realizar uma requisicao POST para "/admin/reset-queue-number"
    Entao deve retornar status 200
    E deve retornar um JSON com o schema "reset-queue-schema.json"