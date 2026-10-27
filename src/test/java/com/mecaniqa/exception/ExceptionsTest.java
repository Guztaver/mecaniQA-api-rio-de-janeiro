package com.mecaniqa.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

    @Test
    @DisplayName("PecaNaoEncontradaException deve carregar mensagem correta")
    void deveCriarPecaNaoEncontradaException() {
        PecaNaoEncontradaException ex = new PecaNaoEncontradaException("Peça não encontrada.");
        assertEquals("Peça não encontrada.", ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    @DisplayName("OrdemServicoNaoEncontradaException deve carregar mensagem correta")
    void deveCriarOrdemServicoNaoEncontradaException() {
        OrdemServicoNaoEncontradaException ex = new OrdemServicoNaoEncontradaException("Ordem de serviço não encontrada.");
        assertEquals("Ordem de serviço não encontrada.", ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    @DisplayName("DadosInvalidosException deve carregar mensagem correta")
    void deveCriarDadosInvalidosException() {
        DadosInvalidosException ex = new DadosInvalidosException("O ID da peça deve ser maior que zero.");
        assertEquals("O ID da peça deve ser maior que zero.", ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }
}
