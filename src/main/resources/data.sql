INSERT INTO cliente (nome, telefone) VALUES ('Ket', '(34) 99999-1111');
INSERT INTO cliente (nome, telefone) VALUES ('Carlos Silva', '(34) 98888-2222');

INSERT INTO profissional (name, especialidade, telefone) VALUES ('Dr. Roberto', 'Odontologia', '(34) 97777-3333');
INSERT INTO profissional (name, especialidade, telefone) VALUES ('Ana Souza', 'Fisioterapia', '(34) 96666-4444');

INSERT INTO servico (name, preco, duracao_minutos, profissional_id)
VALUES ('Clareamento a Laser', 350.00, 60, 1);
INSERT INTO servico (name, preco, duracao_minutos, profissional_id)
VALUES ('Limpeza Dental', 150.00, 45, 1);

INSERT INTO servico (name, preco, duracao_minutos, profissional_id)
VALUES ('Sessão de RPG', 120.00, 60, 2);
INSERT INTO servico (name, preco, duracao_minutos, profissional_id)
VALUES ('Massagem Terapêutica', 100.00, 50, 2);

INSERT INTO agendamento (data, horario, status, profissional_id, cliente_id)
VALUES ('2026-11-10', '2026-11-10 14:30:00', 'PENDENTE', 1, 1);

INSERT INTO agendamento (data, horario, status, profissional_id, cliente_id)
VALUES ('2026-11-11', '2026-11-11 09:00:00', 'CONFIRMADO', 2, 2);