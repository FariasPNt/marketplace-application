# Stocker-application
## ☁️ Backend - Sistema de Estoque na Nuvem
Este projeto é um backend moderno e escalável para gerenciamento de estoques. Desenvolvido com Java 21, integra-se a serviços da AWS para garantir distribuição de eventos, persistência externa e resiliência. Utiliza MongoDB como banco de dados NoSQL para flexibilidade no armazenamento.

### 📦 Visão Geral
A aplicação permite:

Cadastro de categorias.

Cadastro de produtos associados a uma categoria.

Criação e controle de estoques, com quantidade e localização (depósito).

Sincronização com a AWS para persistência de dados em arquivos e disparo de eventos.

### 🏗️ Arquitetura
Fluxo de Eventos na AWS

Backend --> SNS["SNS (catalog-emit)"]
SNS --> SQS_Catalog["SQS (catalog-queue)"]
SNS --> SQS_Stock["SQS (stock-queue)"]
SQS_Catalog --> Lambda_Catalog["Lambda (CatalogProcessor)"]
SQS_Stock --> Lambda_Stock["Lambda (StockProcessor)"]
Lambda_Catalog --> S3["S3 (catalog-prefix/)"]
Lambda_Stock --> S3["S3 (stock-prefix/)"]

### ☁️ Componentes da AWS
SNS (Simple Notification Service):
Serviço usado para publicação que recebe eventos da aplicação (como criação ou exclusão de produtos e categorias) e os distribui para filas diferentes de acordo com o tipo de evento.

SQS (Simple Queue Service):
Fila resiliente que armazena os eventos publicados pelo SNS. Permite desacoplamento e reprocessamento em caso de falhas.

AWS Lambda:
Funções serverless que são acionadas automaticamente quando há uma nova mensagem na fila. Responsáveis por processar os dados e atualizar o armazenamento S3 com os catálogos ou os estoques.

S3 (Simple Storage Service):
Armazena os arquivos JSON dos catálogos e dos estoques, organizados por prefixos (catalog/, stock/) com base no ownerId.

### 🧪 Tecnologias
Java 21

Spring Boot

MongoDB (NoSQL) – flexível e altamente escalável

AWS SNS – notificação de eventos

AWS SQS – filas de mensagens

AWS Lambda – processamento assíncrono

AWS S3 – armazenamento de dados estruturados

🚀 Como executar localmente
Clone o repositório:

```
git clone https://github.com/seu-usuario/sistema-estoque-cloud.git
```

Configure o MongoDB (local ou Atlas) no application.properties.

Configure as credenciais da AWS (via ~/.aws/credentials ou variáveis de ambiente).

### 📂 Organização do Projeto

src/
├── controller/
├── domain/
├── dto/
├── repository/
├── service/
├── config/
AWS Lambdas são separadas em um repositório Node.js com estrutura modular (catálogo e estoque).

### 📈 Próximas melhorias
Autenticação com JWT

Dashboards de inventário com gráficos

Integração com DynamoDB como alternativa ao S3

Monitoramento com CloudWatch

### 🧑‍💻 Autor
Repositório mantido por Antonio Farias.
Contribuições são bem-vindas!