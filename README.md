# Ecommerce Café ☕

API REST de e-commerce especializado em café, desenvolvida como projeto final da disciplina **Tópicos 1** — UNITINS 2026.

## Tecnologias

| Tecnologia | Versão |
|-----------|--------|
| Java | 21 |
| Quarkus | 3.34.5 |
| Hibernate ORM + Panache | - |
| PostgreSQL | 18+ |
| H2 (testes) | - |
| SmallRye JWT | - |
| Jakarta Validation | - |

---

## Como rodar

### Pré-requisitos
- Java 21
- Maven 3.9+
- PostgreSQL instalado

### 1. Gerar as chaves JWT (obrigatório na primeira vez)

```bash
openssl genrsa -out privateKey.pem 2048
openssl rsa -pubout -in privateKey.pem -out publicKey.pem
```

### 2. Configurar o banco de dados

No pgAdmin (ou psql), crie o banco:

```sql
CREATE DATABASE cafe_db;
```

Em `src/main/resources/application.properties`, ajuste as credenciais:

```properties
quarkus.datasource.username=postgres
quarkus.datasource.password=sua_senha
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/cafe_db
```

### 3. Rodar em modo desenvolvimento

```bash
./mvnw quarkus:dev
```

A API sobe em: `http://localhost:8080`

Swagger UI: `http://localhost:8080/q/swagger-ui`

### 4. Rodar os testes

```bash
./mvnw test
```

> Os testes usam H2 em memória — não precisam de PostgreSQL.

---

## Dados iniciais (seed)

Ao subir o projeto pela primeira vez, o `DataInitializerService` popula o banco automaticamente com:

| Login | Senha | Perfil |
|-------|-------|--------|
| `admin` | `admin123` | ADMIN |
| `cliente` | `cliente123` | USER |

Além de 4 produtos de café, 2 fornecedores, categorias, torras, embalagens e estoque.

---

## Endpoints

### Autenticação
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/login` | Login — retorna token JWT |

### Senha
| Método | Endpoint | Acesso | Descrição |
|--------|----------|--------|-----------|
| PATCH | `/senha/esqueceu` | Público | Envia token de recuperação por email |
| PATCH | `/senha/redefinir` | Público | Redefine senha com o token recebido |
| PATCH | `/senha/alterar` | Logado | Altera senha informando a atual |

### E-commerce (público)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/ecommerce/produtos` | Lista produtos ativos com filtros |
| GET | `/ecommerce/produtos?nome=` | Filtra por nome |
| GET | `/ecommerce/produtos?fornecedor=` | Filtra por fornecedor/marca |
| GET | `/ecommerce/produtos?materialEmbalagem=` | Filtra por embalagem |
| GET | `/ecommerce/produtos/{id}` | Detalhe de um produto |

### Carrinho (cliente logado)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/carrinho` | Visualiza o carrinho |
| POST | `/carrinho/{produtoId}?quantidade=` | Adiciona item |
| PATCH | `/carrinho/{produtoId}?quantidade=` | Atualiza quantidade |
| DELETE | `/carrinho/{produtoId}` | Remove item |
| DELETE | `/carrinho` | Limpa o carrinho |
| POST | `/carrinho/checkout` | Finaliza compra (cria pedido + processa pagamento) |

### Checkout — body esperado
```json
{
  "enderecoId": 1,
  "formaPagamento": "PIX",
  "parcelas": null
}
```
Formas de pagamento: `PIX` · `CARTAO_CREDITO` · `CARTAO_DEBITO` · `BOLETO`

### Pedidos (cliente logado)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/pedidos` | Histórico de pedidos |
| GET | `/pedidos/{id}` | Detalhe do pedido |

### Lista de Desejos (cliente logado)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/lista-desejos` | Visualiza lista |
| POST | `/lista-desejos/{produtoId}` | Adiciona produto |
| DELETE | `/lista-desejos/{produtoId}` | Remove produto |

### Usuário
| Método | Endpoint | Acesso | Descrição |
|--------|----------|--------|-----------|
| POST | `/usuarios/cadastro/simples` | Público | Cadastro com login e senha |
| POST | `/usuarios/cadastro/completo` | Público | Cadastro com dados pessoais e endereço |
| PATCH | `/usuarios/meu-perfil` | Logado | Edita nome, email e telefone |
| GET | `/usuarios/meus-enderecos` | Logado | Lista endereços salvos |
| POST | `/usuarios/meus-enderecos` | Logado | Adiciona endereço |
| PUT | `/usuarios/meus-enderecos/{id}` | Logado | Atualiza endereço |
| DELETE | `/usuarios/meus-enderecos/{id}` | Logado | Remove endereço |

### CRUDs Administrativos (requer perfil ADMIN)
| Endpoint base | Entidade |
|--------------|---------|
| `/produtos` | Produtos |
| `/categorias` | Categorias |
| `/torras` | Torras |
| `/fornecedores` | Fornecedores |
| `/materiais-embalagem` | Materiais de embalagem |
| `/tamanhos-embalagem` | Tamanhos de embalagem |
| `/lotes-estoque` | Controle de estoque |
| `/usuarios` | Usuários |
| `/pedidos/{id}/pagamento` | Pagamento avulso |

---

## Fluxo do cliente

```
1. Navegar produtos      GET  /ecommerce/produtos
2. Criar conta           POST /usuarios/cadastro/completo
3. Login                 POST /auth/login
4. Adicionar endereço    POST /usuarios/meus-enderecos
5. Montar carrinho       POST /carrinho/{id}?quantidade=2
6. Fazer checkout        POST /carrinho/checkout
                              { enderecoId, formaPagamento, parcelas }
7. Acompanhar pedido     GET  /pedidos
```

---

## Arquitetura

```
model → dto → mapper → repository → service → resource → exception
```

- **model** — Entidades JPA com todos os relacionamentos OOP
- **dto** — Records Java para request/response
- **mapper** — Conversão manual model ↔ DTO
- **repository** — PanacheRepository<T>
- **service** — Regras de negócio
- **resource** — Endpoints REST
- **exception** — Tratamento padronizado RFC 7807

---

## Testes

73 testes de integração com `@QuarkusTest` + H2 em memória.

```bash
./mvnw test
```

---

## Autor

Desenvolvido por **Gustavo** — UNITINS, Tópicos 1, 2026.
