
USE vittas_db;

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
WHERE cnpj = '12.345.678/0001-95';

SELECT * FROM vittas_db.instituicao
WHERE cnpj = '12.345.678/0001-95';

DELETE FROM vittas_db.instituicao
WHERE cnpj = '12.345.678/0001-95';

SELECT * FROM vittas_db.instituicao
WHERE cnpj = '12.345.678/0001-95';