package br.com.mecaniQA.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import br.com.mecaniQA.api.exception.DadosInvalidosException;
import br.com.mecaniQA.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniQA.api.mapper.OrdemServicoMapper;
import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.model.StatusOrdemServico;
import br.com.mecaniQA.api.repository.OrdemServicoRepository;
import br.com.mecaniQA.api.repository.PedidoPecasRepository;
import br.com.mecaniQA.api.repository.ServicoRepository;
import br.com.mecaniQA.api.request.OrdemServicoRequest;
import br.com.mecaniQA.api.response.OrdemServicoResponse;

public class OrdemServicoService {
	private final OrdemServicoRepository ordens = OrdemServicoRepository.getInstance();
	private final ServicoRepository servicos = ServicoRepository.getInstance();
	private final PedidoPecasRepository pedidos = PedidoPecasRepository.getInstance();
	public OrdemServicoResponse criar(OrdemServicoRequest request) {
		if (request == null || request.getCliente() == null || request.getCliente().isBlank() || request.getVeiculo() == null || request.getVeiculo().isBlank()) throw new DadosInvalidosException("Cliente e veículo são obrigatórios.");
		List<Servico> lista = new ArrayList<>();
		if (request.getServicoIds() != null) for (Long id : request.getServicoIds()) { Servico s = servicos.findById(id); if (s == null) throw new RecursoNaoEncontradoException("Serviço", id); lista.add(s); }
		PedidoPecas pedido = null;
		if (request.getPedidoPecasId() != null) { pedido = pedidos.findById(request.getPedidoPecasId()); if (pedido == null) throw new RecursoNaoEncontradoException("Pedido", request.getPedidoPecasId()); }
		return OrdemServicoMapper.toResponse(ordens.save(OrdemServicoMapper.toEntity(request, lista, pedido)));
	}
	public OrdemServicoResponse atualizarStatus(Long codigo, StatusOrdemServico status) { if (status == null) throw new DadosInvalidosException("Status é obrigatório."); OrdemServico ordem = encontrar(codigo); ordem.setStatus(status); if (status == StatusOrdemServico.EXECUTADO || status == StatusOrdemServico.PAGO) ordem.setDataFinalizacao(LocalDateTime.now()); return OrdemServicoMapper.toResponse(ordens.update(ordem)); }
	public List<OrdemServicoResponse> listar() { return ordens.findAll().stream().map(OrdemServicoMapper::toResponse).toList(); }
	public OrdemServicoResponse buscar(Long codigo) { return OrdemServicoMapper.toResponse(encontrar(codigo)); }
	private OrdemServico encontrar(Long codigo) { OrdemServico ordem = ordens.findById(codigo); if (ordem == null) throw new RecursoNaoEncontradoException("Ordem de serviço", codigo); return ordem; }
}
