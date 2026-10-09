package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.request.ServicoRequest;
import br.com.mecaniQA.api.response.ServicoResponse;

public class ServicoMapper {

	public static Servico toEntity(ServicoRequest request) {
		if (request == null) {
			return null;
		}

		Servico servico = new Servico();
		servico.setNome(request.getNome());
		servico.setTempoEstimadoMinutos(request.getTempoEstimadoMinutos());
		servico.setCustoTabelado(request.getCustoTabelado());
		return servico;
	}

	public static ServicoResponse toResponse(Servico entity) {
		if (entity == null) {
			return null;
		}

		ServicoResponse response = new ServicoResponse();
		response.setCodigo(entity.getCodigo());
		response.setNome(entity.getNome());
		response.setTempoEstimadoMinutos(entity.getTempoEstimadoMinutos());
		response.setCustoTabelado(entity.getCustoTabelado());
		response.setDataCriacao(entity.getDataCriacao());
		response.setDataAtualizacao(entity.getDataAtualizacao());
		return response;
	}

}
