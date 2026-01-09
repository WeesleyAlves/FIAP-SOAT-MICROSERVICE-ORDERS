# 🍔 FIAP FastFood - Microserviço de Pedidos (Orders)

[![CI/CD](https://github.com/WeesleyAlves/FIAP-SOAT-MICROSERVICE-ORDERS/actions/workflows/ci-cd.yaml.yml/badge.svg)](https://github.com/your-repo/FIAP-SOAT-MS-ORDERS/actions)
[![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=WeesleyAlves_FIAP-SOAT-MICROSERVICE-ORDERS&metric=alert_status)](https://sonarcloud.io/dashboard?id=WeesleyAlves_FIAP-SOAT-MICROSERVICE-ORDERS)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=WeesleyAlves_FIAP-SOAT-MICROSERVICE-ORDERS&metric=coverage)](https://sonarcloud.io/dashboard?id=WeesleyAlves_FIAP-SOAT-MICROSERVICE-ORDERS)

## 📋 Sobre o Projeto

Microserviço responsável pelo gerenciamento de pedidos do sistema FastFood da FIAP Tech Challenge. Este serviço faz parte de uma arquitetura de microserviços e gerencia todo o ciclo de vida dos pedidos, desde a criação até a finalização, integrando-se com outros microserviços (Produtos, Inventário e Pagamento).

### 🎯 Funcionalidades

- **Gestão de Pedidos**: Criação, consulta e atualização de pedidos
- **Fila de Pedidos**: Sistema de numeração e gerenciamento de fila
- **Integração com Pagamentos**: Criação automática de pagamento ao criar pedido
- **Integração com Inventário**: Atualização automática do estoque
- **Integração com Produtos**: Validação e consulta de produtos
- **API REST**: Endpoints públicos e administrativos
- **Documentação OpenAPI**: Swagger UI integrado

## 🛠️ Tecnologias Utilizadas

### Core
- **Java 23** - Linguagem de programação
- **Spring Boot 3.3.5** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **Flyway** - Migração de banco de dados

### Documentação
- **SpringDoc OpenAPI 2.6.0** - Documentação da API (Swagger)

### Testes
- **JUnit 5** - Framework de testes
- **Cucumber 7.18.0** - Testes BDD
- **H2 Database** - Banco em memória para testes
- **JaCoCo 0.8.12** - Cobertura de código

### Infraestrutura
- **Docker** - Containerização
- **Docker Compose** - Orquestração local
- **Kubernetes** - Orquestração em produção
- **Amazon EKS** - Kubernetes gerenciado na AWS
- **Amazon ECR** - Registry de imagens Docker

### CI/CD e Qualidade
- **GitHub Actions** - Pipeline CI/CD
- **SonarCloud** - Análise de código e qualidade
- **Maven** - Gerenciamento de dependências

### Outras Bibliotecas
- **Lombok** - Redução de boilerplate
- **JSON Schema Validator** - Validação de schemas JSON

## 📐 Arquitetura

O projeto segue os princípios da **Clean Architecture**, com clara separação de responsabilidades:

```
├── api/                      # Camada de apresentação (Controllers/Handlers)
│   └── handlers/            # Endpoints REST
├── application/             # Camada de aplicação
│   ├── controllers/        # Controladores de negócio
│   ├── dtos/              # Objetos de transferência
│   ├── gateways/          # Interfaces de saída
│   └── presenters/        # Formatação de respostas
├── core/                   # Camada de domínio
│   ├── entities/          # Entidades de negócio
│   └── use_cases/        # Casos de uso
└── infrastructure/        # Camada de infraestrutura
    ├── datasources/      # Implementação de gateways
    ├── entities/         # Entidades JPA
    └── repositories/     # Repositórios Spring Data
```

## Cobertura + Sonar

É possível verificar os dados do sonar em https://sonarcloud.io/project/overview?id=WeesleyAlves_FIAP-SOAT-MICROSERVICE-ORDERS ;

<img width="1633" height="977" alt="coverage-ms-orders" src="https://github.com/user-attachments/assets/fe28baac-2318-474c-be13-1a054ca7c5a1" />

## 🚀 Como Executar Localmente

### Pré-requisitos

- Java 23 ou superior
- Maven 3.9+
- Docker e Docker Compose
- Git

### 1️⃣ Clonar o Repositório

```bash
git clone https://github.com/your-org/FIAP-SOAT-MS-ORDERS.git
cd FIAP-SOAT-MS-ORDERS
```

### 2️⃣ Configurar Variáveis de Ambiente

Copie o arquivo de exemplo e configure as variáveis:

```bash
cp .env.example .env
```

Edite o arquivo `.env` com as configurações necessárias:

```properties
# Banco de Dados
POSTGRES_DB=fastfood_orders
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Microserviços Externos
MS_PRODUCTS_GETBYID=http://localhost:8081/api/v1/products/find-all-by-ids
MS_INVENTORY_UPDATEINVENTORY=http://localhost:8081/api/v1/inventory/discount-items-by-products
MS_PAYMENT_CREATE=http://localhost:8083/api/v1/payments/
MS_PAYMENT_GETBYID=http://localhost:8083/api/v1/payments/order
```

### 3️⃣ Executar com Docker Compose

```bash
docker-compose up --build
```

A aplicação estará disponível em: `http://localhost:8080`

### 4️⃣ Executar Manualmente (Desenvolvimento)

```bash
# Subir apenas o banco de dados
docker-compose up postgres_db -d

# Executar a aplicação
./mvnw spring-boot:run
```

### 5️⃣ Acessar a Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/orders/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/orders/v3/api-docs

## 📍 Endpoints Principais

### Zona Pública (`/api/v1/orders`)

- `GET /api/v1/orders/{id}` - Buscar pedido por ID
- `GET /api/v1/orders/queue` - Listar pedidos em fila
- `POST /api/v1/orders` - Criar novo pedido

### Zona Administrativa (`/api/v1/admin/orders`)

- `GET /api/v1/admin/orders` - Listar todos os pedidos
- `PATCH /api/v1/admin/orders/status` - Atualizar status do pedido
- `POST /api/v1/admin/orders/reset-queue-number` - Reiniciar contador da fila

## 🧪 Executar Testes

### Todos os Testes

```bash
./mvnw clean verify
```

### Apenas Testes Unitários

```bash
./mvnw test
```

### Testes BDD (Cucumber)

```bash
./mvnw test -Dtest=Cucumber*
```

### Relatório de Cobertura (JaCoCo)

```bash
./mvnw clean verify
# Relatório em: target/site/jacoco/index.html
```

## 🔄 CI/CD Pipeline

O projeto utiliza **GitHub Actions** para automação de CI/CD com as seguintes etapas:

### Pipeline de Pull Request e Push

1. **Build & SonarCloud** (Job `sonar`)
   - Checkout do código
   - Setup do Java 23
   - Build com Maven
   - Execução de todos os testes
   - Análise de código no SonarCloud
   - Verificação de cobertura de testes

2. **Deploy to EKS** (Job `deploy`) - Apenas branch `main`
   - Build da imagem Docker
   - Push para Amazon ECR
   - Deploy no cluster EKS
   - Atualização dos recursos Kubernetes
   - Rollout forçado do deployment

### Configuração de Secrets

Para o pipeline funcionar, configure os seguintes secrets no GitHub:

```yaml
# SonarCloud
SONAR_TOKEN
SONAR_PROJECT_KEY
SONAR_ORGANIZATION

# AWS
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_SESSION_TOKEN
AWS_ACCOUNT_ID

# Database
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_DATASOURCE_URL
```

## 📊 SonarCloud - Qualidade de Código

O projeto está integrado com o **SonarCloud** para análise contínua de qualidade:

### Métricas Monitoradas

- **Cobertura de Código**: Mínimo de cobertura definido pelos testes
- **Code Smells**: Identificação de más práticas
- **Bugs**: Detecção de possíveis bugs
- **Vulnerabilidades**: Análise de segurança
- **Duplicação**: Código duplicado
- **Maintainability**: Índice de manutenibilidade

### Configuração do SonarCloud

O projeto está configurado para enviar métricas através do Maven:

```xml
<!-- JaCoCo para cobertura -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
</plugin>
```

```properties
# application.properties
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
```

### Visualizar Resultados

Acesse o dashboard do SonarCloud em:
- https://sonarcloud.io/dashboard?id=WeesleyAlves_FIAP-SOAT-MICROSERVICE-ORDERS

## 🐳 Deploy com Kubernetes

### Ambiente Local (Minikube/Kind)

```bash
# Aplicar configurações
kubectl apply -f k8s/local/namespace.yaml
kubectl apply -f k8s/local/secret.yaml
kubectl apply -f k8s/local/configmap.yaml

# Deploy do PostgreSQL
kubectl apply -f k8s/local/postgres-pvc.yaml
kubectl apply -f k8s/local/postgres-deployment.yaml
kubectl apply -f k8s/local/postgres-service.yaml

# Deploy da aplicação
kubectl apply -f k8s/local/orders-deployment.yaml
kubectl apply -f k8s/local/orders-service.yaml
```

Ou use o script PowerShell:

```powershell
.\k8s\local\deploy-local.ps1
```

### Ambiente AWS (EKS)

```bash
# Configure o kubeconfig
aws eks update-kubeconfig --region us-east-1 --name fiap-fastfood-eks

# Deploy
kubectl apply -f k8s/aws/namespace.yaml
kubectl apply -f k8s/aws/configmap.yaml
kubectl apply -f k8s/aws/orders-deployment.yaml
kubectl apply -f k8s/aws/orders-service.yaml
```

## 🔗 Microserviços Relacionados

Este microserviço se integra com:

- **MS-Products**: Consulta de informações de produtos
- **MS-Inventory**: Atualização de estoque
- **MS-Payment**: Criação e consulta de pagamentos

## 📝 Licença

Este projeto foi desenvolvido como parte do Tech Challenge da FIAP.

## 👥 Membros do projeto

- Diego de Salles — RM362702
- Lucas Felinto — RM363094
- Maickel Alves — RM361616
- Pedro Morgado — RM364209
- Wesley Alves — RM364342

