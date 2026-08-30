package br.com.mecaniQA.api.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.mecaniQA.api.model.Peca;

public class PecaRepository {

	private static PecaRepository instance;
	private List<Peca> pecas;
	private Long proximoCodigo;

	private PecaRepository() {
		pecas = new ArrayList<>();
		proximoCodigo = 1L;
	}

	public static PecaRepository getInstance() {
		if (instance == null) {
			instance = new PecaRepository();
		}

		return instance;
	}

	public Peca salvar(Peca peca) {
		LocalDateTime agora = LocalDateTime.now();
		peca.setCodigo(proximoCodigo);
		peca.setDataCadastro(agora);
		peca.setDataAtualizacao(agora);
		pecas.add(peca);
		proximoCodigo++;
		return peca;
	}

	public List<Peca> listar() {
		return pecas;
	}

	public Peca buscarPorCodigo(Long codigo) {
		for (Peca peca : pecas) {
			if (peca.getCodigo().equals(codigo)) {
				return peca;
			}
		}

		return null;
	}

	public Peca atualizar(Peca peca) {
		peca.setDataAtualizacao(LocalDateTime.now());
		return peca;
	}

	public boolean excluir(Long codigo) {
		Peca peca = buscarPorCodigo(codigo);
		if (peca == null) {
			return false;
		}

		pecas.remove(peca);
		return true;
	}

}
