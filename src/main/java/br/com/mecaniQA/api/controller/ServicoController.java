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
import br.com.mecaniQA.api.exception.DadosInvalidosException;
import br.com.mecaniQA.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniQA.api.request.ServicoRequest;
import br.com.mecaniQA.api.response.ServicoResponse;
import br.com.mecaniQA.api.service.ServicoService;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {
	private final ServicoService service = new ServicoService();
	@PostMapping public ResponseEntity<ServicoResponse> cadastrar(@RequestBody ServicoRequest r) { try { return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(r)); } catch (DadosInvalidosException e) { return ResponseEntity.badRequest().build(); } }
	@GetMapping public ResponseEntity<List<ServicoResponse>> listar() { return ResponseEntity.ok(service.listar()); }
	@GetMapping("/{codigo}") public ResponseEntity<ServicoResponse> buscarPorCodigo(@PathVariable Long codigo) { try { return ResponseEntity.ok(service.buscar(codigo)); } catch (RecursoNaoEncontradoException e) { return ResponseEntity.notFound().build(); } }
	@PutMapping("/{codigo}") public ResponseEntity<ServicoResponse> atualizar(@PathVariable Long codigo, @RequestBody ServicoRequest r) { try { return ResponseEntity.ok(service.atualizar(codigo, r)); } catch (RecursoNaoEncontradoException e) { return ResponseEntity.notFound().build(); } catch (DadosInvalidosException e) { return ResponseEntity.badRequest().build(); } }
	@DeleteMapping("/{codigo}") public ResponseEntity<Void> excluir(@PathVariable Long codigo) { try { service.excluir(codigo); return ResponseEntity.noContent().build(); } catch (RecursoNaoEncontradoException e) { return ResponseEntity.notFound().build(); } }
}
