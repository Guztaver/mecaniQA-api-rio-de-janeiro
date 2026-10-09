package br.com.mecaniQA.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.mecaniQA.api.model.CategoriaPeca;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.model.StatusOrdemServico;
import br.com.mecaniQA.api.repository.OrdemServicoRepository;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.repository.PedidoPecasRepository;
import br.com.mecaniQA.api.repository.ServicoRepository;
import br.com.mecaniQA.api.request.OrdemServicoRequest;
import br.com.mecaniQA.api.request.StatusOrdemServicoRequest;
import br.com.mecaniQA.api.response.OrdemServicoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrdemServicoControllerTest {

	private OrdemServicoController controller;
	private ServicoRepository servicoRepository;
	private PecaRepository pecaRepository;
	private PedidoPecasRepository pedidoPecasRepository;

	@BeforeEach
	void setUp() {
		OrdemServicoRepository.getInstance().reset();
		servicoRepository = ServicoRepository.getInstance();
		pecaRepository = PecaRepository.getInstance();
		pedidoPecasRepository = PedidoPecasRepository.getInstance();
		controller = new OrdemServicoController();
	}

	@Test
	@DisplayName("US01 - Deve criar uma Ordem de Servico com sucesso")
	void deveCriarOrdemServicoComSucesso() {
		Servico servico = new Servico();
		servico.setNome("Troca de Óleo");
		servico.setTempoEstimadoMinutos(45);
		servico.setCustoTabelado(new BigDecimal("150.00"));
		servico = servicoRepository.salvar(servico);

		OrdemServicoRequest request = new OrdemServicoRequest();
		request.setCliente("João Silva");
		request.setVeiculo("Civic 2021");
		request.setDescricao("Revisão de 50k km");
		request.setServicoIds(List.of(servico.getCodigo()));

		ResponseEntity<OrdemServicoResponse> response = controller.criarOrdemServico(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1L, response.getBody().getCodigo());
		assertEquals("João Silva", response.getBody().getCliente());
		assertEquals(StatusOrdemServico.ABERTO, response.getBody().getStatus());
		assertEquals(1, response.getBody().getServicos().size());
		assertEquals(new BigDecimal("150.00"), response.getBody().getValorTotal());
	}

	@Test
	@DisplayName("US02 - Deve modificar status da Ordem de Servico com sucesso")
	void deveModificarStatusOrdemServicoComSucesso() {
		OrdemServicoRequest request = new OrdemServicoRequest();
		request.setCliente("Maria Souza");
		request.setVeiculo("Corolla 2020");
		request.setDescricao("Alinhamento");

		ResponseEntity<OrdemServicoResponse> responseCriacao = controller.criarOrdemServico(request);
		Long osCodigo = responseCriacao.getBody().getCodigo();

		StatusOrdemServicoRequest statusRequest = new StatusOrdemServicoRequest(StatusOrdemServico.EM_EXECUCAO);
		ResponseEntity<OrdemServicoResponse> responseAtualizacao = controller.atualizarStatusPut(osCodigo, statusRequest);

		assertEquals(HttpStatus.OK, responseAtualizacao.getStatusCode());
		assertEquals(StatusOrdemServico.EM_EXECUCAO, responseAtualizacao.getBody().getStatus());

		StatusOrdemServicoRequest statusExecutado = new StatusOrdemServicoRequest(StatusOrdemServico.EXECUTADO);
		ResponseEntity<OrdemServicoResponse> responseFinalizacao = controller.atualizarStatusPatch(osCodigo, statusExecutado);

		assertEquals(HttpStatus.OK, responseFinalizacao.getStatusCode());
		assertEquals(StatusOrdemServico.EXECUTADO, responseFinalizacao.getBody().getStatus());
		assertNotNull(responseFinalizacao.getBody().getDataFinalizacao());
	}

}
