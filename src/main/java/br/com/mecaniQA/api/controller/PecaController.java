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

import br.com.mecaniQA.api.mapper.PecaMapper;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.request.PecaRequest;
import br.com.mecaniQA.api.response.PecaResponse;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

	private PecaRepository pecaRepository;

	public PecaController() {
		pecaRepository = PecaRepository.getInstance();
	}

	@PostMapping
	public ResponseEntity<PecaResponse> cadastrar(@RequestBody PecaRequest request) {
		Peca peca = PecaMapper.toEntity(request);
		Peca pecaCadastrada = pecaRepository.salvar(peca);
		return ResponseEntity.status(HttpStatus.CREATED).body(PecaMapper.toResponse(pecaCadastrada));
	}

	@GetMapping
	public ResponseEntity<List<PecaResponse>> listar() {
		List<Peca> pecas = pecaRepository.listar();
		List<PecaResponse> responses = new ArrayList<>();
		for (Peca peca : pecas) {
			responses.add(PecaMapper.toResponse(peca));
		}
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<PecaResponse> buscarPorCodigo(@PathVariable Long codigo) {
		Peca peca = pecaRepository.buscarPorCodigo(codigo);
		if (peca == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(PecaMapper.toResponse(peca));
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<PecaResponse> atualizar(@PathVariable Long codigo, @RequestBody PecaRequest request) {
		Peca peca = pecaRepository.buscarPorCodigo(codigo);
		if (peca == null) {
			return ResponseEntity.notFound().build();
		}

		peca.setQuantidadeEstoque(request.getQuantidadeEstoque());
		peca.setPrecoCusto(request.getPrecoCusto());
		peca.setPrecoVenda(request.getPrecoVenda());
		if (request.getCodigoBarras() != null) {
			peca.setCodigoBarras(request.getCodigoBarras());
		}
		if (request.getFornecedor() != null) {
			peca.setFornecedor(request.getFornecedor());
		}
		if (request.getCategoria() != null) {
			peca.setCategoria(request.getCategoria());
		}

		Peca pecaAtualizada = pecaRepository.atualizar(peca);
		return ResponseEntity.ok(PecaMapper.toResponse(pecaAtualizada));
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		if (!pecaRepository.excluir(codigo)) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.noContent().build();
	}

}
