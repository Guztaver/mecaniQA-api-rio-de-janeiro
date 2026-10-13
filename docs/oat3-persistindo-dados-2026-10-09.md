# OAT 3 — Persistindo os meus Dados

**Data:** 09/10/2026  
**Piloto(a):** _a preencher_  
**Copiloto(a):** _a preencher_  
**QA:** _a preencher_  
**Arquiteto(a):** _a preencher_  
**Scrum Master:** _a preencher_

## Fatos extraídos

| Pergunta | Resposta registrada |
| --- | --- |
| Banco de dados oficial e ORM | O banco oficial é **MySQL**. A persistência usa **JDBC puro**; JPA, Hibernate e qualquer outro ORM são proibidos. |
| Versionamento do banco | O projeto deve versionar o script SQL com DDL e DML junto do código-fonte. O arquivo é `script_mecaniqa.sql`, na raiz. |

## Decisões técnicas

- `ConnectionFactory` centraliza a conexão JDBC e recebe `MECANIQA_DB_URL`, `MECANIQA_DB_USERNAME` e `MECANIQA_DB_PASSWORD` pelo ambiente. Senhas não entram no Git.
- Falhas de autenticação ou URL lançam `DatabaseConnectionException` com mensagem objetiva e sem revelar segredo.
- A interface genérica `Repository<T, ID>` padroniza `save`, `findAll`, `findById`, `update` e `deleteById`. `JdbcRepository<T>` fornece a abertura de conexão e mantém os nomes em português usados pela API anterior.
- `PecaRepository`, `ServicoRepository`, `PedidoPecasRepository` e `OrdemServicoRepository` executam `INSERT`, `SELECT`, `UPDATE` e `DELETE` manualmente. Os dois últimos usam transações para gravar as relações de itens e serviços.
- Os métodos `mapear` de cada repositório são os mappers de `ResultSet`: isolam a conversão das linhas SQL para os modelos Java.
- Consultas e comandos usam `PreparedStatement`, evitando concatenação de parâmetros e injeção SQL.
- Os controladores somente recebem e devolvem HTTP. As validações, cálculos e chamadas aos repositórios ficam em `service/`; as exceções `DadosInvalidosException` e `RecursoNaoEncontradoException` são tratadas diretamente por `try/catch` nos controladores, sem `@ExceptionHandler` global.

## Brainstorm investigativo

### A conexão

**Pergunta:** Como implementar uma conexão segura com o banco de dados?

**Resposta/ideia definida:** Centralizar a criação de conexões em `ConnectionFactory`, usando `DriverManager` do JDBC. A URL, o usuário e a senha vêm das variáveis de ambiente `MECANIQA_DB_URL`, `MECANIQA_DB_USERNAME` e `MECANIQA_DB_PASSWORD`; portanto, não são gravados no código nem enviados ao Git. A URL padrão exige SSL/TLS. Caso a conexão falhe, a aplicação lança `DatabaseConnectionException` com orientação objetiva, sem expor a senha ou a URL completa.

### Padrão de repositório

**Pergunta:** Como adaptar os repositórios para a nova interface genérica baseada em JDBC?

**Resposta/ideia definida:** Criar `Repository<T, ID>` com o contrato único `save`, `findAll`, `findById`, `update` e `deleteById`. A classe abstrata `JdbcRepository<T>` implementa esse contrato e concentra a abertura da conexão. `PecaRepository`, `ServicoRepository`, `PedidoPecasRepository` e `OrdemServicoRepository` herdam essa base e escrevem suas próprias consultas SQL com `PreparedStatement`.

### Mapeamento e responsabilidade

**Ideia definida:** Cada repositório possui um método `mapear` que transforma o `ResultSet` em objetos do domínio. Os controllers não acessam JDBC: eles delegam a validação, as regras de negócio e a persistência às classes da pasta `service/`.

## Cenário de teste do QA

1. Execute `script_mecaniqa.sql` em um MySQL 8+.
2. Exporte as três variáveis de ambiente e execute a aplicação; uma consulta de repositório deve abrir a conexão e retornar dados.
3. Cadastre e consulte uma peça ou serviço. O dado deve permanecer disponível em uma nova requisição, pois foi salvo no MySQL.
4. Altere temporariamente `MECANIQA_DB_PASSWORD` para uma senha inválida. A operação deve falhar com: `Não foi possível conectar ao MySQL. Verifique MECANIQA_DB_URL, MECANIQA_DB_USERNAME e MECANIQA_DB_PASSWORD.`
5. Envie dados inválidos, como serviço sem nome ou pedido sem itens. O controller deve responder manualmente com HTTP 400, a partir de `DadosInvalidosException`, sem usar tratamento global.

## Entrega

### Artefatos

- Script DDL/DML versionado: `script_mecaniqa.sql`, na raiz do projeto.
- Registro da reunião e respostas da OAT: este documento.

### Código

- Infraestrutura de conexão: `database/ConnectionFactory` e `DatabaseConnectionException`.
- Repositórios: CRUD manual JDBC, contrato genérico e mapeamento de resultados, sem ORM.
- Serviços: validações, regras de negócio e comunicação com repositórios.
- Controllers: apenas entrada/saída HTTP e tratamento manual das exceções.
