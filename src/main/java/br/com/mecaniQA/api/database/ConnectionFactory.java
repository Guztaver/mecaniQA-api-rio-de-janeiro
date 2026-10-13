package br.com.mecaniQA.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Ponto único de criação de conexões JDBC. As credenciais são lidas somente de
 * variáveis de ambiente, nunca do código-fonte ou do arquivo versionado.
 */
public final class ConnectionFactory {

	private static final String DEFAULT_URL =
			"jdbc:mysql://localhost:3306/mecaniqa?useSSL=true&requireSSL=true&serverTimezone=UTC";

	private ConnectionFactory() {
	}

	public static Connection getConnection() {
		String url = value("MECANIQA_DB_URL", DEFAULT_URL);
		String username = value("MECANIQA_DB_USERNAME", "mecaniqa_app");
		String password = value("MECANIQA_DB_PASSWORD", "");

		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException exception) {
			throw new DatabaseConnectionException(
					"Não foi possível conectar ao MySQL. Verifique MECANIQA_DB_URL, "
							+ "MECANIQA_DB_USERNAME e MECANIQA_DB_PASSWORD.",
					exception);
		}
	}

	private static String value(String name, String defaultValue) {
		String value = System.getenv(name);
		return value == null || value.isBlank() ? defaultValue : value;
	}
}
