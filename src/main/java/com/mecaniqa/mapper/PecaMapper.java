package com.mecaniqa.mapper;

import com.mecaniqa.model.Peca;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PecaMapper {

    public static Peca mapear(ResultSet rs) throws SQLException {

        Peca peca = new Peca();

        peca.setId(rs.getInt("id"));
        peca.setNome(rs.getString("nome"));
        peca.setPreco(rs.getBigDecimal("preco"));
        peca.setQuantidade(rs.getInt("quantidade"));

        return peca;
    }
}
