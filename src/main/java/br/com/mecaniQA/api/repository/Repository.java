package br.com.mecaniQA.api.repository;

import java.util.List;

/** Contrato base para persistência manual via JDBC. */
public interface Repository<T, ID> {

	T save(T entity);

	List<T> findAll();

	T findById(ID id);

	T update(T entity);

	boolean deleteById(ID id);
}
