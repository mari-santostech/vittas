CREATE DATABASE IF NOT EXISTS vittas_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE vittas_db;

CREATE TABLE IF NOT EXISTS instituicao (
    id_instituicao BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    razao_social VARCHAR(150) NOT NULL,
    nome_fantasia VARCHAR(150) NOT NULL,
    endereco VARCHAR(200) NOT NULL,
    telefone VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    data_cadastro DATETIME NOT NULL
) ENGINE = InnoDB;
