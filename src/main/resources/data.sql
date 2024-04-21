-- Inserir os dados de papel caso não exista
insert into papel (id, nome)
values (1, 'ADM'),
       (2, 'PEDAGOGICO'),
       (3, 'RECRUITER'),
       (4, 'PROFESSOR'),
       (5, 'ALUNO')
ON CONFLICT (id) DO NOTHING;

