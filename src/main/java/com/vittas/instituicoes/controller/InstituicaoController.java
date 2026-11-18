package com.vittas.instituicoes.controller;

import com.vittas.dto.instituicao.InstituicaoCreateRequest;
import com.vittas.dto.instituicao.InstituicaoResponse;
import com.vittas.dto.instituicao.InstituicaoUpdateRequest;
import com.vittas.service.InstituicaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instituicoes")
@Validated
public class InstituicaoController {

    private final InstituicaoService service;

    public InstituicaoController(InstituicaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InstituicaoResponse> criar(
            @Valid @RequestBody InstituicaoCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(request));
    }

    @GetMapping
    public ResponseEntity<Page<InstituicaoResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean status,
            Pageable pageable) {

        return ResponseEntity.ok(service.listar(nome, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoResponse> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(service.consultar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstituicaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody InstituicaoUpdateRequest request) {

        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}