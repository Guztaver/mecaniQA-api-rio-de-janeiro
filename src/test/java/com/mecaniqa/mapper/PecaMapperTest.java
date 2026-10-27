package com.mecaniqa.mapper;

import com.mecaniqa.model.Peca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PecaMapperTest {

    @Test
    @DisplayName("Deve mapear ResultSet para objeto Peca corretamente")
    void deveMapearResultSetParaPeca() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("nome")).thenReturn("Amortecedor");
        when(rs.getBigDecimal("preco")).thenReturn(new BigDecimal("350.00"));
        when(rs.getInt("quantidade")).thenReturn(8);

        Peca peca = PecaMapper.mapear(rs);

        assertNotNull(peca);
        assertEquals(1, peca.getId());
        assertEquals("Amortecedor", peca.getNome());
        assertEquals(new BigDecimal("350.00"), peca.getPreco());
        assertEquals(8, peca.getQuantidade());
    }

    @Test
    @DisplayName("Deve propagar SQLException quando falhar leitura do ResultSet")
    void devePropagarSQLException() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt("id")).thenThrow(new SQLException("Erro ao acessar coluna id"));

        assertThrows(SQLException.class, () -> PecaMapper.mapear(rs));
    }
}
