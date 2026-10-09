package br.com.mecaniQA.api.response;

import java.math.BigDecimal;

public class ItemPedidoPecasResponse {

	private Long codigo;
	private PecaResponse peca;
	private Integer quantidade;
	private BigDecimal precoUnitario;
	private BigDecimal subtotal;

	public ItemPedidoPecasResponse() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public PecaResponse getPeca() {
		return peca;
	}

	public void setPeca(PecaResponse peca) {
		this.peca = peca;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

}
