package br.com.mecaniQA.api.service;

import java.util.List;

import br.com.mecaniQA.api.exception.DadosInvalidosException;
import br.com.mecaniQA.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniQA.api.mapper.ServicoMapper;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.ServicoRepository;
import br.com.mecaniQA.api.request.ServicoRequest;
import br.com.mecaniQA.api.response.ServicoResponse;

public class ServicoService {
	private final ServicoRepository repository = ServicoRepository.getInstance();
	public ServicoResponse cadastrar(ServicoRequest request) { validar(request); return ServicoMapper.toResponse(repository.save(ServicoMapper.toEntity(request))); }
	public List<ServicoResponse> listar() { return repository.findAll().stream().map(ServicoMapper::toResponse).toList(); }
	public ServicoResponse buscar(Long codigo) { return ServicoMapper.toResponse(encontrar(codigo)); }
	public ServicoResponse atualizar(Long codigo, ServicoRequest request) {
		validar(request); Servico entidade = ServicoMapper.toEntity(request); entidade.setCodigo(encontrar(codigo).getCodigo());
		return ServicoMapper.toResponse(repository.update(entidade));
	}
	public void excluir(Long codigo) { if (!repository.deleteById(codigo)) throw new RecursoNaoEncontradoException("Serviço", codigo); }
	private Servico encontrar(Long codigo) { Servico s = repository.findById(codigo); if (s == null) throw new RecursoNaoEncontradoException("Serviço", codigo); return s; }
	private void validar(ServicoRequest r) {
		if (r == null || r.getNome() == null || r.getNome().isBlank() || r.getTempoEstimadoMinutos() == null || r.getTempoEstimadoMinutos() <= 0 || r.getCustoTabelado() == null || r.getCustoTabelado().signum() < 0)
			throw new DadosInvalidosException("Nome, tempo estimado positivo e custo não negativo são obrigatórios.");
	}
}
