package br.com.mecaniQA.api.controller;

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

import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.request.PecaRequest;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

	private PecaRepository pecaRepository;

	public PecaController() {
		pecaRepository = PecaRepository.getInstance();
	}

	@PostMapping
	public ResponseEntity<Peca> cadastrar(@RequestBody PecaRequest request) {
		Peca peca = new Peca();
		peca.setCodigoBarras(request.getCodigoBarras());
		peca.setFornecedor(request.getFornecedor());
		peca.setQuantidadeEstoque(request.getQuantidadeEstoque());
		peca.setPrecoCusto(request.getPrecoCusto());
		peca.setPrecoVenda(request.getPrecoVenda());
		peca.setTamanho(request.getTamanho());
		peca.setCor(request.getCor());
		peca.setCategoria(request.getCategoria());

		Peca pecaCadastrada = pecaRepository.salvar(peca);
		return ResponseEntity.status(HttpStatus.CREATED).body(pecaCadastrada);
	}

	@GetMapping
	public ResponseEntity<List<Peca>> listar() {
		return ResponseEntity.ok(pecaRepository.listar());
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Peca> buscarPorCodigo(@PathVariable Long codigo) {
		Peca peca = pecaRepository.buscarPorCodigo(codigo);
		if (peca == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(peca);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Peca> atualizar(@PathVariable Long codigo, @RequestBody PecaRequest request) {
		Peca peca = pecaRepository.buscarPorCodigo(codigo);
		if (peca == null) {
			return ResponseEntity.notFound().build();
		}

		peca.setQuantidadeEstoque(request.getQuantidadeEstoque());
		peca.setPrecoCusto(request.getPrecoCusto());
		peca.setPrecoVenda(request.getPrecoVenda());
		return ResponseEntity.ok(pecaRepository.atualizar(peca));
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		if (!pecaRepository.excluir(codigo)) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.noContent().build();
	}

}
