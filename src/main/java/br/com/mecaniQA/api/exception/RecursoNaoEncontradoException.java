package br.com.mecaniQA.api.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
	public RecursoNaoEncontradoException(String recurso, Long codigo) {
		super(recurso + " não encontrado(a) para o código " + codigo + ".");
	}
}
