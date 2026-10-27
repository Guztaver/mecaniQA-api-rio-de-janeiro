package com.mecaniqa.exception;

public class OrdemServicoNaoEncontradaException extends RuntimeException {

    public OrdemServicoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
