package com.mecaniqa.controller;

import com.mecaniqa.exception.DadosInvalidosException;
import com.mecaniqa.exception.PecaNaoEncontradaException;
import com.mecaniqa.model.Peca;
import com.mecaniqa.service.PecaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    private final PecaService service;

    public PecaController(PecaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {

        try {

            Peca peca = service.buscarPorId(id);

            return ResponseEntity.ok(peca);

        } catch (PecaNaoEncontradaException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));

        } catch (DadosInvalidosException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));

        } catch (SQLException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno ao consultar a peça."));
        }
    }
}
