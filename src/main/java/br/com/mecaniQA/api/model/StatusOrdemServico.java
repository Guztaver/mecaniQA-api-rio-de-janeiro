package br.com.mecaniQA.api.model;

public enum StatusOrdemServico {

	ABERTO("Aberto"),
	PENDENTE_PAGAMENTO("Pendente de Pagamento"),
	PAGO("Pago"),
	EM_EXECUCAO("Em Execução"),
	EXECUTADO("Executado");

	private final String descricao;

	StatusOrdemServico(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
