package br.com.mecaniQA.api.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.mecaniQA.api.model.PedidoPecas;

public class PedidoPecasRepository {

	private static PedidoPecasRepository instance;
	private List<PedidoPecas> pedidos;
	private Long proximoCodigo;

	private PedidoPecasRepository() {
		pedidos = new ArrayList<>();
		proximoCodigo = 1L;
	}

	public static PedidoPecasRepository getInstance() {
		if (instance == null) {
			instance = new PedidoPecasRepository();
		}
		return instance;
	}

	public PedidoPecas salvar(PedidoPecas pedido) {
		LocalDateTime agora = LocalDateTime.now();
		pedido.setCodigo(proximoCodigo);
		pedido.setDataCriacao(agora);
		pedido.setDataAtualizacao(agora);
		pedidos.add(pedido);
		proximoCodigo++;
		return pedido;
	}

	public List<PedidoPecas> listar() {
		return pedidos;
	}

	public PedidoPecas buscarPorCodigo(Long codigo) {
		for (PedidoPecas pedido : pedidos) {
			if (pedido.getCodigo().equals(codigo)) {
				return pedido;
			}
		}
		return null;
	}

	public PedidoPecas atualizar(PedidoPecas pedido) {
		pedido.setDataAtualizacao(LocalDateTime.now());
		return pedido;
	}

	public boolean excluir(Long codigo) {
		PedidoPecas pedido = buscarPorCodigo(codigo);
		if (pedido == null) {
			return false;
		}
		pedidos.remove(pedido);
		return true;
	}

	public void reset() {
		pedidos.clear();
		proximoCodigo = 1L;
	}

}
