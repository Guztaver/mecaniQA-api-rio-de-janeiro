package br.com.mecaniQA.api.model;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PedidoPecasTest {

	@Test
	@DisplayName("Deve permitir adicionar a mesma peça múltiplas vezes a um pedido via entidade associativa")
	void devePermitirAdicionarMesmaPecaMultiplasVezes() {
		Peca filtroOleo = new Peca();
		filtroOleo.setCodigo(1L);
		filtroOleo.setCodigoBarras("7891112223334");
		filtroOleo.setPrecoVenda(new BigDecimal("45.00"));

		ItemPedidoPecas item1 = ItemPedidoPecas.builder()
				.codigo(1L)
				.peca(filtroOleo)
				.quantidade(2)
				.precoUnitario(new BigDecimal("45.00"))
				.build();

		ItemPedidoPecas item2 = ItemPedidoPecas.builder()
				.codigo(2L)
				.peca(filtroOleo)
				.quantidade(3)
				.precoUnitario(new BigDecimal("45.00"))
				.build();

		PedidoPecas pedido = PedidoPecas.builder()
				.codigo(100L)
				.descricao("Pedido de filtros adicionais")
				.item(item1)
				.item(item2)
				.build();

		List<ItemPedidoPecas> itens = pedido.getItens();
		assertEquals(2, itens.size());
		assertEquals(StatusPedidoPecas.ORCANDO, pedido.getStatus());
		assertEquals(new BigDecimal("90.00"), item1.getSubtotal());
		assertEquals(new BigDecimal("135.00"), item2.getSubtotal());
		assertEquals(new BigDecimal("225.00"), pedido.getValorTotal());
	}

	@Test
	@DisplayName("Deve criar Ordem de Servico com Builder manual e status Aberto")
	void deveCriarOrdemServicoComBuilderManual() {
		Servico trocaOleo = new Servico(1L, "Troca de Óleo", 30, new BigDecimal("80.00"), null, null);

		OrdemServico os = OrdemServico.builder()
				.codigo(1L)
				.cliente("Carlos da Silva")
				.veiculo("Civic 2020")
				.servico(trocaOleo)
				.build();

		assertNotNull(os);
		assertEquals(StatusOrdemServico.ABERTO, os.getStatus());
		assertEquals(new BigDecimal("80.00"), os.getValorTotal());
	}

}
