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

import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.ServicoRepository;
import br.com.mecaniQA.api.request.ServicoRequest;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

	private ServicoRepository servicoRepository;

	public ServicoController() {
		servicoRepository = ServicoRepository.getInstance();
	}

	@PostMapping
	public ResponseEntity<Servico> cadastrar(@RequestBody ServicoRequest request) {
		Servico servico = new Servico();
		servico.setNome(request.getNome());
		servico.setTempoEstimadoMinutos(request.getTempoEstimadoMinutos());
		servico.setCustoTabelado(request.getCustoTabelado());

		Servico servicoCadastrado = servicoRepository.salvar(servico);
		return ResponseEntity.status(HttpStatus.CREATED).body(servicoCadastrado);
	}

	@GetMapping
	public ResponseEntity<List<Servico>> listar() {
		return ResponseEntity.ok(servicoRepository.listar());
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Servico> buscarPorCodigo(@PathVariable Long codigo) {
		Servico servico = servicoRepository.buscarPorCodigo(codigo);
		if (servico == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(servico);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Servico> atualizar(@PathVariable Long codigo, @RequestBody ServicoRequest request) {
		Servico servico = servicoRepository.buscarPorCodigo(codigo);
		if (servico == null) {
			return ResponseEntity.notFound().build();
		}

		servico.setTempoEstimadoMinutos(request.getTempoEstimadoMinutos());
		servico.setCustoTabelado(request.getCustoTabelado());
		return ResponseEntity.ok(servicoRepository.atualizar(servico));
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		if (!servicoRepository.excluir(codigo)) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.noContent().build();
	}

}
