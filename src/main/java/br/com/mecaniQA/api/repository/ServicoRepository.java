package br.com.mecaniQA.api.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.mecaniQA.api.model.Servico;

public class ServicoRepository {

	private static ServicoRepository instance;
	private List<Servico> servicos;
	private Long proximoCodigo;

	private ServicoRepository() {
		servicos = new ArrayList<>();
		proximoCodigo = 1L;
	}

	public static ServicoRepository getInstance() {
		if (instance == null) {
			instance = new ServicoRepository();
		}

		return instance;
	}

	public Servico salvar(Servico servico) {
		LocalDateTime agora = LocalDateTime.now();
		servico.setCodigo(proximoCodigo);
		servico.setDataCriacao(agora);
		servico.setDataAtualizacao(agora);
		servicos.add(servico);
		proximoCodigo++;
		return servico;
	}

	public List<Servico> listar() {
		return servicos;
	}

	public Servico buscarPorCodigo(Long codigo) {
		for (Servico servico : servicos) {
			if (servico.getCodigo().equals(codigo)) {
				return servico;
			}
		}

		return null;
	}

	public Servico atualizar(Servico servico) {
		servico.setDataAtualizacao(LocalDateTime.now());
		return servico;
	}

	public boolean excluir(Long codigo) {
		Servico servico = buscarPorCodigo(codigo);
		if (servico == null) {
			return false;
		}

		servicos.remove(servico);
		return true;
	}

}
