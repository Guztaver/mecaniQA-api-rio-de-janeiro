package br.com.mecaniQA.api.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.mecaniQA.api.model.OrdemServico;

public class OrdemServicoRepository {

	private static OrdemServicoRepository instance;
	private List<OrdemServico> ordensServico;
	private Long proximoCodigo;

	private OrdemServicoRepository() {
		ordensServico = new ArrayList<>();
		proximoCodigo = 1L;
	}

	public static OrdemServicoRepository getInstance() {
		if (instance == null) {
			instance = new OrdemServicoRepository();
		}
		return instance;
	}

	public OrdemServico salvar(OrdemServico os) {
		LocalDateTime agora = LocalDateTime.now();
		os.setCodigo(proximoCodigo);
		os.setDataAbertura(agora);
		os.setDataAtualizacao(agora);
		ordensServico.add(os);
		proximoCodigo++;
		return os;
	}

	public List<OrdemServico> listar() {
		return ordensServico;
	}

	public OrdemServico buscarPorCodigo(Long codigo) {
		for (OrdemServico os : ordensServico) {
			if (os.getCodigo().equals(codigo)) {
				return os;
			}
		}
		return null;
	}

	public OrdemServico atualizar(OrdemServico os) {
		os.setDataAtualizacao(LocalDateTime.now());
		return os;
	}

	public boolean excluir(Long codigo) {
		OrdemServico os = buscarPorCodigo(codigo);
		if (os == null) {
			return false;
		}
		ordensServico.remove(os);
		return true;
	}

	public void reset() {
		ordensServico.clear();
		proximoCodigo = 1L;
	}

}
