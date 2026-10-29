# mecaniQA API — Rio de Janeiro

API REST desenvolvida para a oficina mecânica fictícia **mecaniQA**, no contexto da disciplina de **Desenvolvimento Web Orientado a Objetos** na **Unex**.

O projeto gerencia ordens de serviço, serviços automotivos, catálogo e estoque de peças, além de pedidos de suprimento de peças com fluxo de status e rastreabilidade.

---

## 📋 Sumário

- [Visão Geral](#-visão-geral)
- [Decisões Técnicas de Arquitetura](#-decisões-técnicas-de-arquitetura)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Banco de Dados e Persistência](#-banco-de-dados-e-persistência)
- [Configuração e Execução](#-configuração-e-execução)
- [Endpoints da API](#-endpoints-da-api)
- [Execução dos Testes](#-execução-dos-testes)
- [Documentações Complementares](#-documentações-complementares)

---

## 🎯 Visão Geral

A **mecaniQA API** oferece serviços essenciais para a operação de uma oficina mecânica:
- **Gestão de Peças:** Cadastro, consulta por código/código de barras, precificação (custo e venda), controle de estoque e categorização.
- **Gestão de Serviços:** Cadastro e manutenção da tabela de serviços (nome, tempo estimado em minutos e valor cobrado).
- **Pedidos de Peças:** Criação e acompanhamento de requisições de compra/reposição de peças com múltiplos itens e controle de status (`ORCANDO`, `APROVADO`, `CANCELADO`, `ENTREGUE`).
- **Ordens de Serviço (OS):** Vinculação com veículos, clientes, lista de serviços prestados e eventual pedido de peças associado, com ciclo de vida (`ABERTA`, `EM_ANDAMENTO`, `FINALIZADA`, `CANCELADA`) e cálculo consolidado do valor total.

---

## 🏛 Decisões Técnicas de Arquitetura

Conforme as diretrizes pedagógicas e arquiteturais da OAT 3:

1. **Persistência com JDBC Puro (Proibição de ORM/JPA/Hibernate):**
   - **Decisão:** Todo acesso a dados é feito via JDBC padrão (`java.sql.Connection`, `PreparedStatement`, `ResultSet`), sem o uso de Spring Data JPA ou frameworks ORM como Hibernate.
   - **Motivação:** Proporcionar controle total sobre as consultas SQL, evitar sobrecarga de abstrações em tempo de execução e solidificar a compreensão dos fundamentos de conexão e transações no ecossistema Java.

2. **Segurança de Credenciais e Conexão via Variáveis de Ambiente:**
   - **Decisão:** As credenciais de banco não são mantidas em arquivos de configuração versionados (`application.properties` ou código-fonte).
   - **Implementação:** A classe `ConnectionFactory` (e `DatabaseConnection`) consome exclusivamente variáveis de ambiente do sistema operacional (`MECANIQA_DB_URL`, `MECANIQA_DB_USERNAME`, `MECANIQA_DB_PASSWORD`).
   - Em caso de indisponibilidade ou falha de credenciais, é lançada a exceção personalizada `DatabaseConnectionException` com mensagem clara e sem vazamento de segredos.

3. **Prevenção contra SQL Injection:**
   - **Decisão:** Todas as operações com parâmetros dinâmicos utilizam rigorosamente `PreparedStatement` com binds indexados (`?`), eliminando riscos de concatenação de strings e vulnerabilidade de SQL Injection.

4. **Padrão Repository Genérico e JdbcRepository Base:**
   - **Decisão:** Implementação de uma interface genérica `Repository<T, ID>` definindo o contrato CRUD (`save`, `findAll`, `findById`, `update`, `deleteById`).
   - Classes concretas (`PecaRepository`, `ServicoRepository`, `PedidoPecasRepository`, `OrdemServicoRepository`) herdam da classe abstrata de apoio ou implementam repositórios desacoplados da camada HTTP.

5. **Mapeamento Explícito de ResultSet (Mappers):**
   - **Decisão:** Cada entidade conta com mapeadores isolados (método `mapear` nos repositórios e classes dedicadas de `Mapper`).
   - **Benefício:** Centraliza e protege as regras de conversão de tipos SQL (`Timestamp`, `BigDecimal`, etc.) para objetos de domínio Java.

6. **Camada de Serviço e Validações de Domínio:**
   - **Decisão:** Toda a lógica de negócio, integridade de status e cálculo de valores totais reside nos serviços (`service/`), mantendo os controladores REST enxutos.

7. **Tratamento de Exceções sem Handlers Globais:**
   - **Decisão:** As exceções de negócio (`DadosInvalidosException`, `RecursoNaoEncontradoException`, `PecaNaoEncontradaException`) são tratadas pontualmente via blocos `try/catch` diretamente nos controladores.
   - **Retorno:** Mapeamento explícito para status HTTP adequados:
     - `400 Bad Request` para validações violadas;
     - `404 Not Found` para recursos inexistentes;
     - `500 Internal Server Error` para falhas inesperadas de banco.

8. **Transacionalidade Manual em Relacionamentos:**
   - **Decisão:** Operações compostas (como inserção de Pedido com seus itens, ou Ordem de Serviço com a lista de serviços associados) utilizam transações manuais com controle de `setAutoCommit(false)`, `commit()` e `rollback()` para assegurar a consistência dos dados.

---

## 🛠 Tecnologias Utilizadas

- **Linguagem:** Java 21 (LTS)
- **Framework Web:** Spring Boot 4.1.0 (`spring-boot-starter-webmvc`)
- **Acesso a Dados:** JDBC Puro (`com.mysql:mysql-connector-j`)
- **Banco de Dados:** MySQL 8+
- **Build Tool:** Gradle (Kotlin DSL)
- **Testes:** JUnit 5 / JUnit Platform

---

## 📂 Estrutura do Projeto

```text
mecaniQA-api-rio-de-janeiro/
├── docs/                        # Diagramas UML (PlantUML, PNG, SVG) e atas de OATs
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/mecaniQA/api/
│   │   │       ├── controller/  # Controladores REST com mapeamento HTTP e try/catch
│   │   │       ├── database/    # ConnectionFactory e DatabaseConnectionException
│   │   │       ├── exception/   # Exceções de negócio (DadosInvalidosException, etc.)
│   │   │       ├── mapper/      # Conversores entre entidades, DTOs e ResultSets
│   │   │       ├── model/       # Entidades e Enums de Domínio
│   │   │       ├── repository/  # Interface Repository e implementações com JDBC
│   │   │       ├── request/     # DTOs de entrada das requisições
│   │   │       ├── response/    # DTOs de resposta da API
│   │   │       ├── service/     # Regras de negócio, cálculos e validações
│   │   │       └── ApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/java/               # Bateria de testes unitários e de integração
├── build.gradle.kts             # Configurações do Gradle e dependências
├── gradlew / gradlew.bat         # Gradle Wrappers
└── script_mecaniqa.sql          # Script DDL/DML versionado para MySQL
```

---

## 🗄 Banco de Dados e Persistência

### Script DDL e Carga Inicial

O script `script_mecaniqa.sql` na raiz do projeto cria o schema e as tabelas com integridade referencial:

- `pecas`
- `servicos`
- `pedidos_pecas`
- `itens_pedido_pecas`
- `ordens_servico`
- `ordem_servico_servicos`

Para inicializar o banco em seu servidor MySQL 8+:

```bash
mysql -u root -p < script_mecaniqa.sql
```

---

## ⚙ Configuração e Execução

### 1. Variáveis de Ambiente Obrigatórias

Configure as variáveis de ambiente antes de executar a aplicação:

**No Linux/macOS:**
```bash
export MECANIQA_DB_URL='jdbc:mysql://localhost:3306/mecaniqa?useSSL=true&requireSSL=true&serverTimezone=UTC'
export MECANIQA_DB_USERNAME='mecaniqa_app'
export MECANIQA_DB_PASSWORD='sua_senha_aqui'
```

**No Windows (PowerShell):**
```powershell
$env:MECANIQA_DB_URL="jdbc:mysql://localhost:3306/mecaniqa?useSSL=false&serverTimezone=UTC"
$env:MECANIQA_DB_USERNAME="mecaniqa_app"
$env:MECANIQA_DB_PASSWORD="sua_senha_aqui"
```

**No Windows (CMD):**
```cmd
set MECANIQA_DB_URL=jdbc:mysql://localhost:3306/mecaniqa?useSSL=false&serverTimezone=UTC
set MECANIQA_DB_USERNAME=mecaniqa_app
set MECANIQA_DB_PASSWORD=sua_senha_aqui
```

### 2. Executando a Aplicação

Utilize o Gradle Wrapper:

```bash
./gradlew bootRun
```
Ou no Windows:
```powershell
.\gradlew.bat bootRun
```

A API estará acessível por padrão em `http://localhost:8080`.

---

## 🔌 Endpoints da API

### Peças (`/api/pecas` e `/pecas`)
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/pecas` | Cadastra uma nova peça |
| `GET` | `/api/pecas` | Lista todas as peças cadastradas |
| `GET` | `/api/pecas/{codigo}` | Busca os detalhes de uma peça por ID |
| `PUT` | `/api/pecas/{codigo}` | Atualiza os dados de uma peça existente |
| `DELETE` | `/api/pecas/{codigo}` | Remove uma peça do catálogo |

### Serviços (`/api/servicos`)
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/servicos` | Cadastra um novo serviço tabelado |
| `GET` | `/api/servicos` | Lista todos os serviços |
| `GET` | `/api/servicos/{codigo}` | Busca um serviço por código |
| `PUT` | `/api/servicos/{codigo}` | Atualiza valores e detalhes de um serviço |
| `DELETE` | `/api/servicos/{codigo}` | Remove um serviço |

### Pedidos de Peças (`/api/pedidos-pecas`)
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/pedidos-pecas` | Abre um novo pedido com lista de itens |
| `GET` | `/api/pedidos-pecas` | Lista os pedidos de peças |
| `GET` | `/api/pedidos-pecas/{codigo}` | Detalhes do pedido de peças e itens |
| `PATCH`| `/api/pedidos-pecas/{codigo}/status` | Atualiza o status do pedido (`ORCANDO`, `APROVADO`, `ENTREGUE`, etc.) |
| `POST` | `/api/pedidos-pecas/{codigo}/itens` | Adiciona novos itens a um pedido existente |

### Ordens de Serviço (`/api/ordens-servico`)
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/ordens-servico` | Cria uma nova Ordem de Serviço vinculando cliente, veículo e serviços |
| `GET` | `/api/ordens-servico` | Lista todas as ordens de serviço |
| `GET` | `/api/ordens-servico/{codigo}` | Detalhes completos da OS (incluindo serviços e pedidos) |
| `PATCH`| `/api/ordens-servico/{codigo}/status` | Atualiza o status da OS (`ABERTA`, `EM_ANDAMENTO`, `FINALIZADA`, etc.) |

---

## 🧪 Execução dos Testes

Para executar os testes unitários sem necessidade de conexão com o banco MySQL:

```bash
./gradlew test --tests "com.mecaniqa.*"
./gradlew test --tests "br.com.mecaniQA.api.model.*"
```

Para rodar todos os testes (incluindo os de integração com banco ativo):

```bash
./gradlew test
```

Os relatórios detalhados de execução são gerados em `build/reports/tests/test/index.html`.

---

## 📑 Documentações Complementares

- [Atas e Decisões da OAT 3](docs/oat3-persistindo-dados-2026-10-09.md)
- [Diagramas de Atividades e Classes](docs/)
- [Coleção Postman](docs/mecaniQA_api_oat1_rio_de_janeiro.postman_collection.json)
