package com.vittas.instituicoes.model;

public class Instituicao {
    package com.vittas.model;

import jakarta.persistence.*;

    @Entity
    @Table(name = "instituicao")
    public class Instituicao {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 150)
        private String nome;

        @Column(nullable = false, unique = true, length = 14)
        private String cnpj;

        @Column(length = 255)
        private String endereco;

        @Column(length = 20)
        private String telefone;

        @Column(length = 150)
        private String email;

        @Column(nullable = false)
        private Boolean status;

        @PrePersist
        void prePersist() {
            if (status == null) {
                status = true;
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getCnpj() { return cnpj; }
        public void setCnpj(String cnpj) { this.cnpj = cnpj; }

        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }

        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Boolean getStatus() { return status; }
        public void setStatus(Boolean status) { this.status = status; }
    }

}
