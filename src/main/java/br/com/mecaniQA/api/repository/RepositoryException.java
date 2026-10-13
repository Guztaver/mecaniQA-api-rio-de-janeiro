package br.com.mecaniQA.api.repository;

public class RepositoryException extends RuntimeException {

	public RepositoryException(String operation, Throwable cause) {
		super("Falha ao " + operation + " no banco de dados.", cause);
	}
}
