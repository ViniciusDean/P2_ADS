DROP DATABASE Distribuidora;
CREATE DATABASE Distribuidora;

USE Distribuidora;

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
    cancelamento CHAR(1)
);

-- Criação da tabela produto
CREATE TABLE produto (
    id BIGINT  PRIMARY KEY,
    nome VARCHAR(50),
    data_compra VARCHAR(40),
    codigo_barras VARCHAR(40),
    preco_venda FLOAT,
    preco_custo FLOAT,
    embalagem VARCHAR(20),
    fornecedor_id INT,
    quantidade INT
);

-- Criação da tabela funcionario
CREATE TABLE funcionario (
    id INT  PRIMARY KEY,
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

-- Adição de chave estrangeira na tabela produto
ALTER TABLE produto ADD CONSTRAINT FK_DISTRIB 
    FOREIGN KEY (fornecedor_id) 
    REFERENCES fornecedor (id);

-- Inserção de dados na tabela fornecedor
INSERT INTO fornecedor (id, CEP,  telefone, razao_social, email, cnpj, regime_tributacao, tipo_frete, devolucao, cancelamento) VALUES
(1,'04538133', '11 1234-5678', 'Ambev S.A.', 'contato@ambev.com.br', '75179792000186', 'Lucro Real', 'CIF', 'N', 'N' ),
(2,'04709111', '11 2345-6789', 'Nestlé Brasil Ltda.', 'contato@nestle.com.br', '60409075000152', 'Lucro Real', 'FOB', 'S', 'N'),
(3,'03178200', '11 3456-7890', 'Pão de Açúcar', 'contato@paodeacucar.com.br', '47508411000156', 'Lucro Presumido', 'CIF', 'N', 'S');

-- Inserção de dados na tabela produto
INSERT INTO produto (id, nome, codigo_barras, preco_venda, preco_custo, embalagem, fornecedor_id, quantidade) VALUES
(1, 'Cerveja Skol 350ml', '7891000010101', 2.50, 1.50, 'Lata', 1, 10),
(2, 'Heineken Lata 350ml', '7891000240101', 5.50, 3.00, 'Lata', 2, 500),
(3, 'Coca Cola 2L', '7896070101018', 10.00, 5.00, 'Garrafa', 3, 300),
(4, 'Guaraná Antarctica 2L', '7891991010011', 7.00, 4.50, 'Garrafa', 1, 200),
(5, 'Suco Laranja 10L', '7891000060102', 30.00, 20.00, 'Garrafa', 2, 150);

-- Inserção de dados na tabela funcionario
INSERT INTO funcionario (username, nome, email, cpf, data_nasc, contrato, CEP, senha, telefone, id) VALUES
('vdean', 'Vinicius Dean', 'vinicius.dean@distribuidora.com', '123.456.789-00', '1985-05-15', 'CLT', '01234-567', 'admin', '11-969038604', '1'),
('gvolpi', 'Gustavo Volpi', 'gustavo.volpi@distribuidora.com', '987.654.321-00', '1990-08-25', 'CLT', '76543-210', 'admin', '11-969033245','2');
