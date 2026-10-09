package br.com.mecaniQA.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mecaniQA.api.mapper.ServicoMapper;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.ServicoRepository;
import br.com.mecaniQA.api.request.ServicoRequest;
import br.com.mecaniQA.api.response.ServicoResponse;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

	private ServicoRepository servicoRepository;

	public ServicoController() {
		servicoRepository = ServicoRepository.getInstance();
	}

	@PostMapping
	public ResponseEntity<ServicoResponse> cadastrar(@RequestBody ServicoRequest request) {
		Servico servico = ServicoMapper.toEntity(request);
		Servico servicoCadastrado = servicoRepository.salvar(servico);
		return ResponseEntity.status(HttpStatus.CREATED).body(ServicoMapper.toResponse(servicoCadastrado));
	}

	@GetMapping
	public ResponseEntity<List<ServicoResponse>> listar() {
		List<Servico> servicos = servicoRepository.listar();
		List<ServicoResponse> responses = new ArrayList<>();
		for (Servico servico : servicos) {
			responses.add(ServicoMapper.toResponse(servico));
		}
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<ServicoResponse> buscarPorCodigo(@PathVariable Long codigo) {
		Servico servico = servicoRepository.buscarPorCodigo(codigo);
		if (servico == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(ServicoMapper.toResponse(servico));
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<ServicoResponse> atualizar(@PathVariable Long codigo, @RequestBody ServicoRequest request) {
		Servico servico = servicoRepository.buscarPorCodigo(codigo);
		if (servico == null) {
			return ResponseEntity.notFound().build();
		}

		if (request.getNome() != null) {
			servico.setNome(request.getNome());
		}
		servico.setTempoEstimadoMinutos(request.getTempoEstimadoMinutos());
		servico.setCustoTabelado(request.getCustoTabelado());

		Servico servicoAtualizado = servicoRepository.atualizar(servico);
		return ResponseEntity.ok(ServicoMapper.toResponse(servicoAtualizado));
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		if (!servicoRepository.excluir(codigo)) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.noContent().build();
	}

}
