package br.com.mecaniQA.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.mecaniQA.api.model.StatusPedidoPecas;

public class PedidoPecasResponse {

	private Long codigo;
	private String descricao;
	private StatusPedidoPecas status;
	private List<ItemPedidoPecasResponse> itens;
	private BigDecimal valorTotal;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;

	public PedidoPecasResponse() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public StatusPedidoPecas getStatus() {
		return status;
	}

	public void setStatus(StatusPedidoPecas status) {
		this.status = status;
	}

	public List<ItemPedidoPecasResponse> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedidoPecasResponse> itens) {
		this.itens = itens;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

}
