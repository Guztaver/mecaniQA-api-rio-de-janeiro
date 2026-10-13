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
import br.com.mecaniQA.api.request.ItemPedidoPecasRequest;
import br.com.mecaniQA.api.request.PedidoPecasRequest;
import br.com.mecaniQA.api.request.StatusPedidoPecasRequest;
import br.com.mecaniQA.api.response.PedidoPecasResponse;
import br.com.mecaniQA.api.service.PedidoPecasService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoPecasController {
	private final PedidoPecasService service = new PedidoPecasService();
	@PostMapping public ResponseEntity<PedidoPecasResponse> criarPedido(@RequestBody PedidoPecasRequest r) { try { return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(r)); } catch (DadosInvalidosException | RecursoNaoEncontradoException e) { return ResponseEntity.badRequest().build(); } }
	@PostMapping("/{codigo}/itens") public ResponseEntity<PedidoPecasResponse> adicionarItem(@PathVariable Long codigo, @RequestBody ItemPedidoPecasRequest r) { try { return ResponseEntity.ok(service.adicionarItem(codigo, r)); } catch (RecursoNaoEncontradoException e) { return ResponseEntity.notFound().build(); } catch (DadosInvalidosException e) { return ResponseEntity.badRequest().build(); } }
	@PutMapping("/{codigo}/status") public ResponseEntity<PedidoPecasResponse> atualizarStatusPut(@PathVariable Long codigo, @RequestBody StatusPedidoPecasRequest r) { return atualizar(codigo, r); }
	@PatchMapping("/{codigo}/status") public ResponseEntity<PedidoPecasResponse> atualizarStatusPatch(@PathVariable Long codigo, @RequestBody StatusPedidoPecasRequest r) { return atualizar(codigo, r); }
	@GetMapping public ResponseEntity<List<PedidoPecasResponse>> listar() { return ResponseEntity.ok(service.listar()); }
	@GetMapping("/{codigo}") public ResponseEntity<PedidoPecasResponse> buscarPorCodigo(@PathVariable Long codigo) { try { return ResponseEntity.ok(service.buscar(codigo)); } catch (RecursoNaoEncontradoException e) { return ResponseEntity.notFound().build(); } }
	private ResponseEntity<PedidoPecasResponse> atualizar(Long codigo, StatusPedidoPecasRequest r) { try { return ResponseEntity.ok(service.atualizarStatus(codigo, r == null ? null : r.getStatus())); } catch (RecursoNaoEncontradoException e) { return ResponseEntity.notFound().build(); } catch (DadosInvalidosException e) { return ResponseEntity.badRequest().build(); } }
}
