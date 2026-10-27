package com.mecaniqa.service;

import com.mecaniqa.exception.DadosInvalidosException;
import com.mecaniqa.exception.PecaNaoEncontradaException;
import com.mecaniqa.model.Peca;
import com.mecaniqa.repository.PecaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PecaServiceTest {

    private PecaRepository repository;
    private PecaService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PecaRepository.class);
        service = new PecaService(repository);
    }

    @Test
    @DisplayName("Deve lancar DadosInvalidosException quando ID for menor ou igual a zero")
    void deveLancarDadosInvalidosExceptionQuandoIdMenorOuIgualZero() {
        DadosInvalidosException exZero = assertThrows(DadosInvalidosException.class, () -> service.buscarPorId(0));
        assertEquals("O ID da peça deve ser maior que zero.", exZero.getMessage());

        DadosInvalidosException exNegativo = assertThrows(DadosInvalidosException.class, () -> service.buscarPorId(-5));
        assertEquals("O ID da peça deve ser maior que zero.", exNegativo.getMessage());

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve lancar PecaNaoEncontradaException quando peca nao existir no repositorio")
    void deveLancarPecaNaoEncontradaExceptionQuandoNaoExistir() throws SQLException {
        when(repository.buscarPorId(99)).thenReturn(null);

        PecaNaoEncontradaException ex = assertThrows(PecaNaoEncontradaException.class, () -> service.buscarPorId(99));
        assertEquals("Peça não encontrada.", ex.getMessage());

        verify(repository, times(1)).buscarPorId(99);
    }

    @Test
    @DisplayName("Deve retornar Peca quando ID existir")
    void deveRetornarPecaQuandoIdExistir() throws SQLException {
        Peca pecaEsperada = new Peca(1, "Vela de Ignição", new BigDecimal("25.00"), 4);
        when(repository.buscarPorId(1)).thenReturn(pecaEsperada);

        Peca resultado = service.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Vela de Ignição", resultado.getNome());
        assertEquals(new BigDecimal("25.00"), resultado.getPreco());
        assertEquals(4, resultado.getQuantidade());

        verify(repository, times(1)).buscarPorId(1);
    }

    @Test
    @DisplayName("Deve propagar SQLException lancada pelo repositorio")
    void devePropagarSQLExceptionDoRepository() throws SQLException {
        when(repository.buscarPorId(2)).thenThrow(new SQLException("Erro no banco de dados"));

        assertThrows(SQLException.class, () -> service.buscarPorId(2));
        verify(repository, times(1)).buscarPorId(2);
    }
}
