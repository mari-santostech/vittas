package com.vittas.instituicoes.service;

import com.vittas.instituicoes.dto.instituicao.InstituicaoRequest;
import com.vittas.instituicoes.dto.instituicao.InstituicaoResponse;
import com.vittas.instituicoes.exception.BusinessException;
import com.vittas.instituicoes.exception.RecursoNaoEncontradoException;
import com.vittas.instituicoes.mapper.InstituicaoMapper;
import com.vittas.instituicoes.model.Instituicao;
import com.vittas.instituicoes.model.StatusInstituicao;
import com.vittas.instituicoes.repository.InstituicaoRepository;
import com.vittas.instituicoes.util.DocumentoUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstituicaoService {

    private final InstituicaoRepository repository;

    public InstituicaoService(InstituicaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public InstituicaoResponse criar(InstituicaoRequest request) {
        String cnpj = validarCnpj(request.cnpj());

        if (repository.existsByCnpj(cnpj)) {
            throw cnpjDuplicado();
        }

        Instituicao entity = InstituicaoMapper.toEntity(request);
        return InstituicaoMapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<InstituicaoResponse> listar(String busca, StatusInstituicao status) {
        String termo = (busca == null || busca.isBlank()) ? null : busca.trim();
        String cnpj = termo == null ? null : DocumentoUtil.somenteDigitos(termo);
        if (cnpj != null && cnpj.isEmpty()) {
            cnpj = null;
        }

        return repository.pesquisar(termo, cnpj, status)
                .stream()
                .map(InstituicaoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InstituicaoResponse consultar(Long id) {
        return InstituicaoMapper.toResponse(buscar(id));
    }

    @Transactional
    public InstituicaoResponse atualizar(Long id, InstituicaoRequest request) {
        Instituicao entity = buscar(id);
        String cnpj = validarCnpj(request.cnpj());

        if (repository.existsByCnpjAndIdInstituicaoNot(cnpj, id)) {
            throw cnpjDuplicado();
        }

        InstituicaoMapper.updateEntity(entity, request);
        return InstituicaoMapper.toResponse(repository.save(entity));
    }

    /**
     * Exclusao logica: a instituicao continua cadastrada, mas passa a constar como INATIVA.
     */
    @Transactional
    public InstituicaoResponse desativar(Long id) {
        Instituicao entity = buscar(id);
        entity.desativar();
        return InstituicaoMapper.toResponse(repository.save(entity));
    }

    @Transactional
    public InstituicaoResponse ativar(Long id) {
        Instituicao entity = buscar(id);
        entity.ativar();
        return InstituicaoMapper.toResponse(repository.save(entity));
    }

    private Instituicao buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instituição não encontrada."));
    }

    private String validarCnpj(String cnpj) {
        if (!DocumentoUtil.cnpjValido(cnpj)) {
            throw BusinessException.campoInvalido(
                    "CNPJ_INVALIDO", HttpStatus.BAD_REQUEST, "cnpj", "CNPJ inválido.");
        }
        return DocumentoUtil.somenteDigitos(cnpj);
    }

    private BusinessException cnpjDuplicado() {
        return BusinessException.campoInvalido(
                "INSTITUICAO_JA_EXISTE", HttpStatus.CONFLICT, "cnpj",
                "Já existe uma instituição cadastrada com este CNPJ.");
    }
}
