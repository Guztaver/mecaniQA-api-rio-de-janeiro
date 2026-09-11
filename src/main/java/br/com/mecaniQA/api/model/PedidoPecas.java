package br.com.mecaniQA.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoPecas {

	private Long codigo;
	private String descricao;
	private StatusPedidoPecas status;
	private List<ItemPedidoPecas> itens;
	private BigDecimal valorTotal;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;

	public PedidoPecas() {
		this.itens = new ArrayList<>();
		this.status = StatusPedidoPecas.ORCANDO;
		this.valorTotal = BigDecimal.ZERO;
	}

	public PedidoPecas(Long codigo, String descricao, StatusPedidoPecas status, List<ItemPedidoPecas> itens,
			BigDecimal valorTotal, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.status = status;
		this.itens = itens != null ? itens : new ArrayList<>();
		this.valorTotal = valorTotal != null ? valorTotal : BigDecimal.ZERO;
		this.dataCriacao = dataCriacao;
		this.dataAtualizacao = dataAtualizacao;
	}

	private PedidoPecas(Builder builder) {
		this.codigo = builder.codigo;
		this.descricao = builder.descricao;
		this.status = builder.status != null ? builder.status : StatusPedidoPecas.ORCANDO;
		this.itens = builder.itens != null ? builder.itens : new ArrayList<>();
		this.valorTotal = builder.valorTotal != null ? builder.valorTotal : calcularValorTotal(this.itens);
		this.dataCriacao = builder.dataCriacao != null ? builder.dataCriacao : LocalDateTime.now();
		this.dataAtualizacao = builder.dataAtualizacao != null ? builder.dataAtualizacao : this.dataCriacao;
	}

	public static Builder builder() {
		return new Builder();
	}

	public void adicionarItem(ItemPedidoPecas item) {
		if (this.itens == null) {
			this.itens = new ArrayList<>();
		}
		this.itens.add(item);
		recalcularValorTotal();
	}

	public void recalcularValorTotal() {
		this.valorTotal = calcularValorTotal(this.itens);
	}

	private static BigDecimal calcularValorTotal(List<ItemPedidoPecas> itens) {
		if (itens == null || itens.isEmpty()) {
			return BigDecimal.ZERO;
		}
		BigDecimal total = BigDecimal.ZERO;
		for (ItemPedidoPecas item : itens) {
			if (item != null && item.getSubtotal() != null) {
				total = total.add(item.getSubtotal());
			}
		}
		return total;
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

	public List<ItemPedidoPecas> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedidoPecas> itens) {
		this.itens = itens;
		recalcularValorTotal();
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

	public static class Builder {
		private Long codigo;
		private String descricao;
		private StatusPedidoPecas status;
		private List<ItemPedidoPecas> itens = new ArrayList<>();
		private BigDecimal valorTotal;
		private LocalDateTime dataCriacao;
		private LocalDateTime dataAtualizacao;

		public Builder codigo(Long codigo) {
			this.codigo = codigo;
			return this;
		}

		public Builder descricao(String descricao) {
			this.descricao = descricao;
			return this;
		}

		public Builder status(StatusPedidoPecas status) {
			this.status = status;
			return this;
		}

		public Builder itens(List<ItemPedidoPecas> itens) {
			this.itens = itens != null ? new ArrayList<>(itens) : new ArrayList<>();
			return this;
		}

		public Builder item(ItemPedidoPecas item) {
			if (this.itens == null) {
				this.itens = new ArrayList<>();
			}
			this.itens.add(item);
			return this;
		}

		public Builder valorTotal(BigDecimal valorTotal) {
			this.valorTotal = valorTotal;
			return this;
		}

		public Builder dataCriacao(LocalDateTime dataCriacao) {
			this.dataCriacao = dataCriacao;
			return this;
		}

		public Builder dataAtualizacao(LocalDateTime dataAtualizacao) {
			this.dataAtualizacao = dataAtualizacao;
			return this;
		}

		public PedidoPecas build() {
			return new PedidoPecas(this);
		}
	}

}
