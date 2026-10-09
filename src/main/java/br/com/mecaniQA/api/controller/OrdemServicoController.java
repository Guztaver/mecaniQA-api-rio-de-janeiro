package br.com.mecaniQA.api.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mecaniQA.api.mapper.OrdemServicoMapper;
import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.model.StatusOrdemServico;
import br.com.mecaniQA.api.repository.OrdemServicoRepository;
import br.com.mecaniQA.api.repository.PedidoPecasRepository;
import br.com.mecaniQA.api.repository.ServicoRepository;
import br.com.mecaniQA.api.request.OrdemServicoRequest;
import br.com.mecaniQA.api.request.StatusOrdemServicoRequest;
import br.com.mecaniQA.api.response.OrdemServicoResponse;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

	private OrdemServicoRepository ordemServicoRepository;
	private ServicoRepository servicoRepository;
	private PedidoPecasRepository pedidoPecasRepository;

	public OrdemServicoController() {
		this.ordemServicoRepository = OrdemServicoRepository.getInstance();
		this.servicoRepository = ServicoRepository.getInstance();
		this.pedidoPecasRepository = PedidoPecasRepository.getInstance();
	}

	@PostMapping
	public ResponseEntity<OrdemServicoResponse> criarOrdemServico(@RequestBody OrdemServicoRequest request) {
		if (request == null || request.getCliente() == null || request.getVeiculo() == null) {
			return ResponseEntity.badRequest().build();
		}

		List<Servico> servicos = new ArrayList<>();
		if (request.getServicoIds() != null) {
			for (Long servicoId : request.getServicoIds()) {
				Servico servico = servicoRepository.buscarPorCodigo(servicoId);
				if (servico != null) {
					servicos.add(servico);
				}
			}
		}

		PedidoPecas pedidoPecas = null;
		if (request.getPedidoPecasId() != null) {
			pedidoPecas = pedidoPecasRepository.buscarPorCodigo(request.getPedidoPecasId());
		}

		OrdemServico os = OrdemServicoMapper.toEntity(request, servicos, pedidoPecas);
		OrdemServico osSalva = ordemServicoRepository.salvar(os);
		return ResponseEntity.status(HttpStatus.CREATED).body(OrdemServicoMapper.toResponse(osSalva));
	}

	@PutMapping("/{codigo}/status")
	public ResponseEntity<OrdemServicoResponse> atualizarStatusPut(
			@PathVariable Long codigo,
			@RequestBody StatusOrdemServicoRequest statusRequest) {
		return atualizarStatusInterno(codigo, statusRequest);
	}

	@PatchMapping("/{codigo}/status")
	public ResponseEntity<OrdemServicoResponse> atualizarStatusPatch(
			@PathVariable Long codigo,
			@RequestBody StatusOrdemServicoRequest statusRequest) {
		return atualizarStatusInterno(codigo, statusRequest);
	}

	private ResponseEntity<OrdemServicoResponse> atualizarStatusInterno(Long codigo, StatusOrdemServicoRequest statusRequest) {
		OrdemServico os = ordemServicoRepository.buscarPorCodigo(codigo);
		if (os == null) {
			return ResponseEntity.notFound().build();
		}

		if (statusRequest == null || statusRequest.getStatus() == null) {
			return ResponseEntity.badRequest().build();
		}

		os.setStatus(statusRequest.getStatus());
		if (StatusOrdemServico.EXECUTADO.equals(statusRequest.getStatus()) || StatusOrdemServico.PAGO.equals(statusRequest.getStatus())) {
			os.setDataFinalizacao(LocalDateTime.now());
		}

		OrdemServico osAtualizada = ordemServicoRepository.atualizar(os);
		return ResponseEntity.ok(OrdemServicoMapper.toResponse(osAtualizada));
	}

	@GetMapping
	public ResponseEntity<List<OrdemServicoResponse>> listar() {
		List<OrdemServico> ordens = ordemServicoRepository.listar();
		List<OrdemServicoResponse> responses = new ArrayList<>();
		for (OrdemServico os : ordens) {
			responses.add(OrdemServicoMapper.toResponse(os));
		}
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<OrdemServicoResponse> buscarPorCodigo(@PathVariable Long codigo) {
		OrdemServico os = ordemServicoRepository.buscarPorCodigo(codigo);
		if (os == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(OrdemServicoMapper.toResponse(os));
	}

}
