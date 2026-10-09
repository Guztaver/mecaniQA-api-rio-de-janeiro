package br.com.mecaniQA.api.request;

import br.com.mecaniQA.api.model.StatusPedidoPecas;

public class StatusPedidoPecasRequest {

	private StatusPedidoPecas status;

	public StatusPedidoPecasRequest() {
	}

	public StatusPedidoPecasRequest(StatusPedidoPecas status) {
		this.status = status;
	}

	public StatusPedidoPecas getStatus() {
		return status;
	}

	public void setStatus(StatusPedidoPecas status) {
		this.status = status;
	}

}
