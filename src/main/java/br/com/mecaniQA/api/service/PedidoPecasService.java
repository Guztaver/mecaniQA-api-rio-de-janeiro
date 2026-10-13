package br.com.mecaniQA.api.service;

import java.util.ArrayList;
import java.util.List;
import br.com.mecaniQA.api.exception.DadosInvalidosException;
import br.com.mecaniQA.api.exception.RecursoNaoEncontradoException;
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
import br.com.mecaniQA.api.response.PedidoPecasResponse;

public class PedidoPecasService {
	private final PedidoPecasRepository pedidos = PedidoPecasRepository.getInstance();
	private final PecaRepository pecas = PecaRepository.getInstance();
	public PedidoPecasResponse criar(PedidoPecasRequest request) {
		if (request == null || request.getItens() == null || request.getItens().isEmpty()) throw new DadosInvalidosException("O pedido deve possuir itens.");
		List<ItemPedidoPecas> itens = new ArrayList<>();
		for (ItemPedidoPecasRequest item : request.getItens()) itens.add(criarItem(item));
		return PedidoPecasMapper.toResponse(pedidos.save(PedidoPecasMapper.toEntity(request, itens)));
	}
	public PedidoPecasResponse adicionarItem(Long codigo, ItemPedidoPecasRequest item) { PedidoPecas pedido = encontrar(codigo); pedido.adicionarItem(criarItem(item)); return PedidoPecasMapper.toResponse(pedidos.update(pedido)); }
	public PedidoPecasResponse atualizarStatus(Long codigo, StatusPedidoPecas status) { if (status == null) throw new DadosInvalidosException("Status é obrigatório."); PedidoPecas p = encontrar(codigo); p.setStatus(status); return PedidoPecasMapper.toResponse(pedidos.update(p)); }
	public List<PedidoPecasResponse> listar() { return pedidos.findAll().stream().map(PedidoPecasMapper::toResponse).toList(); }
	public PedidoPecasResponse buscar(Long codigo) { return PedidoPecasMapper.toResponse(encontrar(codigo)); }
	private ItemPedidoPecas criarItem(ItemPedidoPecasRequest item) { if (item == null || item.getPecaId() == null || item.getQuantidade() == null || item.getQuantidade() <= 0) throw new DadosInvalidosException("Peça e quantidade positiva são obrigatórias."); Peca peca = pecas.findById(item.getPecaId()); if (peca == null) throw new RecursoNaoEncontradoException("Peça", item.getPecaId()); return ItemPedidoPecasMapper.toEntity(item, peca); }
	private PedidoPecas encontrar(Long codigo) { PedidoPecas pedido = pedidos.findById(codigo); if (pedido == null) throw new RecursoNaoEncontradoException("Pedido", codigo); return pedido; }
}
