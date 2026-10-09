package br.com.mecaniQA.api.request;

import br.com.mecaniQA.api.model.StatusOrdemServico;

public class StatusOrdemServicoRequest {

	private StatusOrdemServico status;

	public StatusOrdemServicoRequest() {
	}

	public StatusOrdemServicoRequest(StatusOrdemServico status) {
		this.status = status;
	}

	public StatusOrdemServico getStatus() {
		return status;
	}

	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}

}
