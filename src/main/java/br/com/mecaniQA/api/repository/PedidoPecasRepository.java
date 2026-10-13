package br.com.mecaniQA.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import br.com.mecaniQA.api.model.ItemPedidoPecas;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.model.StatusPedidoPecas;

/** CRUD JDBC de pedidos e seus itens, com transação para manter consistência. */
public class PedidoPecasRepository extends JdbcRepository<PedidoPecas> {
	private static final PedidoPecasRepository INSTANCE = new PedidoPecasRepository();
	private final PecaRepository pecas = PecaRepository.getInstance();
	private PedidoPecasRepository() { }
	public static PedidoPecasRepository getInstance() { return INSTANCE; }

	@Override public PedidoPecas salvar(PedidoPecas pedido) {
		String sql = "INSERT INTO pedidos_pecas (descricao, status, valor_total) VALUES (?, ?, ?)";
		try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			c.setAutoCommit(false); preencher(ps, pedido); ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) pedido.setCodigo(keys.getLong(1)); }
			gravarItens(c, pedido); c.commit(); return buscarPorCodigo(pedido.getCodigo());
		} catch (SQLException e) { throw new RepositoryException("salvar pedido", e); }
	}
	@Override public List<PedidoPecas> listar() {
		List<PedidoPecas> lista = new ArrayList<>();
		try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement("SELECT * FROM pedidos_pecas ORDER BY codigo"); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) lista.add(mapear(c, rs)); return lista;
		} catch (SQLException e) { throw new RepositoryException("listar pedidos", e); }
	}
	@Override public PedidoPecas buscarPorCodigo(Long codigo) {
		try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement("SELECT * FROM pedidos_pecas WHERE codigo = ?")) {
			ps.setLong(1, codigo); try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(c, rs) : null; }
		} catch (SQLException e) { throw new RepositoryException("buscar pedido", e); }
	}
	@Override public PedidoPecas atualizar(PedidoPecas pedido) {
		String sql = "UPDATE pedidos_pecas SET descricao = ?, status = ?, valor_total = ? WHERE codigo = ?";
		try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
			c.setAutoCommit(false); preencher(ps, pedido); ps.setLong(4, pedido.getCodigo()); if (ps.executeUpdate() == 0) return null;
			try (PreparedStatement apagar = c.prepareStatement("DELETE FROM itens_pedido_pecas WHERE pedido_pecas_codigo = ?")) { apagar.setLong(1, pedido.getCodigo()); apagar.executeUpdate(); }
			gravarItens(c, pedido); c.commit(); return buscarPorCodigo(pedido.getCodigo());
		} catch (SQLException e) { throw new RepositoryException("atualizar pedido", e); }
	}
	@Override public boolean excluir(Long codigo) {
		try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement("DELETE FROM pedidos_pecas WHERE codigo = ?")) { ps.setLong(1, codigo); return ps.executeUpdate() > 0; } catch (SQLException e) { throw new RepositoryException("excluir pedido", e); }
	}
	public void reset() { try (Connection c = abrirConexao(); Statement s = c.createStatement()) { s.executeUpdate("DELETE FROM pedidos_pecas"); } catch (SQLException e) { throw new RepositoryException("limpar pedidos", e); } }
	private void preencher(PreparedStatement ps, PedidoPecas p) throws SQLException { p.recalcularValorTotal(); ps.setString(1, p.getDescricao()); ps.setString(2, p.getStatus().name()); ps.setBigDecimal(3, p.getValorTotal()); }
	private void gravarItens(Connection c, PedidoPecas pedido) throws SQLException {
		String sql = "INSERT INTO itens_pedido_pecas (pedido_pecas_codigo, peca_codigo, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { for (ItemPedidoPecas i : pedido.getItens()) { ps.setLong(1, pedido.getCodigo()); ps.setLong(2, i.getPeca().getCodigo()); ps.setInt(3, i.getQuantidade()); ps.setBigDecimal(4, i.getPrecoUnitario()); ps.executeUpdate(); try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) i.setCodigo(keys.getLong(1)); } } }
	}
	private PedidoPecas mapear(Connection c, ResultSet rs) throws SQLException {
		List<ItemPedidoPecas> itens = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT codigo, peca_codigo, quantidade, preco_unitario FROM itens_pedido_pecas WHERE pedido_pecas_codigo = ? ORDER BY codigo")) { ps.setLong(1, rs.getLong("codigo")); try (ResultSet irs = ps.executeQuery()) { while (irs.next()) { Peca peca = pecas.buscarPorCodigo(irs.getLong("peca_codigo")); itens.add(new ItemPedidoPecas(irs.getLong("codigo"), peca, irs.getInt("quantidade"), irs.getBigDecimal("preco_unitario"))); } } }
		Timestamp criacao = rs.getTimestamp("data_criacao"), atualizacao = rs.getTimestamp("data_atualizacao");
		return new PedidoPecas(rs.getLong("codigo"), rs.getString("descricao"), StatusPedidoPecas.valueOf(rs.getString("status")), itens, rs.getBigDecimal("valor_total"), criacao == null ? null : criacao.toLocalDateTime(), atualizacao == null ? null : atualizacao.toLocalDateTime());
	}
}
