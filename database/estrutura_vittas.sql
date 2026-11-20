CREATE DATABASE IF NOT EXISTS vittas_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE vittas_db;

-- CNPJ armazenado somente com os 14 digitos (a mascara e aplicada no frontend).
-- Status: 'ATIVA' ou 'INATIVA' (exclusao logica: a instituicao e desativada, nao apagada).
CREATE TABLE IF NOT EXISTS instituicao (
    id_instituicao BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cnpj           VARCHAR(14)  NOT NULL,
    razao_social   VARCHAR(150) NOT NULL,
    nome_fantasia  VARCHAR(150) NOT NULL,
    endereco       VARCHAR(200) NOT NULL,
    telefone       VARCHAR(20)  NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'ATIVA',
    data_cadastro  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_instituicao_cnpj UNIQUE (cnpj),
    CONSTRAINT ck_instituicao_status CHECK (status IN ('ATIVA', 'INATIVA'))
) ENGINE = InnoDB;
