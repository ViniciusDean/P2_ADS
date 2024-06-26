-- Criação do banco de dados
DROP DATABASE IF EXISTS Distribuidora;
CREATE DATABASE Distribuidora;
USE Distribuidora;

-- Criação da tabela fornecedor
CREATE TABLE fornecedor (
    id INT PRIMARY KEY,
    CEP VARCHAR(20),
    telefone VARCHAR(20),
    razao_social VARCHAR(50),
    email VARCHAR(40),
    cnpj VARCHAR(20),
    regime_tributacao VARCHAR(30),
    tipo_frete VARCHAR(30),
    devolucao CHAR(1),
    cancelamento CHAR(1),
    tipo_fornecedor VARCHAR(20)
);

-- Criação da tabela produto
CREATE TABLE produto (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(50),
    data_compra VARCHAR(40),
    codigo_barras VARCHAR(40),
    preco_venda FLOAT,
    preco_custo FLOAT,
    embalagem VARCHAR(20),
    fornecedor_id INT,
    quantidade INT,
    FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id)
);

-- Criação da tabela funcionario
CREATE TABLE funcionario (
    id INT PRIMARY KEY,
    username VARCHAR(20),
    telefone VARCHAR(20),
    nome VARCHAR(55),
    email VARCHAR(50),
    senha VARCHAR(50),
    cpf VARCHAR(15),
    data_nasc DATE,
    contrato VARCHAR(20),
    CEP VARCHAR(9)
);

-- Criação da tabela cliente
CREATE TABLE cliente (
    id INT PRIMARY KEY,
    nome VARCHAR(55),
    cpf VARCHAR(15)
);

CREATE TABLE venda (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cliente_id INT,
    data_hora DATETIME,
    operador_id INT,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (operador_id) REFERENCES funcionario(id)
);

-- Criação da tabela venda_produto
CREATE TABLE venda_produto (
    venda_id INT,
    produto_id BIGINT,
    FOREIGN KEY (venda_id) REFERENCES venda(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id),
    PRIMARY KEY (venda_id, produto_id)
);

-- Inserção de dados na tabela fornecedor
INSERT INTO fornecedor (id, CEP, telefone, razao_social, email, cnpj, regime_tributacao, tipo_frete, devolucao, cancelamento, tipo_fornecedor) VALUES
(1, '04538133', '11 1234-5678', 'Ambev S.A.', 'contato@ambev.com.br', '75179792000186', 'Especial', 'TERCEIROS', 'N', 'N', 'DISTRIBUIDOR'	),
(2, '04709111', '11 2345-6789', 'Nestlé Brasil Ltda.', 'contato@nestle.com.br', '60409075000152', 'Simples', 'SEM FRETE', 'S', 'N', 'ATACADO'),
(3, '03178200', '11 3456-7890', 'Pão de Açúcar', 'contato@paodeacucar.com.br', '47508411000156', 'Simples', 'FRETE PRÓPRIO', 'N', 'S', 'VAREJO');

-- Inserção de dados na tabela produto
INSERT INTO produto (id, nome, data_compra, codigo_barras, preco_venda, preco_custo, embalagem, fornecedor_id, quantidade) VALUES
(1, 'Cerveja Skol 350ml', '2023-01-01', '123', 2.50, 1.50, 'Lata', 1, 10),
(2, 'Heineken Lata 350ml', '2023-01-01', '124', 5.50, 3.00, 'Vasilhame', 2, 500),
(3, 'Coca Cola 2L', '2023-01-01', '125', 10.00, 5.00, 'Retornável', 3, 300),
(4, 'Guaraná Antarctica 2L', '2023-01-01', '126', 7.00, 4.50, 'Retornável', 1, 200),
(5, 'Suco Laranja 10L', '2023-01-01', '127', 30.00, 20.00, 'Fardo', 2, 150),
(6, 'Fardo Coca Cola 600ml', '2024-01-01', '128', 50.00, 40.00, 'Fardo', 2, 100);

-- Inserção de dados na tabela funcionario
INSERT INTO funcionario (id, username, telefone, nome, email, senha, cpf, data_nasc, contrato, CEP) VALUES
(1, 'vdean', '11969038604', 'Vinicius Dean', 'vinicius.dean@distribuidora.com', 'admin', '12345678900', '1985-05-15', 'CLT', '09280370'),
(2, 'gvolpi', '11969033245', 'Gustavo Volpi', 'gustavo.volpi@distribuidora.com', 'admin', '98765432100', '1990-08-25', 'CLT', '09121720');

-- Inserção de dados na tabela cliente
INSERT INTO cliente (id, nome, cpf) VALUES
('1', 'Professor', '11111111111'),
('2' ,'Vinicius Dean', '50602487803'),
('3','Gustavo Volpi', '22222222222');

-- Inserção de dados na tabela venda
INSERT INTO venda (cliente_id, operador_id, data_hora) VALUES
(1, 1, '2023-01-01 10:00:00'),
(2, 2, '2023-01-02 11:00:00');

-- Inserção de dados na tabela venda_produto
INSERT INTO venda_produto (venda_id, produto_id) VALUES
(1, 1),
(1, 2),
(2, 3);