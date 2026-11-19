package com.vittas.instituicoes.controller;

import com.vittas.instituicoes.dto.instituicao.InstituicaoRequest;
import com.vittas.instituicoes.dto.instituicao.InstituicaoResponse;
import com.vittas.instituicoes.model.StatusInstituicao;
import com.vittas.instituicoes.service.InstituicaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoController {

    private final InstituicaoService service;

    public InstituicaoController(InstituicaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InstituicaoResponse> criar(@Valid @RequestBody InstituicaoRequest request) {
        InstituicaoResponse criada = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.id())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping
    public List<InstituicaoResponse> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) StatusInstituicao status) {
        return service.listar(busca, status);
    }

    @GetMapping("/{id}")
    public InstituicaoResponse consultar(@PathVariable Long id) {
        return service.consultar(id);
    }

    @PutMapping("/{id}")
    public InstituicaoResponse atualizar(@PathVariable Long id,
                                         @Valid @RequestBody InstituicaoRequest request) {
        return service.atualizar(id, request);
    }

    /**
     * Exclusao logica: marca a instituicao como INATIVA.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/ativar")
    public InstituicaoResponse ativar(@PathVariable Long id) {
        return service.ativar(id);
    }
}
