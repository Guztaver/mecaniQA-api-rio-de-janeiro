package br.com.mecaniQA.api.controller;

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

import br.com.mecaniQA.api.mapper.ItemPedidoPecasMapper;
import br.com.mecaniQA.api.mapper.PedidoPecasMapper;
import br.com.mecaniQA.api.model.ItemPedidoPecas;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.model.StatusPedidoPecas;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.repository.PedidoPecasRepository;
import br.com.mecaniQA.api.request.ItemPedidoPecasRequest;
import br.com.mecaniQA.api.request.PedidoPecasRequest;
import br.com.mecaniQA.api.request.StatusPedidoPecasRequest;
import br.com.mecaniQA.api.response.PedidoPecasResponse;

@RestController
@RequestMapping("/api/pedidos-pecas")
public class PedidoPecasController {

	private PedidoPecasRepository pedidoPecasRepository;
	private PecaRepository pecaRepository;

	public PedidoPecasController() {
		this.pedidoPecasRepository = PedidoPecasRepository.getInstance();
		this.pecaRepository = PecaRepository.getInstance();
	}

	@PostMapping
	public ResponseEntity<PedidoPecasResponse> criarPedido(@RequestBody PedidoPecasRequest request) {
		if (request == null || request.getItens() == null || request.getItens().isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		List<ItemPedidoPecas> itens = new ArrayList<>();
		long itemCodigoSequencial = 1L;
		for (ItemPedidoPecasRequest itemReq : request.getItens()) {
			if (itemReq.getPecaId() == null || itemReq.getQuantidade() == null || itemReq.getQuantidade() <= 0) {
				return ResponseEntity.badRequest().build();
			}

			Peca peca = pecaRepository.buscarPorCodigo(itemReq.getPecaId());
			if (peca == null) {
				return ResponseEntity.badRequest().build();
			}

			ItemPedidoPecas item = ItemPedidoPecasMapper.toEntity(itemReq, peca);
			item.setCodigo(itemCodigoSequencial++);
			itens.add(item);
		}

		PedidoPecas pedido = PedidoPecasMapper.toEntity(request, itens);
		PedidoPecas pedidoSalvo = pedidoPecasRepository.salvar(pedido);
		return ResponseEntity.status(HttpStatus.CREATED).body(PedidoPecasMapper.toResponse(pedidoSalvo));
	}

	@PostMapping("/{codigo}/itens")
	public ResponseEntity<PedidoPecasResponse> adicionarItem(
			@PathVariable Long codigo,
			@RequestBody ItemPedidoPecasRequest itemRequest) {

		PedidoPecas pedido = pedidoPecasRepository.buscarPorCodigo(codigo);
		if (pedido == null) {
			return ResponseEntity.notFound().build();
		}

		if (itemRequest == null || itemRequest.getPecaId() == null || itemRequest.getQuantidade() == null || itemRequest.getQuantidade() <= 0) {
			return ResponseEntity.badRequest().build();
		}

		Peca peca = pecaRepository.buscarPorCodigo(itemRequest.getPecaId());
		if (peca == null) {
			return ResponseEntity.badRequest().build();
		}

		ItemPedidoPecas item = ItemPedidoPecasMapper.toEntity(itemRequest, peca);
		long novoCodigoItem = (long) (pedido.getItens().size() + 1);
		item.setCodigo(novoCodigoItem);

		pedido.adicionarItem(item);
		PedidoPecas pedidoAtualizado = pedidoPecasRepository.atualizar(pedido);
		return ResponseEntity.ok(PedidoPecasMapper.toResponse(pedidoAtualizado));
	}

	@PutMapping("/{codigo}/status")
	public ResponseEntity<PedidoPecasResponse> atualizarStatusPut(
			@PathVariable Long codigo,
			@RequestBody StatusPedidoPecasRequest statusRequest) {
		return atualizarStatusInterno(codigo, statusRequest);
	}

	@PatchMapping("/{codigo}/status")
	public ResponseEntity<PedidoPecasResponse> atualizarStatusPatch(
			@PathVariable Long codigo,
			@RequestBody StatusPedidoPecasRequest statusRequest) {
		return atualizarStatusInterno(codigo, statusRequest);
	}

	private ResponseEntity<PedidoPecasResponse> atualizarStatusInterno(Long codigo, StatusPedidoPecasRequest statusRequest) {
		PedidoPecas pedido = pedidoPecasRepository.buscarPorCodigo(codigo);
		if (pedido == null) {
			return ResponseEntity.notFound().build();
		}

		if (statusRequest == null || statusRequest.getStatus() == null) {
			return ResponseEntity.badRequest().build();
		}

		pedido.setStatus(statusRequest.getStatus());
		PedidoPecas pedidoAtualizado = pedidoPecasRepository.atualizar(pedido);
		return ResponseEntity.ok(PedidoPecasMapper.toResponse(pedidoAtualizado));
	}

	@GetMapping
	public ResponseEntity<List<PedidoPecasResponse>> listar() {
		List<PedidoPecas> pedidos = pedidoPecasRepository.listar();
		List<PedidoPecasResponse> responses = new ArrayList<>();
		for (PedidoPecas pedido : pedidos) {
			responses.add(PedidoPecasMapper.toResponse(pedido));
		}
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<PedidoPecasResponse> buscarPorCodigo(@PathVariable Long codigo) {
		PedidoPecas pedido = pedidoPecasRepository.buscarPorCodigo(codigo);
		if (pedido == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(PedidoPecasMapper.toResponse(pedido));
	}

}
