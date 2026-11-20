package com.vittas.instituicoes.exception;

import java.util.Map;

/**
 * Corpo padrao de erro da API.
 * {@code campos} traz o erro de cada campo invalido (nome do campo -> mensagem).
 */
public record ErroResponse(String codigo, String mensagem, Map<String, String> campos) {

    public ErroResponse(String codigo, String mensagem) {
        this(codigo, mensagem, Map.of());
    }
}
