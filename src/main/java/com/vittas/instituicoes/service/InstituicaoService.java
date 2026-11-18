package com.vittas.instituicoes.service;

import com.vittas.dto.instituicao.InstituicaoCreateRequest;
import com.vittas.dto.instituicao.InstituicaoResponse;
import com.vittas.dto.instituicao.InstituicaoUpdateRequest;
import com.vittas.exception.BusinessException;
import com.vittas.mapper.InstituicaoMapper;
import com.vittas.model.Instituicao;
import com.vittas.repository.InstituicaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstituicaoService {

    private final InstituicaoRepository repository;

    public InstituicaoService(InstituicaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public InstituicaoResponse criar(InstituicaoCreateRequest request) {
        if (repository.existsByCnpj(request.cnpj())) {
            throw new BusinessException(
                    "INSTITUICAO_JA_EXISTE",
                    "Já existe uma instituição com este CNPJ."
            );
        }

        Instituicao entity = InstituicaoMapper.toEntity(request);
        return InstituicaoMapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public Page<InstituicaoResponse> listar(String nome, Boolean status, Pageable pageable) {
        Page<Instituicao> page;
        boolean temNome = nome != null && !nome.isBlank();

        if (temNome && status != null) {
            page = repository.findByNomeContainingIgnoreCaseAndStatus(nome.trim(), status, pageable);
        } else if (temNome) {
            page = repository.findByNomeContainingIgnoreCase(nome.trim(), pageable);
        } else if (status != null) {
            page = repository.findByStatus(status, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(InstituicaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public InstituicaoResponse consultar(Long id) {
        return InstituicaoMapper.toResponse(buscar(id));
    }

    @Transactional
    public InstituicaoResponse atualizar(Long id, InstituicaoUpdateRequest request) {
        Instituicao entity = buscar(id);

        if (repository.existsByCnpjAndIdNot(request.cnpj(), id)) {
            throw new BusinessException(
                    "INSTITUICAO_JA_EXISTE",
                    "Já existe outra instituição com este CNPJ."
            );
        }

        InstituicaoMapper.updateEntity(entity, request);
        return InstituicaoMapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void excluir(Long id) {
        Instituicao entity = buscar(id);
        entity.setStatus(false);
        repository.save(entity);
    }

    private Instituicao buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "INSTITUICAO_NAO_ENCONTRADA",
                        "Instituição não encontrada."
                ));
    }
}