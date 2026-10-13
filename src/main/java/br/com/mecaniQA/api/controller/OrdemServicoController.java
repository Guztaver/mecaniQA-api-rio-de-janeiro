package br.com.mecaniQA.api.controller;

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
import br.com.mecaniQA.api.exception.DadosInvalidosException;
import br.com.mecaniQA.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniQA.api.request.OrdemServicoRequest;
import br.com.mecaniQA.api.request.StatusOrdemServicoRequest;
import br.com.mecaniQA.api.response.OrdemServicoResponse;
import br.com.mecaniQA.api.service.OrdemServicoService;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {
	private final OrdemServicoService service = new OrdemServicoService();
	@PostMapping public ResponseEntity<OrdemServicoResponse> criarOrdemServico(@RequestBody OrdemServicoRequest r) { try { return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(r)); } catch (DadosInvalidosException e) { return ResponseEntity.badRequest().build(); } catch (RecursoNaoEncontradoException e) { return ResponseEntity.badRequest().build(); } }
	@PutMapping("/{codigo}/status") public ResponseEntity<OrdemServicoResponse> atualizarStatusPut(@PathVariable Long codigo, @RequestBody StatusOrdemServicoRequest r) { return atualizar(codigo, r); }
	@PatchMapping("/{codigo}/status") public ResponseEntity<OrdemServicoResponse> atualizarStatusPatch(@PathVariable Long codigo, @RequestBody StatusOrdemServicoRequest r) { return atualizar(codigo, r); }
	@GetMapping public ResponseEntity<List<OrdemServicoResponse>> listar() { return ResponseEntity.ok(service.listar()); }
	@GetMapping("/{codigo}") public ResponseEntity<OrdemServicoResponse> buscarPorCodigo(@PathVariable Long codigo) { try { return ResponseEntity.ok(service.buscar(codigo)); } catch (RecursoNaoEncontradoException e) { return ResponseEntity.notFound().build(); } }
	private ResponseEntity<OrdemServicoResponse> atualizar(Long codigo, StatusOrdemServicoRequest r) { try { return ResponseEntity.ok(service.atualizarStatus(codigo, r == null ? null : r.getStatus())); } catch (RecursoNaoEncontradoException e) { return ResponseEntity.notFound().build(); } catch (DadosInvalidosException e) { return ResponseEntity.badRequest().build(); } }
}
