USE vittas_db;

-- CREATE
INSERT INTO instituicao
    (cnpj, razao_social, nome_fantasia, endereco, telefone, status, data_cadastro)
VALUES
    ('11222333000181', 'Clinica Teste VITTAS Ltda', 'Clinica Teste VITTAS',
     'Rua Exemplo, 100 - Maringa/PR', '44999990000', 'ATIVA', NOW());

-- READ
SELECT * FROM instituicao WHERE cnpj = '11222333000181';

-- UPDATE
UPDATE instituicao
SET endereco = 'Avenida Brasil, 500 - Maringa/PR'
WHERE cnpj = '11222333000181';

SELECT * FROM instituicao WHERE cnpj = '11222333000181';

-- DELETE logico (mesma regra usada pela API)
UPDATE instituicao
SET status = 'INATIVA'
WHERE cnpj = '11222333000181';

SELECT * FROM instituicao WHERE cnpj = '11222333000181';

-- Limpeza do registro de teste
DELETE FROM instituicao WHERE cnpj = '11222333000181';
