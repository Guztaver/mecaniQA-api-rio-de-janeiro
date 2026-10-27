package com.mecaniqa.repository;

import com.mecaniqa.config.DatabaseConnection;
import com.mecaniqa.mapper.PecaMapper;
import com.mecaniqa.model.Peca;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PecaRepository {

    public Peca buscarPorId(int id) throws SQLException {

        String sql = "SELECT id, nome, preco, quantidade FROM peca WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return PecaMapper.mapear(rs);
                }

                return null;
            }
        }
    }
}
