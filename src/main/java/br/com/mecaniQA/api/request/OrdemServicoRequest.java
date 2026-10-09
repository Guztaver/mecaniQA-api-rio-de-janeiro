package br.com.mecaniQA.api.request;

import java.util.ArrayList;
import java.util.List;

public class OrdemServicoRequest {

	private String descricao;
	private String cliente;
	private String veiculo;
	private List<Long> servicoIds;
	private Long pedidoPecasId;

	public OrdemServicoRequest() {
		this.servicoIds = new ArrayList<>();
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

	public List<Long> getServicoIds() {
		return servicoIds;
	}

	public void setServicoIds(List<Long> servicoIds) {
		this.servicoIds = servicoIds;
	}

	public Long getPedidoPecasId() {
		return pedidoPecasId;
	}

	public void setPedidoPecasId(Long pedidoPecasId) {
		this.pedidoPecasId = pedidoPecasId;
	}

}
