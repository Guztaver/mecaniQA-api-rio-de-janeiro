package br.com.mecaniQA.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

	private Long codigo;
	private String descricao;
	private String cliente;
	private String veiculo;
	private StatusOrdemServico status;
	private List<Servico> servicos;
	private PedidoPecas pedidoPecas;
	private BigDecimal valorTotal;
	private LocalDateTime dataAbertura;
	private LocalDateTime dataAtualizacao;
	private LocalDateTime dataFinalizacao;


	private OrdemServico(Builder builder) {
		this.codigo = builder.codigo;
		this.descricao = builder.descricao;
		this.cliente = builder.cliente;
		this.veiculo = builder.veiculo;
		this.status = builder.status != null ? builder.status : StatusOrdemServico.ABERTO;
		this.servicos = builder.servicos != null ? builder.servicos : new ArrayList<>();
		this.pedidoPecas = builder.pedidoPecas;
		this.valorTotal = builder.valorTotal != null ? builder.valorTotal : calcularValorTotal(this.servicos, this.pedidoPecas);
		this.dataAbertura = builder.dataAbertura != null ? builder.dataAbertura : LocalDateTime.now();
		this.dataAtualizacao = builder.dataAtualizacao != null ? builder.dataAtualizacao : this.dataAbertura;
		this.dataFinalizacao = builder.dataFinalizacao;
	}

	public static Builder builder() {
		return new Builder();
	}

	public void adicionarServico(Servico servico) {
		if (this.servicos == null) {
			this.servicos = new ArrayList<>();
		}
		this.servicos.add(servico);
		recalcularValorTotal();
	}

	public void recalcularValorTotal() {
		this.valorTotal = calcularValorTotal(this.servicos, this.pedidoPecas);
	}

	private static BigDecimal calcularValorTotal(List<Servico> servicos, PedidoPecas pedidoPecas) {
		BigDecimal total = BigDecimal.ZERO;
		if (servicos != null) {
			for (Servico servico : servicos) {
				if (servico != null && servico.getCustoTabelado() != null) {
					total = total.add(servico.getCustoTabelado());
				}
			}
		}
		if (pedidoPecas != null && pedidoPecas.getValorTotal() != null) {
			total = total.add(pedidoPecas.getValorTotal());
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

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}

	public StatusOrdemServico getStatus() {
		return status;
	}

	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}

	public List<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
		recalcularValorTotal();
	}

	public PedidoPecas getPedidoPecas() {
		return pedidoPecas;
	}

	public void setPedidoPecas(PedidoPecas pedidoPecas) {
		this.pedidoPecas = pedidoPecas;
		recalcularValorTotal();
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public LocalDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public static class Builder {
		private Long codigo;
		private String descricao;
		private String cliente;
		private String veiculo;
		private StatusOrdemServico status;
		private List<Servico> servicos = new ArrayList<>();
		private PedidoPecas pedidoPecas;
		private BigDecimal valorTotal;
		private LocalDateTime dataAbertura;
		private LocalDateTime dataAtualizacao;
		private LocalDateTime dataFinalizacao;

		public Builder codigo(Long codigo) {
			this.codigo = codigo;
			return this;
		}

		public Builder descricao(String descricao) {
			this.descricao = descricao;
			return this;
		}

		public Builder cliente(String cliente) {
			this.cliente = cliente;
			return this;
		}

		public Builder veiculo(String veiculo) {
			this.veiculo = veiculo;
			return this;
		}

		public Builder status(StatusOrdemServico status) {
			this.status = status;
			return this;
		}

		public Builder servicos(List<Servico> servicos) {
			this.servicos = servicos != null ? new ArrayList<>(servicos) : new ArrayList<>();
			return this;
		}

		public Builder servico(Servico servico) {
			if (this.servicos == null) {
				this.servicos = new ArrayList<>();
			}
			this.servicos.add(servico);
			return this;
		}

		public Builder pedidoPecas(PedidoPecas pedidoPecas) {
			this.pedidoPecas = pedidoPecas;
			return this;
		}

		public Builder valorTotal(BigDecimal valorTotal) {
			this.valorTotal = valorTotal;
			return this;
		}

		public Builder dataAbertura(LocalDateTime dataAbertura) {
			this.dataAbertura = dataAbertura;
			return this;
		}

		public Builder dataAtualizacao(LocalDateTime dataAtualizacao) {
			this.dataAtualizacao = dataAtualizacao;
			return this;
		}

		public Builder dataFinalizacao(LocalDateTime dataFinalizacao) {
			this.dataFinalizacao = dataFinalizacao;
			return this;
		}

		public OrdemServico build() {
			return new OrdemServico(this);
		}
	}

}
