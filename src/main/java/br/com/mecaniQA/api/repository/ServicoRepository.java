package br.com.mecaniQA.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.mecaniQA.api.model.Servico;

/** Exemplo completo de CRUD manual sobre MySQL, sem JPA/Hibernate. */
public class ServicoRepository extends JdbcRepository<Servico> {

	private static final ServicoRepository INSTANCE = new ServicoRepository();

	private ServicoRepository() {
	}

	public static ServicoRepository getInstance() {
		return INSTANCE;
	}

	@Override
	public Servico salvar(Servico servico) {
		String sql = "INSERT INTO servicos (nome, tempo_estimado_minutos, custo_tabelado) VALUES (?, ?, ?)";
		try (Connection connection = abrirConexao();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preencher(statement, servico);
			statement.executeUpdate();
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (keys.next()) {
					servico.setCodigo(keys.getLong(1));
				}
			}
			return buscarPorCodigo(servico.getCodigo());
		} catch (SQLException exception) {
			throw new RepositoryException("salvar serviço", exception);
		}
	}

	@Override
	public List<Servico> listar() {
		List<Servico> servicos = new ArrayList<>();
		try (Connection connection = abrirConexao();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM servicos ORDER BY codigo");
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				servicos.add(mapear(resultSet));
			}
			return servicos;
		} catch (SQLException exception) {
			throw new RepositoryException("listar serviços", exception);
		}
	}

	@Override
	public Servico buscarPorCodigo(Long codigo) {
		try (Connection connection = abrirConexao();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM servicos WHERE codigo = ?")) {
			statement.setLong(1, codigo);
			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next() ? mapear(resultSet) : null;
			}
		} catch (SQLException exception) {
			throw new RepositoryException("buscar serviço", exception);
		}
	}

	@Override
	public Servico atualizar(Servico servico) {
		String sql = "UPDATE servicos SET nome = ?, tempo_estimado_minutos = ?, custo_tabelado = ? WHERE codigo = ?";
		try (Connection connection = abrirConexao(); PreparedStatement statement = connection.prepareStatement(sql)) {
			preencher(statement, servico);
			statement.setLong(4, servico.getCodigo());
			return statement.executeUpdate() == 0 ? null : buscarPorCodigo(servico.getCodigo());
		} catch (SQLException exception) {
			throw new RepositoryException("atualizar serviço", exception);
		}
	}

	@Override
	public boolean excluir(Long codigo) {
		try (Connection connection = abrirConexao();
				PreparedStatement statement = connection.prepareStatement("DELETE FROM servicos WHERE codigo = ?")) {
			statement.setLong(1, codigo);
			return statement.executeUpdate() > 0;
		} catch (SQLException exception) {
			throw new RepositoryException("excluir serviço", exception);
		}
	}

	private void preencher(PreparedStatement statement, Servico servico) throws SQLException {
		statement.setString(1, servico.getNome());
		statement.setInt(2, servico.getTempoEstimadoMinutos());
		statement.setBigDecimal(3, servico.getCustoTabelado());
	}

	private Servico mapear(ResultSet resultSet) throws SQLException {
		Timestamp criacao = resultSet.getTimestamp("data_criacao");
		Timestamp atualizacao = resultSet.getTimestamp("data_atualizacao");
		return new Servico(resultSet.getLong("codigo"), resultSet.getString("nome"),
				resultSet.getInt("tempo_estimado_minutos"), resultSet.getBigDecimal("custo_tabelado"),
				criacao == null ? null : criacao.toLocalDateTime(),
				atualizacao == null ? null : atualizacao.toLocalDateTime());
	}
}
