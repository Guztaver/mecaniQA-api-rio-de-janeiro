package com.mecaniqa.service;

import com.mecaniqa.exception.DadosInvalidosException;
import com.mecaniqa.exception.PecaNaoEncontradaException;
import com.mecaniqa.model.Peca;
import com.mecaniqa.repository.PecaRepository;

import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class PecaService {

    private final PecaRepository repository;

    public PecaService(PecaRepository repository) {
        this.repository = repository;
    }

    public Peca buscarPorId(int id) throws SQLException {

        if (id <= 0) {
            throw new DadosInvalidosException(
                "O ID da peça deve ser maior que zero."
            );
        }

        Peca peca = repository.buscarPorId(id);

        if (peca == null) {
            throw new PecaNaoEncontradaException(
                "Peça não encontrada."
            );
        }

        return peca;
    }
}
