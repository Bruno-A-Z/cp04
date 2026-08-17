# Amazin — Mercado Express API

API REST desenvolvida em **Spring Boot** para gestão de itens de um mercado express (ex: produtos de limpeza, alimentos, utilidades), com persistência em banco de dados **Oracle**, uso obrigatório do **Lombok** e retorno de dados no padrão **HATEOAS (nível de maturidade 3 de Richardson)**.

## Integrantes

| Nome | RM |
|---|---|
| Bruno A Zanateli | RM563736 |
| Christian S Freitas | RM566098 |
| Rodrigo Tiezzi | RM562975 |
| Pedro P Biasolli| RM562521 |
| Matheus Souza | RM562532 |

## Tecnologias utilizadas

- Java 21
- Spring Boot 4.1.0 (Maven)
- Spring Data JPA / Hibernate
- Oracle Database (ORACLE_FIAP)
- Lombok
- Spring HATEOAS
- Springdoc OpenAPI (Swagger) 3.1.0
- Postman (testes de endpoint)

**IDE utilizada:** IntelliJ IDEA

## Estrutura do projeto

```
com.java.cp04.Amazin
 ├── controller
 │    └── ItemController.java
 ├── model
 │    ├── Item.java
 │    └── enumerator/Size.java
 ├── repository
 │    └── ItemRepository.java
 ├── service
 │    └── ItemService.java
 ├── assembler
 │    └── ItemModelAssembler.java
 └── Cp04Application.java
```

## Modelo de dados

Tabela `TB_ITEM` no banco Oracle:

| Coluna | Campo na entidade | Tipo |
|---|---|---|
| ID | id | Long (auto gerado) |
| NAME_ITEN | name | String |
| KIND_ITEM | kind | String |
| ITEM_SECTOR | sector | String |
| ITEM_SIZE | size | Enum (Size) |
| ITEM_PRICE | price | Double |

## Configuração e execução

1. Clone o repositório.
2. Configure as credenciais do Oracle ou banco de dados de sua preferencia em `src/main/resources/application.properties`:

```properties
server.port=8082
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
spring.datasource.username=XXXXXX
spring.datasource.password=XXXXXX
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.jpa.hibernate.ddl-auto=update
```

3. Rode a aplicação:

```bash
mvn spring-boot:run
```

4. A API sobe em `http://localhost:8082`.
5. Documentação Swagger disponível em `http://localhost:8082/swagger-ui/index.html`.

## Endpoints (CRUD completo)

Base URL: `http://localhost:8082/item`

### 1. Criar item — `POST /item`

**Request body:**
```json
{
  "name": "Pasta",
  "kind": "Food",
  "sector": "KITCHEN",
  "size": "BIG",
  "price": 19.5
}
```

**Response — `201 Created`:**
```json
{
  "id": 1,
  "name": "Pasta",
  "kind": "Food",
  "sector": "KITCHEN",
  "size": "BIG",
  "price": 19.5,
  "_links": {
    "self": { "href": "http://localhost:8082/item/1" },
    "itens": { "href": "http://localhost:8082/item" },
    "deletar": { "href": "http://localhost:8082/item/1" }
  }
}
```

<img width="848" height="665" alt="{23F6EB6C-7767-4DCA-B131-509DF66F1F76}" src="https://github.com/user-attachments/assets/169ec08e-25b4-43bc-8091-1765daf0aead" />


---

### 2. Listar todos os itens — `GET /item`

**Response — `200 OK`:**
```json
{
  "_embedded": {
    "itemList": [
      {
        "id": 1,
        "name": "Pasta",
        "kind": "Food",
        "sector": "KITCHEN",
        "size": "BIG",
        "price": 19.5,
        "_links": { "...": "..." }
      }
    ]
  },
  "_links": {
    "self": { "href": "http://localhost:8082/item" }
  }
}
```

<img width="844" height="664" alt="{6CF3C60B-CD24-4EBE-AF07-2F09EE4A5CC3}" src="https://github.com/user-attachments/assets/a7ee0037-ea2b-40a9-841c-63532371e0dc" />


---

### 3. Buscar item por ID — `GET /item/{id}`

**Response — `200 OK`:**
```json
{
  "id": 1,
  "name": "Pasta",
  "kind": "Rare",
  "sector": "KITCHEN",
  "size": "BIG",
  "price": 19.5,
  "_links": {
    "self": { "href": "http://localhost:8082/item/1" },
    "itens": { "href": "http://localhost:8082/item" },
    "deletar": { "href": "http://localhost:8082/item/1" }
  }
}
```

<img width="848" height="484" alt="{EDE449F8-B205-42CA-ACAD-644DF5CB6B51}" src="https://github.com/user-attachments/assets/e07d3564-313a-4027-bcd4-18b107fa4b3e" />


---

### 4. Atualizar item por completo — `PUT /item/{id}`

**Request body:**
```json
{
  "name": "Pasta Integral",
  "kind": "Rare",
  "sector": "KITCHEN",
  "size": "MEDIUM",
  "price": 22.9
}
```

**Response — `200 OK`:** item com todos os campos substituídos pelos novos valores.

<img width="843" height="649" alt="{C0F7F7F1-8FEF-4F10-940D-789319E7E9B7}" src="https://github.com/user-attachments/assets/96cc5aae-dd4f-44d3-968c-bf85e60f3114" />


---

### 5. Atualizar item parcialmente — `PATCH /item/{id}`

**Request body:**
```json
{
  "price": 24.9
}
```

**Response — `200 OK`:** apenas o campo `price` é alterado, os demais permanecem inalterados.

<img width="435" height="582" alt="{314D323F-F5B8-4DC1-9B2A-05287402A265}" src="https://github.com/user-attachments/assets/b7cbef76-0e8b-4073-b6a3-f677e9f128e8" />


---

### 6. Deletar item — `DELETE /item/{id}`

**Response — `204 No Content`**

Após a exclusão, uma nova busca por `GET /item/{id}` com o mesmo id não retorna mais o item.

<img width="653" height="248" alt="{F3E82D89-119E-4BE9-8CAB-8E35D0B2A3D3}" src="https://github.com/user-attachments/assets/7be0c024-51bc-461d-9d06-f219b2644b33" />


## HATEOAS

Todas as respostas retornam links de navegação (`_links`) seguindo o padrão HAL, permitindo que o cliente descubra as próximas ações possíveis (buscar, listar, deletar) sem precisar conhecer a estrutura fixa da API — atendendo ao nível de maturidade 3 de Richardson.

## Deploy

Link da aplicação em produção: _(inserir link do deploy aqui)_

## Configuração do Spring Initializr

> _(inserir print da configuração do Spring Initializr aqui)_
