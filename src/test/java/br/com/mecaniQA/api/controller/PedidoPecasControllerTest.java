package br.com.mecaniQA.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.mecaniQA.api.model.CategoriaPeca;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.model.StatusPedidoPecas;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.repository.PedidoPecasRepository;
import br.com.mecaniQA.api.request.ItemPedidoPecasRequest;
import br.com.mecaniQA.api.request.PedidoPecasRequest;
import br.com.mecaniQA.api.request.StatusPedidoPecasRequest;
import br.com.mecaniQA.api.response.PedidoPecasResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PedidoPecasControllerTest {

	private PedidoPecasController controller;
	private PecaRepository pecaRepository;
	private PedidoPecasRepository pedidoPecasRepository;

	@BeforeEach
	void setUp() {
		pedidoPecasRepository = PedidoPecasRepository.getInstance();
		pedidoPecasRepository.reset();
		pecaRepository = PecaRepository.getInstance();
		controller = new PedidoPecasController();
	}

	@Test
	@DisplayName("US03 - Deve criar um Pedido de Pecas com status Orcando")
	void deveCriarPedidoPecasComSucesso() {
		Peca vela = new Peca();
		vela.setCodigoBarras("123456");
		vela.setPrecoVenda(new BigDecimal("35.00"));
		vela.setCategoria(CategoriaPeca.MOTOR);
		vela = pecaRepository.salvar(vela);

		ItemPedidoPecasRequest itemReq = new ItemPedidoPecasRequest(vela.getCodigo(), 4);
		PedidoPecasRequest request = new PedidoPecasRequest();
		request.setDescricao("Jogo de velas de ignição");
		request.setItens(List.of(itemReq));

		ResponseEntity<PedidoPecasResponse> response = controller.criarPedido(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(StatusPedidoPecas.ORCANDO, response.getBody().getStatus());
		assertEquals(1, response.getBody().getItens().size());
		assertEquals(new BigDecimal("140.00"), response.getBody().getValorTotal());
	}

	@Test
	@DisplayName("US04 - Deve adicionar novas pecas a um pedido existente")
	void deveAdicionarNovasPecasAoPedidoExistente() {
		Peca filtro = new Peca();
		filtro.setPrecoVenda(new BigDecimal("50.00"));
		filtro = pecaRepository.salvar(filtro);

		Peca oleo = new Peca();
		oleo.setPrecoVenda(new BigDecimal("40.00"));
		oleo = pecaRepository.salvar(oleo);

		PedidoPecasRequest request = new PedidoPecasRequest();
		request.setDescricao("Pedido inicial");
		request.setItens(List.of(new ItemPedidoPecasRequest(filtro.getCodigo(), 1)));

		ResponseEntity<PedidoPecasResponse> criacao = controller.criarPedido(request);
		Long pedidoCodigo = criacao.getBody().getCodigo();

		ItemPedidoPecasRequest novoItemReq = new ItemPedidoPecasRequest(oleo.getCodigo(), 4);
		ResponseEntity<PedidoPecasResponse> atualizacao = controller.adicionarItem(pedidoCodigo, novoItemReq);

		assertEquals(HttpStatus.OK, atualizacao.getStatusCode());
		assertEquals(2, atualizacao.getBody().getItens().size());
		assertEquals(new BigDecimal("210.00"), atualizacao.getBody().getValorTotal());
	}

	@Test
	@DisplayName("US05 - Deve modificar o status do Pedido")
	void deveModificarStatusDoPedido() {
		Peca disco = new Peca();
		disco.setPrecoVenda(new BigDecimal("200.00"));
		disco = pecaRepository.salvar(disco);

		PedidoPecasRequest request = new PedidoPecasRequest();
		request.setDescricao("Freios dianteiros");
		request.setItens(List.of(new ItemPedidoPecasRequest(disco.getCodigo(), 2)));

		ResponseEntity<PedidoPecasResponse> criacao = controller.criarPedido(request);
		Long pedidoCodigo = criacao.getBody().getCodigo();

		StatusPedidoPecasRequest statusReq = new StatusPedidoPecasRequest(StatusPedidoPecas.PAGO_FATURADO);
		ResponseEntity<PedidoPecasResponse> atualizacao = controller.atualizarStatusPut(pedidoCodigo, statusReq);

		assertEquals(HttpStatus.OK, atualizacao.getStatusCode());
		assertEquals(StatusPedidoPecas.PAGO_FATURADO, atualizacao.getBody().getStatus());
	}

}
