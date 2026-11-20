
package com.vittas.instituicoes.service;

import com.vittas.instituicoes.dto.instituicao.InstituicaoCreateRequest;
import com.vittas.instituicoes.dto.instituicao.InstituicaoResponse;
import com.vittas.instituicoes.dto.instituicao.InstituicaoUpdateRequest;
import com.vittas.instituicoes.exception.BusinessException;
import com.vittas.instituicoes.mapper.InstituicaoMapper;
import com.vittas.instituicoes.model.Instituicao;
import com.vittas.instituicoes.repository.InstituicaoRepository;

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

        String cnpj = request.cnpj().trim();

        if (repository.existsByCnpj(cnpj)) {
            throw new BusinessException(
                    "INSTITUICAO_JA_EXISTE",
                    "Já existe uma instituição com este CNPJ."
            );
        }

        Instituicao entity = InstituicaoMapper.toEntity(request);

        return InstituicaoMapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public Page<InstituicaoResponse> listar(
            String nome,
            String status,
            Pageable pageable) {

        String filtroNome = nome == null || nome.isBlank()
                ? null
                : nome.trim();

        String filtroStatus = status == null || status.isBlank()
                ? null
                : status.trim().toUpperCase();

        Page<Instituicao> page =
                repository.buscarPorNomeOuRazaoSocialEStatus(
                        filtroNome,
                        filtroStatus,
                        pageable
                );

        return page.map(InstituicaoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public InstituicaoResponse consultar(Long id) {
        return InstituicaoMapper.toResponse(buscar(id));
    }

    @Transactional
    public InstituicaoResponse atualizar(
            Long id,
            InstituicaoUpdateRequest request) {

        Instituicao entity = buscar(id);

        String cnpj = request.cnpj().trim();

        if (repository.existsByCnpjAndIdInstituicaoNot(cnpj, id)) {
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

        entity.setStatus("INATIVA");

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
