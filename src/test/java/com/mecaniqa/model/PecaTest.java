package com.mecaniqa.model;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PecaTest {

    @Test
    @DisplayName("Deve instanciar Peca usando construtor padrao e acessar campos via getters e setters")
    void deveInstanciarComConstrutorPadrao() {
        Peca peca = new Peca();
        peca.setId(10);
        peca.setNome("Pastilha de Freio");
        peca.setPreco(new BigDecimal("120.50"));
        peca.setQuantidade(5);

        assertEquals(10, peca.getId());
        assertEquals("Pastilha de Freio", peca.getNome());
        assertEquals(new BigDecimal("120.50"), peca.getPreco());
        assertEquals(5, peca.getQuantidade());
    }

    @Test
    @DisplayName("Deve instanciar Peca usando construtor parametrizado")
    void deveInstanciarComConstrutorParametrizado() {
        Peca peca = new Peca(20, "Filtro de Óleo", new BigDecimal("45.00"), 12);

        assertEquals(20, peca.getId());
        assertEquals("Filtro de Óleo", peca.getNome());
        assertEquals(new BigDecimal("45.00"), peca.getPreco());
        assertEquals(12, peca.getQuantidade());
    }
}
