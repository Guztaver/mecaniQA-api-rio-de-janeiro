package br.com.mecaniQA.api.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.request.OrdemServicoRequest;
import br.com.mecaniQA.api.response.OrdemServicoResponse;
import br.com.mecaniQA.api.response.ServicoResponse;

public class OrdemServicoMapper {

	public static OrdemServico toEntity(OrdemServicoRequest request, List<Servico> servicos, PedidoPecas pedidoPecas) {
		if (request == null) {
			return null;
		}

		return OrdemServico.builder()
				.descricao(request.getDescricao())
				.cliente(request.getCliente())
				.veiculo(request.getVeiculo())
				.servicos(servicos)
				.pedidoPecas(pedidoPecas)
				.build();
	}

	public static OrdemServicoResponse toResponse(OrdemServico entity) {
		if (entity == null) {
			return null;
		}

		List<ServicoResponse> servicosResponse = new ArrayList<>();
		if (entity.getServicos() != null) {
			for (Servico servico : entity.getServicos()) {
				servicosResponse.add(ServicoMapper.toResponse(servico));
			}
		}

		OrdemServicoResponse response = new OrdemServicoResponse();
		response.setCodigo(entity.getCodigo());
		response.setDescricao(entity.getDescricao());
		response.setCliente(entity.getCliente());
		response.setVeiculo(entity.getVeiculo());
		response.setStatus(entity.getStatus());
		response.setServicos(servicosResponse);
		response.setPedidoPecas(PedidoPecasMapper.toResponse(entity.getPedidoPecas()));
		response.setValorTotal(entity.getValorTotal());
		response.setDataAbertura(entity.getDataAbertura());
		response.setDataAtualizacao(entity.getDataAtualizacao());
		response.setDataFinalizacao(entity.getDataFinalizacao());
		return response;
	}

}
