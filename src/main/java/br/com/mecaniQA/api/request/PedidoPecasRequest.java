package br.com.mecaniQA.api.request;

import java.util.ArrayList;
import java.util.List;

public class PedidoPecasRequest {

	private String descricao;
	private List<ItemPedidoPecasRequest> itens;

	public PedidoPecasRequest() {
		this.itens = new ArrayList<>();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<ItemPedidoPecasRequest> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedidoPecasRequest> itens) {
		this.itens = itens;
	}

}
