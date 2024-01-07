-- VER TODOS OS DADOS INSERIDOS --
USE esideal;
SELECT * FROM clientes;
SELECT * FROM veiculos;
SELECT * FROM fichas;
SELECT * FROM funcionarios;
SELECT * FROM turnos;
SELECT * FROM checkups;
SELECT * FROM servicos;

SET SQL_SAFE_UPDATES = 0;
DELETE FROM clientes;
DELETE FROM veiculos;
DELETE FROM funcionarios;
DELETE FROM turnos;
DELETE FROM fichas;
DELETE FROM checkups;
DELETE FROM servicos;

USE esideal;

INSERT INTO clientes 
(Nome, Morada, NIF, Telefone, Email, Voucher) 
VALUES
    ('Marta Goncalves', 'Praça Central, 30', 101112131, 911111111, 'marta@email.com', 0),
    ('Pedro Santos', 'Alameda dos Ventos, 5', 141516171, 922222222, 'pedro@email.com', 1),
    ('Sofia Ferreira', 'Largo da Paz, 12', 181920212, 933333333, 'sofia@email.com', 0),
    ('Rui Almeida', 'Rua dos Girassóis, 7', 222333444, 944444444, 'rui@email.com', 1),
    ('Luisa Carvalho', 'Avenida da Liberdade, 25', 252627282, 955555555, 'luisa@email.com', 0),
    ('Hugo Pereira', 'Travessa das Árvores, 14', 293031323, 966666666, 'hugo@email.com', 1),
    ('Andreia Oliveira', 'Praça das Fontes, 9', 333435363, 977777777, 'andreia@email.com', 0),
    ('Filipe Rodrigues', 'Avenida dos Oceanos, 55', 363738394, 988888888, 'filipe@email.com', 1),
    ('Ines Sousa', 'Rua das Colinas, 18', 404142434, 999999999, 'ines@email.com', 0),
    ('Antonio Costa', 'Largo dos Poetas, 22', 434445464, 910000000, 'antonio@email.com', 1),
    ('Teresa Mendes', 'Avenida das Estrelas, 3', 474849505, 921111111, 'teresa@email.com', 0),
    ('Goncalo Silva', 'Rua do Sol, 8', 505152535, 932222222, 'goncalo@email.com', 1),
    ('Rita Santos', 'Travessa dos Pássaros, 6', 545556575, 943333333, 'rita@email.com', 0),
    ('Miguel Lopes', 'Avenida das Rosas, 16', 575859606, 954444444, 'miguel@email.com', 1),
    ('Beatriz Vieira', 'Rua das Águias, 40', 606162636, 965555555, 'beatriz@email.com', 0);

INSERT INTO veiculos 
(Matricula, Dono, NomeVeic, TVeiculo, TMotor) 
VALUES
    ('BC789DE', 'Marta Goncalves', 'Ford Fiesta', 'CARRO', 'GASOLINA'),
    ('FG987HI', 'Pedro Santos', 'Fiat Punto', 'CARRO', 'GASOLINA'),
    ('LM654OP', 'Sofia Ferreira', 'Peugeot 208', 'CARRO', 'GASOLINA'),
    ('ST321UV', 'Rui Almeida', 'Seat Ibiza', 'CARRO', 'GASOLEO'),
    ('WV951JK', 'Luisa Carvalho', 'Toyota Yaris', 'CARRO', 'HIBRIDO'),
    ('NP846QR', 'Hugo Pereira', 'Opel Corsa', 'CARRO', 'ELETRICO'),
    ('DT752FG', 'Andreia Oliveira', 'Citroen C3', 'CARRO', 'GASOLINA'),
    ('UV159MN', 'Filipe Rodrigues', 'Hyundai i20', 'CARRO', 'ELETRICO'),
    ('XS753OP', 'Ines Sousa', 'Renault Captur', 'SUV', 'GASOLEO'),
    ('LP246QR', 'Antonio Costa', 'Nissan Juke', 'SUV', 'GASOLEO'),
    ('AZ741KL', 'Teresa Mendes', 'Peugeot 2008', 'SUV', 'GASOLEO'),
    ('BV852XZ', 'Goncalo Silva', 'Renault Kadjar', 'SUV', 'GASOLEO'),
    ('HY369PL', 'Rita Santos', 'Toyota C-HR', 'SUV', 'HIBRIDO'),
    ('XF458SA', 'Miguel Lopes', 'Ford EcoSport', 'DESPORTIVO', 'ELETRICO'),
    ('QL987XC', 'Beatriz Vieira', 'Kia Sportage', 'DESPORTIVO', 'HIBRIDO');
    
INSERT INTO funcionarios 
(Cartao, HEntrada, HSaida, TipoFunc, Posto) 
VALUES
    (1, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'GASOLINA'),
    (2, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'ELETRICO'),
    (3, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'GERENTE', 'GERENCIA'),
    (4, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'GASOLEO'),
    (5, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'GERENTE', 'GERENCIA'),
    (6, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'UNIVERSAL'),
    (7, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'HIBRIDO'),
    (8, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'GERENTE', 'GERENCIA'),
    (9, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'GASOLEO'),
	(10, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'HIBRIDO'),
    (11, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'UNIVERSAL'),
	(12, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'ELETRICO'),
    (13, DATE_ADD(CURDATE(), INTERVAL 8 HOUR), DATE_ADD(CURDATE(), INTERVAL 17 HOUR), 'MECANICO', 'GASOLINA');


INSERT INTO fichas (NumFicha, Matricula, NomeDono, NomeVeiculo)
VALUES     
	(1, 'BC789DE', 'Marta Goncalves', 'Ford Fiesta'),
    (2, 'FG987HI', 'Pedro Santos', 'Fiat Punto'),
    (3, 'LM654OP', 'Sofia Ferreira', 'Peugeot 208'),
    (4, 'ST321UV', 'Rui Almeida', 'Seat Ibiza'),
    (5, 'WV951JK', 'Luisa Carvalho', 'Toyota Yaris'),
    (6, 'NP846QR', 'Hugo Pereira', 'Opel Corsa'),
    (7, 'DT752FG', 'Andreia Oliveira', 'Citroen C3'),
    (8, 'UV159MN', 'Filipe Rodrigues', 'Hyundai i20'),
    (9, 'XS753OP', 'Ines Sousa', 'Renault Captur'),
    (10, 'LP246QR', 'Antonio Costa', 'Nissan Juke'),
    (11, 'AZ741KL', 'Teresa Mendes', 'Peugeot 2008'),
    (12, 'BV852XZ', 'Goncalo Silva', 'Renault Kadjar'),
    (13, 'HY369PL', 'Rita Santos', 'Toyota C-HR'),
    (14, 'XF458SA', 'Miguel Lopes', 'Ford EcoSport'),
    (15, 'QL987XC', 'Beatriz Vieira', 'Kia Sportage');