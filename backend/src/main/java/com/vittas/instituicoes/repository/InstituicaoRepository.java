package com.vittas.instituicoes.repository;

import com.vittas.instituicoes.model.Instituicao;
import com.vittas.instituicoes.model.StatusInstituicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdInstituicaoNot(String cnpj, Long idInstituicao);

    /**
     * Busca por nome fantasia, razao social ou CNPJ (parcial) e, opcionalmente, por status.
     * Parametros nulos sao ignorados no filtro.
     */
    @Query("""
            SELECT i FROM Instituicao i
            WHERE (:status IS NULL OR i.status = :status)
              AND (:termo IS NULL
                   OR LOWER(i.nomeFantasia) LIKE LOWER(CONCAT('%', :termo, '%'))
                   OR LOWER(i.razaoSocial) LIKE LOWER(CONCAT('%', :termo, '%'))
                   OR (:cnpj IS NOT NULL AND i.cnpj LIKE CONCAT('%', :cnpj, '%')))
            ORDER BY i.nomeFantasia
            """)
    List<Instituicao> pesquisar(@Param("termo") String termo,
                                @Param("cnpj") String cnpj,
                                @Param("status") StatusInstituicao status);
}
