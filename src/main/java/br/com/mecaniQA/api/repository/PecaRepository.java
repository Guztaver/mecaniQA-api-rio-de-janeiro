package br.com.mecaniQA.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.mecaniQA.api.model.CategoriaPeca;
import br.com.mecaniQA.api.model.Peca;

/** Persistência de peças com SQL escrito manualmente (JDBC puro). */
public class PecaRepository extends JdbcRepository<Peca> {

	private static final PecaRepository INSTANCE = new PecaRepository();
	private static final String CAMPOS = "codigo, codigo_barras, fornecedor, quantidade_estoque, preco_custo, "
			+ "preco_venda, tamanho, cor, categoria, data_cadastro, data_atualizacao";

	private PecaRepository() {
	}

	public static PecaRepository getInstance() {
		return INSTANCE;
	}

	@Override
	public Peca salvar(Peca peca) {
		String sql = "INSERT INTO pecas (codigo_barras, fornecedor, quantidade_estoque, preco_custo, preco_venda, tamanho, cor, categoria) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = abrirConexao();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preencher(statement, peca);
			statement.executeUpdate();
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (keys.next()) {
					peca.setCodigo(keys.getLong(1));
				}
			}
			return buscarPorCodigo(peca.getCodigo());
		} catch (SQLException exception) {
			throw new RepositoryException("salvar peça", exception);
		}
	}

	@Override
	public List<Peca> listar() {
		List<Peca> pecas = new ArrayList<>();
		String sql = "SELECT " + CAMPOS + " FROM pecas ORDER BY codigo";
		try (Connection connection = abrirConexao(); PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				pecas.add(mapear(resultSet));
			}
			return pecas;
		} catch (SQLException exception) {
			throw new RepositoryException("listar peças", exception);
		}
	}

	@Override
	public Peca buscarPorCodigo(Long codigo) {
		String sql = "SELECT " + CAMPOS + " FROM pecas WHERE codigo = ?";
		try (Connection connection = abrirConexao(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, codigo);
			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next() ? mapear(resultSet) : null;
			}
		} catch (SQLException exception) {
			throw new RepositoryException("buscar peça", exception);
		}
	}

	@Override
	public Peca atualizar(Peca peca) {
		String sql = "UPDATE pecas SET codigo_barras = ?, fornecedor = ?, quantidade_estoque = ?, preco_custo = ?, "
				+ "preco_venda = ?, tamanho = ?, cor = ?, categoria = ? WHERE codigo = ?";
		try (Connection connection = abrirConexao(); PreparedStatement statement = connection.prepareStatement(sql)) {
			preencher(statement, peca);
			statement.setLong(9, peca.getCodigo());
			return statement.executeUpdate() == 0 ? null : buscarPorCodigo(peca.getCodigo());
		} catch (SQLException exception) {
			throw new RepositoryException("atualizar peça", exception);
		}
	}

	@Override
	public boolean excluir(Long codigo) {
		try (Connection connection = abrirConexao();
				PreparedStatement statement = connection.prepareStatement("DELETE FROM pecas WHERE codigo = ?")) {
			statement.setLong(1, codigo);
			return statement.executeUpdate() > 0;
		} catch (SQLException exception) {
			throw new RepositoryException("excluir peça", exception);
		}
	}

	private void preencher(PreparedStatement statement, Peca peca) throws SQLException {
		statement.setString(1, peca.getCodigoBarras());
		statement.setString(2, peca.getFornecedor());
		statement.setInt(3, peca.getQuantidadeEstoque() == null ? 0 : peca.getQuantidadeEstoque());
		statement.setBigDecimal(4, peca.getPrecoCusto());
		statement.setBigDecimal(5, peca.getPrecoVenda());
		statement.setString(6, peca.getTamanho());
		statement.setString(7, peca.getCor());
		statement.setString(8, peca.getCategoria() == null ? null : peca.getCategoria().name());
	}

	private Peca mapear(ResultSet resultSet) throws SQLException {
		Timestamp cadastro = resultSet.getTimestamp("data_cadastro");
		Timestamp atualizacao = resultSet.getTimestamp("data_atualizacao");
		String categoria = resultSet.getString("categoria");
		return new Peca(resultSet.getLong("codigo"), resultSet.getString("codigo_barras"),
				resultSet.getString("fornecedor"), resultSet.getInt("quantidade_estoque"),
				resultSet.getBigDecimal("preco_custo"), resultSet.getBigDecimal("preco_venda"),
				cadastro == null ? null : cadastro.toLocalDateTime(),
				atualizacao == null ? null : atualizacao.toLocalDateTime(), resultSet.getString("tamanho"),
				resultSet.getString("cor"), categoria == null ? null : CategoriaPeca.valueOf(categoria));
	}
}
