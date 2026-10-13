package br.com.mecaniQA.api.repository;

import java.sql.Connection;

import br.com.mecaniQA.api.database.ConnectionFactory;

/** Base compartilhada dos repositórios JDBC, sem ORM. */
public abstract class JdbcRepository<T> implements Repository<T, Long> {

	protected Connection abrirConexao() {
		return ConnectionFactory.getConnection();
	}

	@Override
	public T save(T entity) {
		return salvar(entity);
	}

	@Override
	public java.util.List<T> findAll() {
		return listar();
	}

	@Override
	public T findById(Long codigo) {
		return buscarPorCodigo(codigo);
	}

	@Override
	public T update(T entity) {
		return atualizar(entity);
	}

	@Override
	public boolean deleteById(Long codigo) {
		return excluir(codigo);
	}

	public abstract T salvar(T entity);

	public abstract java.util.List<T> listar();

	public abstract T buscarPorCodigo(Long codigo);

	public abstract T atualizar(T entity);

	public abstract boolean excluir(Long codigo);
}
