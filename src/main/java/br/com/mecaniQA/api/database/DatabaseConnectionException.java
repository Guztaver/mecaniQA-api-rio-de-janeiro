package br.com.mecaniQA.api.database;

/** Exceção de infraestrutura sem expor URL, usuário ou senha do banco. */
public class DatabaseConnectionException extends RuntimeException {

	public DatabaseConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
}
