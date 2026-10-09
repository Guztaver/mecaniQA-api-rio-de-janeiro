package br.com.mecaniQA.api.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.mecaniQA.api.model.ItemPedidoPecas;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.request.PedidoPecasRequest;
import br.com.mecaniQA.api.response.ItemPedidoPecasResponse;
import br.com.mecaniQA.api.response.PedidoPecasResponse;

public class PedidoPecasMapper {

	public static PedidoPecas toEntity(PedidoPecasRequest request, List<ItemPedidoPecas> itens) {
		if (request == null) {
			return null;
		}

		return PedidoPecas.builder()
				.descricao(request.getDescricao())
				.itens(itens)
				.build();
	}

	public static PedidoPecasResponse toResponse(PedidoPecas entity) {
		if (entity == null) {
			return null;
		}

		List<ItemPedidoPecasResponse> itensResponse = new ArrayList<>();
		if (entity.getItens() != null) {
			for (ItemPedidoPecas item : entity.getItens()) {
				itensResponse.add(ItemPedidoPecasMapper.toResponse(item));
			}
		}

		PedidoPecasResponse response = new PedidoPecasResponse();
		response.setCodigo(entity.getCodigo());
		response.setDescricao(entity.getDescricao());
		response.setStatus(entity.getStatus());
		response.setItens(itensResponse);
		response.setValorTotal(entity.getValorTotal());
		response.setDataCriacao(entity.getDataCriacao());
		response.setDataAtualizacao(entity.getDataAtualizacao());
		return response;
	}

}
