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

## Cenário de teste do QA

1. Execute `script_mecaniqa.sql` em um MySQL 8+.
2. Exporte as três variáveis de ambiente e execute a aplicação; uma consulta de repositório deve abrir a conexão e retornar dados.
3. Altere temporariamente `MECANIQA_DB_PASSWORD` para uma senha inválida. A operação deve falhar com: `Não foi possível conectar ao MySQL. Verifique MECANIQA_DB_URL, MECANIQA_DB_USERNAME e MECANIQA_DB_PASSWORD.`

## Entrega

- Script DDL/DML: `script_mecaniqa.sql`.
- Infraestrutura: `database/ConnectionFactory` e `DatabaseConnectionException`.
- Repositórios: CRUD manual JDBC, contrato genérico e mapeamento de resultados, sem ORM.
