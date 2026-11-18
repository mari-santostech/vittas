package com.vittas.instituicoes.repository;

public class InstituicaoRepository {
    package com.vittas.instituicoes.repository;

import com.vittas.model.Instituicao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

        boolean existsByCnpj(String cnpj);

        boolean existsByCnpjAndIdNot(String cnpj, Long id);

        Page<Instituicao> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

        Page<Instituicao> findByStatus(Boolean status, Pageable pageable);

        Page<Instituicao> findByNomeContainingIgnoreCaseAndStatus(
                String nome, Boolean status, Pageable pageable);
    }

}
