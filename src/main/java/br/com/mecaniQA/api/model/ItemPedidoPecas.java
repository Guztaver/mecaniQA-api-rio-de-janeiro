package br.com.mecaniQA.api.model;

import java.math.BigDecimal;

public class ItemPedidoPecas {

	private Long codigo;
	private Peca peca;
	private Integer quantidade;
	private BigDecimal precoUnitario;

	public ItemPedidoPecas() {
	}

	public ItemPedidoPecas(Long codigo, Peca peca, Integer quantidade, BigDecimal precoUnitario) {
		this.codigo = codigo;
		this.peca = peca;
		this.quantidade = quantidade;
		this.precoUnitario = precoUnitario;
	}

	private ItemPedidoPecas(Builder builder) {
		this.codigo = builder.codigo;
		this.peca = builder.peca;
		this.quantidade = builder.quantidade;
		this.precoUnitario = builder.precoUnitario;
	}

	public static Builder builder() {
		return new Builder();
	}

	public BigDecimal getSubtotal() {
		if (precoUnitario == null || quantidade == null) {
			return BigDecimal.ZERO;
		}
		return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Peca getPeca() {
		return peca;
	}

	public void setPeca(Peca peca) {
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

	public static class Builder {
		private Long codigo;
		private Peca peca;
		private Integer quantidade;
		private BigDecimal precoUnitario;

		public Builder codigo(Long codigo) {
			this.codigo = codigo;
			return this;
		}

		public Builder peca(Peca peca) {
			this.peca = peca;
			if (peca != null && this.precoUnitario == null) {
				this.precoUnitario = peca.getPrecoVenda();
			}
			return this;
		}

		public Builder quantidade(Integer quantidade) {
			this.quantidade = quantidade;
			return this;
		}

		public Builder precoUnitario(BigDecimal precoUnitario) {
			this.precoUnitario = precoUnitario;
			return this;
		}

		public ItemPedidoPecas build() {
			return new ItemPedidoPecas(this);
		}
	}

}
