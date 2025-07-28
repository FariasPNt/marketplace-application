# Stocker-application
## ☁️ Backend - Sistema de Estoque na Nuvem
Este projeto é um backend moderno e escalável para gerenciamento de estoques. Desenvolvido com Java 21, integra-se a serviços da AWS para garantir distribuição de eventos, persistência externa e resiliência. Utiliza MongoDB como banco de dados NoSQL para flexibilidade no armazenamento.

### 🧪 Tecnologias

- **Java 21** – linguagem principal da aplicação
- **Spring Boot** – framework para desenvolvimento de APIs REST
- **MongoDB (NoSQL)** – banco de dados flexível e escalável
- **AWS SNS (Simple Notification Service)** – notificação de eventos
- **AWS SQS (Simple Queue Service)** – filas de mensagens assíncronas
- **AWS Lambda** – processamento serverless baseado em eventos
- **AWS S3 (Simple Storage Service)** – armazenamento estruturado em objetos JSON

### 🏗️ Arquitetura

| Etapa                      | Origem                     | Destino                      |
|---------------------------|----------------------------|------------------------------|
| Emissão de Evento         | Backend                    | SNS (catalog-emit)          |
| Distribuição de Eventos   | SNS                        | SQS (catalog-queue)         |
|                           | SNS                        | SQS (stock-queue)           |
| Processamento             | SQS (catalog-queue)        | Lambda (CatalogProcessor)   |
|                           | SQS (stock-queue)          | Lambda (StockProcessor)     |
| Armazenamento em Arquivo  | Lambda (CatalogProcessor)  | S3 (catalog-prefix/)        |
|                           | Lambda (StockProcessor)    | S3 (stock-prefix/)          |

### 🔗 Endpoints da API

| Método | Endpoint                  | Descrição                                    |
|--------|---------------------------|----------------------------------------------|
| POST   | /categories               | Cadastrar uma nova categoria                 |
| GET    | /categories               | Listar todas as categorias                   |
| PUT    | /categories/{id}          | Atualizar uma categoria por ID               |
| DELETE | /categories/{id}          | Deletar uma categoria por ID                 |
| POST   | /products                 | Cadastrar um novo produto                    |
| GET    | /products                 | Listar todos os produtos                     |
| PUT    | /products/{id}            | Atualizar um produto por ID                  |
| DELETE | /products/{id}            | Deletar um produto por ID                    |
| POST   | /stocks/init              | Criar estoque para um produto                |
| POST   | /stocks/entry             | Registrar entrada de produto em estoque      |
| POST   | /stocks/exit              | Registrar saída de produto em estoque        |

🚀 Como executar localmente
Clone o repositório:

```
https://github.com/FariasPNt/stock-application
```

Configure o MongoDB (local ou Atlas) no application.properties.

Configure as credenciais da AWS (via ~/.aws/credentials ou variáveis de ambiente).
Ou crie o arquivo .env e adicione ao Docker

### 📂 Organização do Projeto

A estrutura do projeto foi organizada de forma modular e de fácil manutenção:

```text
src/
├── config/        # Configurações gerais da aplicação (MongoDB, AWS, etc.)
├── controller/    # Camada responsável pelas requisições HTTP (REST Controllers)
├── domain/        # Entidades de domínio (modelos principais)
├── dto/           # Objetos de transferência de dados (Data Transfer Objects)
├── repository/    # Interfaces de acesso ao banco de dados (MongoRepository)
├── service/       # Lógica de negócio e regras da aplicação
````

> ⚙️ As funções AWS Lambda foram desenvolvidas separadamente em um repositório Node.js, com módulos independentes para:
> - 📁 **Catálogo** (`CatalogProcessor`)
> - 📁 **Estoque** (`StockProcessor`)


### 📈 Próximas melhorias
Autenticação com JWT

Dashboards de inventário com gráficos

Integração com DynamoDB como alternativa ao S3

### 🧑‍💻 Autor
Repositório mantido por Antonio Farias.
Contribuições são bem-vindas!
