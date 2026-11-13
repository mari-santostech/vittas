
USE vittas_db;

CREATE TABLE instituicao (
    id_instituicao BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    razao_social VARCHAR(150) NOT NULL,
    nome_fantasia VARCHAR(150) NOT NULL,
    endereco VARCHAR(200) NOT NULL,
    telefone VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    data_cadastro DATETIME NOT NULL
) ENGINE = InnoDB;

INSERT INTO vittas_db.instituicao
(cnpj, razao_social, nome_fantasia, endereco, telefone, status, data_cadastro)
VALUES
(
    '12.345.678/0001-95',
    'Clinica Teste VITTAS Ltda',
    'Clinica Teste VITTAS',
    'Rua Exemplo, 100 - Maringa/PR',
    '(44) 99999-0000',
    'ATIVA',
    NOW()
);

SELECT * FROM vittas_db.instituicao;

UPDATE vittas_db.instituicao
SET endereco = 'Avenida Brasil, 500 - Maringa/PR'
WHERE id_instituicao = 1;

SELECT * FROM vittas_db.instituicao
WHERE id_instituicao = 1;

DELETE FROM vittas_db.instituicao
WHERE id_instituicao = 1;

SELECT * FROM vittas_db.instituicao
WHERE id_instituicao = 1;