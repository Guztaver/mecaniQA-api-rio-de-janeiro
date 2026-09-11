package br.com.mecaniQA.api.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdemServicoTest {

	@Test
	@DisplayName("Deve garantir que todos os construtores de OrdemServico sao privados")
	void deveGarantirQueConstrutoresSaoPrivados() {
		Constructor<?>[] constructors = OrdemServico.class.getDeclaredConstructors();
		assertTrue(constructors.length > 0, "Deve possuir ao menos um construtor");
		for (Constructor<?> constructor : constructors) {
			assertTrue(Modifier.isPrivate(constructor.getModifiers()),
					"O construtor deve ser estritamente privado para impedir new OrdemServico()");
		}
	}

	@Test
	@DisplayName("Deve instanciar OrdemServico via metodo estatico builder()")
	void deveCriarOrdemServicoViaMetodoBuilder() {
		Servico balanceamento = new Servico(1L, "Balanceamento", 40, new BigDecimal("60.00"), null, null);

		OrdemServico os = OrdemServico.builder()
				.codigo(10L)
				.descricao("Revisao preventiva")
				.cliente("Maria Oliveira")
				.veiculo("Corolla 2022")
				.servico(balanceamento)
				.build();

		assertNotNull(os);
		assertEquals(10L, os.getCodigo());
		assertEquals("Revisao preventiva", os.getDescricao());
		assertEquals("Maria Oliveira", os.getCliente());
		assertEquals("Corolla 2022", os.getVeiculo());
		assertEquals(StatusOrdemServico.ABERTO, os.getStatus());
		assertEquals(1, os.getServicos().size());
		assertEquals(new BigDecimal("60.00"), os.getValorTotal());
		assertNotNull(os.getDataAbertura());
		assertNotNull(os.getDataAtualizacao());
	}

	@Test
	@DisplayName("Deve instanciar OrdemServico via new OrdemServico.Builder()")
	void deveCriarOrdemServicoViaNewBuilder() {
		OrdemServico os = new OrdemServico.Builder()
				.codigo(20L)
				.cliente("Joao Santos")
				.veiculo("Gol 2018")
				.status(StatusOrdemServico.EM_EXECUCAO)
				.build();

		assertNotNull(os);
		assertEquals(20L, os.getCodigo());
		assertEquals("Joao Santos", os.getCliente());
		assertEquals(StatusOrdemServico.EM_EXECUCAO, os.getStatus());
		assertEquals(BigDecimal.ZERO, os.getValorTotal());
	}

	@Test
	@DisplayName("Deve calcular valor total somando servicos e pedido de pecas")
	void deveCalcularValorTotalComServicosEPedidoPecas() {
		Servico trocaPastilha = new Servico(2L, "Troca de Pastilhas", 60, new BigDecimal("120.00"), null, null);

		Peca pastilha = new Peca();
		pastilha.setCodigo(5L);
		pastilha.setPrecoVenda(new BigDecimal("90.00"));

		ItemPedidoPecas item = ItemPedidoPecas.builder()
				.codigo(1L)
				.peca(pastilha)
				.quantidade(2)
				.precoUnitario(new BigDecimal("90.00"))
				.build();

		PedidoPecas pedidoPecas = PedidoPecas.builder()
				.codigo(50L)
				.item(item)
				.build();

		OrdemServico os = OrdemServico.builder()
				.codigo(30L)
				.servico(trocaPastilha)
				.pedidoPecas(pedidoPecas)
				.build();

		assertEquals(new BigDecimal("300.00"), os.getValorTotal());
	}

	@Test
	@DisplayName("Deve recalcular valor total ao adicionar novo servico a OS")
	void deveRecalcularValorTotalAoAdicionarServico() {
		Servico alinhamento = new Servico(3L, "Alinhamento", 30, new BigDecimal("70.00"), null, null);
		Servico cambagem = new Servico(4L, "Cambagem", 45, new BigDecimal("80.00"), null, null);

		OrdemServico os = OrdemServico.builder()
				.codigo(40L)
				.servico(alinhamento)
				.build();

		assertEquals(new BigDecimal("70.00"), os.getValorTotal());

		os.adicionarServico(cambagem);
		assertEquals(2, os.getServicos().size());
		assertEquals(new BigDecimal("150.00"), os.getValorTotal());
	}

}
