package br.com.mecaniQA.api.service;

import java.util.List;

import br.com.mecaniQA.api.exception.DadosInvalidosException;
import br.com.mecaniQA.api.exception.RecursoNaoEncontradoException;
import br.com.mecaniQA.api.mapper.PecaMapper;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.request.PecaRequest;
import br.com.mecaniQA.api.response.PecaResponse;

public class PecaService {
	private final PecaRepository repository = PecaRepository.getInstance();

	public PecaResponse cadastrar(PecaRequest request) {
		validar(request);
		return PecaMapper.toResponse(repository.save(PecaMapper.toEntity(request)));
	}
	public List<PecaResponse> listar() { return repository.findAll().stream().map(PecaMapper::toResponse).toList(); }
	public PecaResponse buscar(Long codigo) { return PecaMapper.toResponse(encontrar(codigo)); }
	public PecaResponse atualizar(Long codigo, PecaRequest request) {
		validar(request);
		Peca peca = encontrar(codigo);
		Peca atualizada = PecaMapper.toEntity(request);
		atualizada.setCodigo(peca.getCodigo());
		return PecaMapper.toResponse(repository.update(atualizada));
	}
	public void excluir(Long codigo) { if (!repository.deleteById(codigo)) throw new RecursoNaoEncontradoException("Peça", codigo); }
	private Peca encontrar(Long codigo) {
		Peca peca = repository.findById(codigo);
		if (peca == null) throw new RecursoNaoEncontradoException("Peça", codigo);
		return peca;
	}
	private void validar(PecaRequest request) {
		if (request == null || request.getPrecoVenda() == null || request.getPrecoVenda().signum() < 0)
			throw new DadosInvalidosException("A peça deve possuir preço de venda não negativo.");
	}
}
