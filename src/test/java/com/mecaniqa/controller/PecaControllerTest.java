package com.mecaniqa.controller;

import com.mecaniqa.exception.DadosInvalidosException;
import com.mecaniqa.exception.PecaNaoEncontradaException;
import com.mecaniqa.model.Peca;
import com.mecaniqa.service.PecaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PecaControllerTest {

    private PecaService service;
    private PecaController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(PecaService.class);
        controller = new PecaController(service);
    }

    @Test
    @DisplayName("Deve retornar 200 OK com corpo da peca quando busca for bem-sucedida")
    void deveRetornar200OkQuandoEncontrarPeca() throws SQLException {
        Peca peca = new Peca(1, "Disco de Freio", new BigDecimal("200.00"), 4);
        when(service.buscarPorId(1)).thenReturn(peca);

        ResponseEntity<?> response = controller.buscarPorId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Peca);
        Peca body = (Peca) response.getBody();
        assertEquals(1, body.getId());
        assertEquals("Disco de Freio", body.getNome());
    }

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND com mensagem de erro quando PecaNaoEncontradaException for lancada")
    void deveRetornar404QuandoPecaNaoEncontrada() throws SQLException {
        when(service.buscarPorId(99)).thenThrow(new PecaNaoEncontradaException("Peça não encontrada."));

        ResponseEntity<?> response = controller.buscarPorId(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map<?, ?>);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Peça não encontrada.", body.get("erro"));
    }

    @Test
    @DisplayName("Deve retornar 400 BAD REQUEST com mensagem de erro quando DadosInvalidosException for lancada")
    void deveRetornar400QuandoDadosInvalidos() throws SQLException {
        when(service.buscarPorId(-1)).thenThrow(new DadosInvalidosException("O ID da peça deve ser maior que zero."));

        ResponseEntity<?> response = controller.buscarPorId(-1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map<?, ?>);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("O ID da peça deve ser maior que zero.", body.get("erro"));
    }

    @Test
    @DisplayName("Deve retornar 500 INTERNAL SERVER ERROR quando SQLException for lancada")
    void deveRetornar500QuandoSQLException() throws SQLException {
        when(service.buscarPorId(5)).thenThrow(new SQLException("Falha na conexão"));

        ResponseEntity<?> response = controller.buscarPorId(5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map<?, ?>);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Erro interno ao consultar a peça.", body.get("erro"));
    }
}
