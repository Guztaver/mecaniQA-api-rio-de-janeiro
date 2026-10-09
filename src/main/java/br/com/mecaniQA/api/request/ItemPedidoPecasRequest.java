package br.com.mecaniQA.api.request;

public class ItemPedidoPecasRequest {

	private Long pecaId;
	private Integer quantidade;

	public ItemPedidoPecasRequest() {
	}

	public ItemPedidoPecasRequest(Long pecaId, Integer quantidade) {
		this.pecaId = pecaId;
		this.quantidade = quantidade;
	}

	public Long getPecaId() {
		return pecaId;
	}

	public void setPecaId(Long pecaId) {
		this.pecaId = pecaId;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

}
