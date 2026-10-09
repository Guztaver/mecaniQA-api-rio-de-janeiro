package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.request.PecaRequest;
import br.com.mecaniQA.api.response.PecaResponse;

public class PecaMapper {

	public static Peca toEntity(PecaRequest request) {
		if (request == null) {
			return null;
		}

		Peca peca = new Peca();
		peca.setCodigoBarras(request.getCodigoBarras());
		peca.setFornecedor(request.getFornecedor());
		peca.setQuantidadeEstoque(request.getQuantidadeEstoque());
		peca.setPrecoCusto(request.getPrecoCusto());
		peca.setPrecoVenda(request.getPrecoVenda());
		peca.setTamanho(request.getTamanho());
		peca.setCor(request.getCor());
		peca.setCategoria(request.getCategoria());
		return peca;
	}

	public static PecaResponse toResponse(Peca entity) {
		if (entity == null) {
			return null;
		}

		PecaResponse response = new PecaResponse();
		response.setCodigo(entity.getCodigo());
		response.setCodigoBarras(entity.getCodigoBarras());
		response.setFornecedor(entity.getFornecedor());
		response.setQuantidadeEstoque(entity.getQuantidadeEstoque());
		response.setPrecoCusto(entity.getPrecoCusto());
		response.setPrecoVenda(entity.getPrecoVenda());
		response.setTamanho(entity.getTamanho());
		response.setCor(entity.getCor());
		response.setCategoria(entity.getCategoria());
		response.setDataCadastro(entity.getDataCadastro());
		response.setDataAtualizacao(entity.getDataAtualizacao());
		return response;
	}

}
