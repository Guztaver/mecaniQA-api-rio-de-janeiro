package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.model.ItemPedidoPecas;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.request.ItemPedidoPecasRequest;
import br.com.mecaniQA.api.response.ItemPedidoPecasResponse;

public class ItemPedidoPecasMapper {

	public static ItemPedidoPecas toEntity(ItemPedidoPecasRequest request, Peca peca) {
		if (request == null) {
			return null;
		}

		return ItemPedidoPecas.builder()
				.peca(peca)
				.quantidade(request.getQuantidade())
				.precoUnitario(peca != null ? peca.getPrecoVenda() : null)
				.build();
	}

	public static ItemPedidoPecasResponse toResponse(ItemPedidoPecas entity) {
		if (entity == null) {
			return null;
		}

		ItemPedidoPecasResponse response = new ItemPedidoPecasResponse();
		response.setCodigo(entity.getCodigo());
		response.setPeca(PecaMapper.toResponse(entity.getPeca()));
		response.setQuantidade(entity.getQuantidade());
		response.setPrecoUnitario(entity.getPrecoUnitario());
		response.setSubtotal(entity.getSubtotal());
		return response;
	}

}
