# mecaniQA-api
Repositório da API da empresa fictícia mecaniQA na disciplina de Desenvolvimento Web Orientado a Objetos na Unex

## Banco de dados (OAT 3)

A API usa MySQL e JDBC puro; não utiliza JPA ou Hibernate. Execute
`script_mecaniqa.sql` em um servidor MySQL 8+ e, antes de iniciar a aplicação,
configure as credenciais fora do repositório:

```bash
export MECANIQA_DB_URL='jdbc:mysql://localhost:3306/mecaniqa?useSSL=true&requireSSL=true&serverTimezone=UTC'
export MECANIQA_DB_USERNAME='mecaniqa_app'
export MECANIQA_DB_PASSWORD='sua-senha'
```

O registro das decisões e do cenário de validação está em
`docs/oat3-persistindo-dados-2026-10-09.md`.
