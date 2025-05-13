-- Limpe as tabelas se já existirem dados
DELETE FROM employe_service;
DELETE FROM service;
DELETE FROM account;
DELETE FROM contact;
ALTER TABLE employe_service ALTER COLUMN id RESTART WITH 1;
ALTER TABLE service ALTER COLUMN id RESTART WITH 1;
ALTER TABLE account ALTER COLUMN id RESTART WITH 1;
ALTER TABLE contact ALTER COLUMN id RESTART WITH 1;

-- A senha para todos é "Senha123" e está criptografada em BCrypt
INSERT INTO account (BIRTH, CPF, CREATED_AT, EMAIL, NAME, PHOTO, ROLE, TEL, PASSWORD, STATUS) VALUES
('2000-11-12', '999.888.777-66', CURRENT_TIMESTAMP -350, 'joca@email.com', 'Joca da Silva', '/photo/1.jpg', 'ADMIN', '(21) 99887-7665', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'ON'),
('1984-08-30', '888.777.666-55', CURRENT_TIMESTAMP -340, 'marineuza@email.com', 'Marineuza Siriliano', '/photo/11.jpg', 'OPERATOR', '(21) 98765-4321', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'ON'),
('1992-01-24', '777.666.555-44', CURRENT_TIMESTAMP -330, 'dilermano@email.com', 'Dilermano Souza', '/photo/2.jpg', 'EMPLOYE', '(21) 98989-7676', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'ON'),
('2001-03-29', '666.555.444-33', CURRENT_TIMESTAMP -320, 'setembrino@email.com', 'Setembrino Trocatapas', '/photo/3.jpg', 'ANALIST', '(21) 98877-6665', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'ON'),
('1981-09-20', '555.444.333-22', CURRENT_TIMESTAMP -310, 'hemengarda@email.com', 'Hemengarda Sirigarda', '/photo/12.jpg', 'USER', '(21) 98798-7987', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'DEL'),
('1981-09-20', '444.333.222-11', CURRENT_TIMESTAMP -300, 'fernandino@email.com', 'Fernandino Nomecladastio', '/photo/4.jpg', 'USER', '(21) 99988-8777', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'OFF'),
('1981-09-20', '333.222.111-00', CURRENT_TIMESTAMP -290, 'salestiana@email.com', 'Salestiana Correntina', '/photo/13.jpg', 'USER', '(21) 98798-7997', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'ON');

-- A senha para todos é "Senha123" e está criptografada em BCrypt
INSERT INTO account (BIRTH, CPF, CREATED_AT, EMAIL, NAME, PHOTO, ROLE, TEL, PASSWORD, STATUS) VALUES
('1990-07-12', '222.111.000-99', CURRENT_TIMESTAMP -280, 'zuleica@email.com', 'Zuleica da Navalha', '/photo/15.jpg', 'EMPLOYE', '(21) 91111-2222', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'ON'),
('1987-05-03', '333.222.111-88', CURRENT_TIMESTAMP -270, 'brunildo@email.com', 'Brunildo dos Cortes', '/photo/6.jpg', 'EMPLOYE', '(21) 92222-3333', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'ON'),
('1995-12-09', '444.333.222-77', CURRENT_TIMESTAMP -260, 'clarice@email.com', 'Clarice Estilosa', '/photo/17.jpg', 'EMPLOYE', '(21) 93333-4444', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'ON'),
('1993-10-18', '555.444.333-66', CURRENT_TIMESTAMP -250, 'genivaldo@email.com', 'Genivaldo Barbeiro', '/photo/8.jpg', 'EMPLOYE', '(21) 94444-5555', '$2a$10$Z830aS2.ZpP/AfhyEclgvOBbw3/DPsWuiAc6Qp.SVJpUfJ/O.fVQi', 'ON');

INSERT INTO service (date, title, description, price, metadata, status, photo1, photo2, photo3, photo4) VALUES
(CURRENT_TIMESTAMP -77, 'Corte simples', 'Corte tradicional com tesoura e pente', 29.90, '', 'ON', 'https://picsum.photos/400/300', 'https://picsum.photos/401/301', 'https://picsum.photos/402/302', 'https://picsum.photos/403/303'),
(CURRENT_TIMESTAMP -66, 'Corte com máquina', 'Rápido, prático e moderno com máquina elétrica', 25.00, '', 'ON', 'https://picsum.photos/400/300', 'https://picsum.photos/401/301', 'https://picsum.photos/402/302', 'https://picsum.photos/403/303'),
(CURRENT_TIMESTAMP -55, 'Corte + Barba + Sobrancelha', 'Pacote completo com estilo', 44.00, '', 'ON', 'https://picsum.photos/400/300', 'https://picsum.photos/401/301', 'https://picsum.photos/402/302', 'https://picsum.photos/403/303'),
(CURRENT_TIMESTAMP, 'Corte degradê', 'Corte estilizado com acabamento degradê', 39.90, '', 'ON', 'https://picsum.photos/400/300', 'https://picsum.photos/401/301', 'https://picsum.photos/402/302', 'https://picsum.photos/403/303'),
(CURRENT_TIMESTAMP, 'Hidratação capilar', 'Tratamento nutritivo para cabelos masculinos', 35.00, '', 'ON', 'https://picsum.photos/400/300', 'https://picsum.photos/401/301', 'https://picsum.photos/402/302', 'https://picsum.photos/403/303'),
(CURRENT_TIMESTAMP, 'Barba desenhada', 'Barba com desenho personalizado e navalha', 30.00, '', 'ON', 'https://picsum.photos/400/300', 'https://picsum.photos/401/301', 'https://picsum.photos/402/302', 'https://picsum.photos/403/303'),
(CURRENT_TIMESTAMP, 'Sobrancelha na régua', 'Acabamento preciso na sobrancelha com régua e navalha', 20.00, '', 'ON', 'https://picsum.photos/400/300', 'https://picsum.photos/401/301', 'https://picsum.photos/402/302', 'https://picsum.photos/403/303'),
(CURRENT_TIMESTAMP, 'Luzes no cabelo', 'Clareamento parcial com estilo', 59.90, '', 'ON', 'https://picsum.photos/400/300', 'https://picsum.photos/401/301', 'https://picsum.photos/402/302', 'https://picsum.photos/403/303');

-- Corte simples atendido por Zuleica (id 8) e Genivaldo (id 11)
INSERT INTO employe_service (service_id, employe_id) VALUES (1, 8), (1, 11);

-- Corte com máquina por Brunildo (id 9) e Clarice (id 10)
INSERT INTO employe_service (service_id, employe_id) VALUES (2, 9), (2, 10);

-- Pacote completo por todos os novos EMPLOYE
INSERT INTO employe_service (service_id, employe_id) VALUES (3, 8), (3, 9), (3, 10), (3, 11);

-- Corte degradê: Clarice e Genivaldo
INSERT INTO employe_service (service_id, employe_id) VALUES (4, 10), (4, 11);

-- Hidratação capilar: Zuleica e Clarice
INSERT INTO employe_service (service_id, employe_id) VALUES (5, 8), (5, 10);

-- Barba desenhada: Brunildo e Genivaldo
INSERT INTO employe_service (service_id, employe_id) VALUES (6, 9), (6, 11);

-- Sobrancelha na régua: Zuleica e Brunildo
INSERT INTO employe_service (service_id, employe_id) VALUES (7, 8), (7, 9);

-- Luzes no cabelo: Clarice
INSERT INTO employe_service (service_id, employe_id) VALUES (8, 10);
