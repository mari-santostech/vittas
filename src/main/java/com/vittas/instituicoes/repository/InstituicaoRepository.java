package com.vittas.instituicoes.repository;

import com.vittas.instituicoes.model.Instituicao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdInstituicaoNot(String cnpj, Long idInstituicao);

    Page<Instituicao> findByRazaoSocialContainingIgnoreCase(
            String razaoSocial,
            Pageable pageable
    );

    Page<Instituicao> findByNomeFantasiaContainingIgnoreCase(
            String nomeFantasia,
            Pageable pageable
    );

    Page<Instituicao> findByStatus(
            String status,
            Pageable pageable
    );
}
