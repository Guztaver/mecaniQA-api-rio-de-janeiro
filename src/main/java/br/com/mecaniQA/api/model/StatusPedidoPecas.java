package br.com.mecaniQA.api.model;

public enum StatusPedidoPecas {

	ORCANDO("Orçando"),
	PENDENTE_PAGAMENTO("Pendente de Pagamento"),
	PAGO_FATURADO("Pago/Faturado"),
	ENTREGUE("Entregue");

	private final String descricao;

	StatusPedidoPecas(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
