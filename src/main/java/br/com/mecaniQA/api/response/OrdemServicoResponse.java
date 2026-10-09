package br.com.mecaniQA.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.mecaniQA.api.model.StatusOrdemServico;

public class OrdemServicoResponse {

	private Long codigo;
	private String descricao;
	private String cliente;
	private String veiculo;
	private StatusOrdemServico status;
	private List<ServicoResponse> servicos;
	private PedidoPecasResponse pedidoPecas;
	private BigDecimal valorTotal;
	private LocalDateTime dataAbertura;
	private LocalDateTime dataAtualizacao;
	private LocalDateTime dataFinalizacao;

	public OrdemServicoResponse() {
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

	public List<ServicoResponse> getServicos() {
		return servicos;
	}

	public void setServicos(List<ServicoResponse> servicos) {
		this.servicos = servicos;
	}

	public PedidoPecasResponse getPedidoPecas() {
		return pedidoPecas;
	}

	public void setPedidoPecas(PedidoPecasResponse pedidoPecas) {
		this.pedidoPecas = pedidoPecas;
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

}
