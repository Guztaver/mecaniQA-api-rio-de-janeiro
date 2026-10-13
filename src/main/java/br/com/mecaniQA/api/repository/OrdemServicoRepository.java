package br.com.mecaniQA.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.model.StatusOrdemServico;

/** Persistência JDBC de ordens e da relação ordem-serviços. */
public class OrdemServicoRepository extends JdbcRepository<OrdemServico> {
	private static final OrdemServicoRepository INSTANCE = new OrdemServicoRepository();
	private final ServicoRepository servicos = ServicoRepository.getInstance();
	private final PedidoPecasRepository pedidos = PedidoPecasRepository.getInstance();
	private OrdemServicoRepository() { }
	public static OrdemServicoRepository getInstance() { return INSTANCE; }
	@Override public OrdemServico salvar(OrdemServico ordem) {
		String sql = "INSERT INTO ordens_servico (descricao, cliente, veiculo, status, pedido_pecas_codigo, valor_total, data_abertura, data_atualizacao, data_finalizacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { c.setAutoCommit(false); preencher(ps, ordem); ps.executeUpdate(); try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) ordem.setCodigo(k.getLong(1)); } gravarServicos(c, ordem); c.commit(); return buscarPorCodigo(ordem.getCodigo()); } catch (SQLException e) { throw new RepositoryException("salvar ordem de serviço", e); }
	}
	@Override public List<OrdemServico> listar() {
		List<OrdemServico> lista = new ArrayList<>(); try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement("SELECT * FROM ordens_servico ORDER BY codigo"); ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(mapear(c, rs)); return lista; } catch (SQLException e) { throw new RepositoryException("listar ordens de serviço", e); }
	}
	@Override public OrdemServico buscarPorCodigo(Long codigo) {
		try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement("SELECT * FROM ordens_servico WHERE codigo = ?")) { ps.setLong(1, codigo); try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(c, rs) : null; } } catch (SQLException e) { throw new RepositoryException("buscar ordem de serviço", e); }
	}
	@Override public OrdemServico atualizar(OrdemServico ordem) {
		String sql = "UPDATE ordens_servico SET descricao = ?, cliente = ?, veiculo = ?, status = ?, pedido_pecas_codigo = ?, valor_total = ?, data_abertura = ?, data_atualizacao = ?, data_finalizacao = ? WHERE codigo = ?";
		try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement(sql)) { c.setAutoCommit(false); preencher(ps, ordem); ps.setLong(10, ordem.getCodigo()); if (ps.executeUpdate() == 0) return null; try (PreparedStatement apagar = c.prepareStatement("DELETE FROM ordem_servico_servicos WHERE ordem_servico_codigo = ?")) { apagar.setLong(1, ordem.getCodigo()); apagar.executeUpdate(); } gravarServicos(c, ordem); c.commit(); return buscarPorCodigo(ordem.getCodigo()); } catch (SQLException e) { throw new RepositoryException("atualizar ordem de serviço", e); }
	}
	@Override public boolean excluir(Long codigo) { try (Connection c = abrirConexao(); PreparedStatement ps = c.prepareStatement("DELETE FROM ordens_servico WHERE codigo = ?")) { ps.setLong(1, codigo); return ps.executeUpdate() > 0; } catch (SQLException e) { throw new RepositoryException("excluir ordem de serviço", e); } }
	public void reset() { try (Connection c = abrirConexao(); Statement s = c.createStatement()) { s.executeUpdate("DELETE FROM ordens_servico"); } catch (SQLException e) { throw new RepositoryException("limpar ordens de serviço", e); } }
	private void preencher(PreparedStatement ps, OrdemServico o) throws SQLException { o.recalcularValorTotal(); ps.setString(1, o.getDescricao()); ps.setString(2, o.getCliente()); ps.setString(3, o.getVeiculo()); ps.setString(4, o.getStatus().name()); if (o.getPedidoPecas() == null) ps.setNull(5, java.sql.Types.BIGINT); else ps.setLong(5, o.getPedidoPecas().getCodigo()); ps.setBigDecimal(6, o.getValorTotal()); ps.setTimestamp(7, Timestamp.valueOf(o.getDataAbertura())); ps.setTimestamp(8, Timestamp.valueOf(o.getDataAtualizacao())); if (o.getDataFinalizacao() == null) ps.setNull(9, java.sql.Types.TIMESTAMP); else ps.setTimestamp(9, Timestamp.valueOf(o.getDataFinalizacao())); }
	private void gravarServicos(Connection c, OrdemServico ordem) throws SQLException { try (PreparedStatement ps = c.prepareStatement("INSERT INTO ordem_servico_servicos (ordem_servico_codigo, servico_codigo) VALUES (?, ?)")) { for (Servico s : ordem.getServicos()) { ps.setLong(1, ordem.getCodigo()); ps.setLong(2, s.getCodigo()); ps.addBatch(); } ps.executeBatch(); } }
	private OrdemServico mapear(Connection c, ResultSet rs) throws SQLException {
		List<Servico> lista = new ArrayList<>(); try (PreparedStatement ps = c.prepareStatement("SELECT servico_codigo FROM ordem_servico_servicos WHERE ordem_servico_codigo = ?")) { ps.setLong(1, rs.getLong("codigo")); try (ResultSet srs = ps.executeQuery()) { while (srs.next()) lista.add(servicos.buscarPorCodigo(srs.getLong(1))); } }
		long pedidoCodigo = rs.getLong("pedido_pecas_codigo"); PedidoPecas pedido = rs.wasNull() ? null : pedidos.buscarPorCodigo(pedidoCodigo); Timestamp abertura = rs.getTimestamp("data_abertura"), atualizacao = rs.getTimestamp("data_atualizacao"), finalizacao = rs.getTimestamp("data_finalizacao");
		return OrdemServico.builder().codigo(rs.getLong("codigo")).descricao(rs.getString("descricao")).cliente(rs.getString("cliente")).veiculo(rs.getString("veiculo")).status(StatusOrdemServico.valueOf(rs.getString("status"))).servicos(lista).pedidoPecas(pedido).valorTotal(rs.getBigDecimal("valor_total")).dataAbertura(abertura == null ? null : abertura.toLocalDateTime()).dataAtualizacao(atualizacao == null ? null : atualizacao.toLocalDateTime()).dataFinalizacao(finalizacao == null ? null : finalizacao.toLocalDateTime()).build();
	}
}
