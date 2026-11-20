package com.vittas.instituicoes.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class BusinessException extends RuntimeException {

    private final String codigo;
    private final HttpStatus status;
    private final Map<String, String> campos;

    public BusinessException(String codigo, String mensagem) {
        this(codigo, mensagem, HttpStatus.BAD_REQUEST, Map.of());
    }

    public BusinessException(String codigo, String mensagem, HttpStatus status, Map<String, String> campos) {
        super(mensagem);
        this.codigo = codigo;
        this.status = status;
        this.campos = campos;
    }

    public static BusinessException campoInvalido(String codigo, HttpStatus status, String campo, String mensagem) {
        return new BusinessException(codigo, mensagem, status, Map.of(campo, mensagem));
    }

    public String getCodigo() {
        return codigo;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getCampos() {
        return campos;
    }
}
