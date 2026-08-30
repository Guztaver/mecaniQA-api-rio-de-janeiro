package br.com.mecaniQA.api.request;

import java.math.BigDecimal;

import br.com.mecaniQA.api.model.CategoriaPeca;

public class PecaRequest {

	private String codigoBarras;
	private String fornecedor;
	private Integer quantidadeEstoque;
	private BigDecimal precoCusto;
	private BigDecimal precoVenda;
	private String tamanho;
	private String cor;
	private CategoriaPeca categoria;

	public PecaRequest() {
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public CategoriaPeca getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaPeca categoria) {
		this.categoria = categoria;
	}

}
