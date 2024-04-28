<h1 align="center">
  LabPCP: API para Gestão Educacional
</h1>  

<p align="center">
  <a href="#notebook-Sobre">Sobre</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#rocket-Inicio">Inicio</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#hammer-Tecnologias">Tecnologias</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#art-Rotas">Rotas (endpoints)</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#mailbox-Contato">Contato</a>
</p>   

## :notebook: Sobre

**LabPCP**, *Projeto final da primeira etapa do curso fullstack-floripa-mais-tech.*

*A LabPCP é uma aplicação back-end desenvolvida em Java, seguindo o padrão de API Rest utilizando o framework Spring Boot. O principal objetivo do sistema é gerenciar informações relacionadas a cursos, turmas, alunos, professores, notas e usuários em uma instituição de ensino.*

[Documento do desafio](https://docs.google.com/document/d/1lXi_jwsIJr3ykwBxdL2KfnvgpinivBt9FPLiK7YoCcQ/edit?usp=sharing)

## :rocket: Inicio

### Instalação

**Clonando Repositório**

```bash
$ git clone https://github.com/andrewmatheus/fs-projeto-final.git
```

### Rodar projeto

#### Configurar Dependências
  
- Certifique-se de ter o [Java](https://www.java.com/pt-BR/download/manual.jsp) e o [PostgreSQL](https://www.postgresql.org/download/) instalados em sua máquina.
- Configure as informações de conexão com o banco de dados no arquivo `application.properties`.

```bash
#Observação:
- Você pode utilizar o .env e configurar em seu projeto para pegar dinamicamente 
ou colocar o username e password manualmente nas linhas abaixo:
 
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

Existe o example.env na raiz do projeto, renomei para .env e configure 
o environment variables corretamente em seu projeto para permanecer dinamicamente.
```

- Execute a aplicação utilizando sua IDE preferida ou via linha de comando.
- Utilize as rotas definidas para interagir com o sistema.

#### Docker

- [Instalar Docker](https://docs.docker.com/compose/migrate/)
- Após configurar o docker corretamente, execute o arquivo de configuração docker-compose.yml

#### URL requisições

```bash
  http://localhost:8081
#  Caso queira alterar ir em properties e alterar valor -> server.port=8081
```

## :hammer: Tecnologias

- Linguagem de Programação: [Java](https://docs.oracle.com/en/java/)
- Framework: [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- Banco de Dados: [PostgreSQL](https://www.postgresql.org/docs/)
- Controle de Versão: [Git (GitHub)](https://docs.github.com/pt)
- Controle de Segurança: [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [Docker](https://docs.docker.com/)

#### Estrutura do Projeto

A estrutura do projeto segue o padrão MVC (Model-View-Controller), com a seguinte organização:

- **Controller**: Responsável por receber as requisições HTTP e chamar os métodos adequados nos serviços.
- **Service**: Responsável pela lógica de negócio da aplicação, realiza chamadas aos repositórios.
- **Repository**: Responsável pela comunicação com o banco de dados.

#### Segurança

A segurança da aplicação é garantida pelo Spring Security, onde cada usuário possui um token JWT (JSON Web Token) para acesso aos dados.

#### Melhorias

- Implementação de testes automatizados.
- Ajuste em retornos endpoint com lista de acordo com melhor encaixe no projeto front end na próxima etapa.

## :art: Rotas

> Para auxiliar segue arquivo exportado do postman para ajudar configurar endpoints para uso `labPCP.postman_collection.json` o mesmo está na raiz do projeto;

#### Endpoint de Login

- **Login de Usuário** (POST /login)
    - Descrição: Permite que um usuário faça login no sistema.
    - Respostas Possíveis:
        - 200 (OK): Login bem-sucedido. Retorna um token JWT no corpo da resposta.
        - 401 (Unauthorized): Credenciais inválidas.
        - 400 (Bad Request): Requisição inválida.

#### Endpoint de Cadastro

- **Cadastro de Usuário** (POST /cadastro)
    - Descrição: Permite que um novo usuário seja cadastrado no sistema.
    - Respostas Possíveis:
        - 201 (Created): Usuário cadastrado com sucesso.
        - 400 (Bad Request): Requisição inválida.

#### Endpoints para entidade Docente

- **Criar Docente** (POST /docentes)
    - Cria um novo docente.
- **Obter Docente por ID** (GET /docentes/{id})
    - Obtém um docente específico pelo ID.
- **Atualizar Docente** (PUT /docentes/{id})
    - Atualiza os dados de um docente específico.
- **Excluir Docente** (DELETE /docentes/{id})
    - Exclui um docente específico.
- **Listar Docentes** (GET /docentes)
    - Lista todos os docentes cadastrados.

#### Endpoints para entidade Turma

- **Criar Turma** (POST /turmas)
    - Cria uma nova turma.
- **Obter Turma por ID** (GET /turmas/{id})
    - Obtém uma turma específica pelo ID.
- **Atualizar Turma** (PUT /turmas/{id})
    - Atualiza os dados de uma turma específica.
- **Excluir Turma** (DELETE /turmas/{id})
    - Exclui uma turma específica.
- **Listar Turmas** (GET /turmas)
    - Lista todas as turmas cadastradas.

### Endpoints para entidade Aluno

- **Criar Aluno** (POST /alunos)
    - Cria um novo aluno.
- **Obter Aluno por ID** (GET /alunos/{id})
    - Obtém um aluno específico pelo ID.
- **Atualizar Aluno** (PUT /alunos/{id})
    - Atualiza os dados de um aluno específico.
- **Excluir Aluno** (DELETE /alunos/{id})
    - Exclui um aluno específico.
- **Listar Alunos** (GET /alunos)
    - Lista todos os alunos cadastrados.

#### Endpoints para entidade Curso

- **Criar Curso** (POST /cursos)
    - Cria um novo curso.
- **Obter Curso por ID** (GET /cursos/{id})
    - Obtém um curso específico pelo ID.
- **Atualizar Curso** (PUT /cursos/{id})
    - Atualiza os dados de um curso específico.
- **Excluir Curso** (DELETE /cursos/{id})
    - Exclui um curso específico.
- **Listar Cursos** (GET /cursos)
    - Lista todos os cursos cadastrados.

#### Endpoints para entidade Matérias

- **Listar Matérias por Curso** (GET /cursos/{id_curso}/materias)
    - Lista todas as matérias de um curso específico.
- **Criar Matéria** (POST /materias)
    - Cria uma nova matéria.
- **Obter Matéria por ID** (GET /materias/{id})
    - Obtém uma matéria específica pelo ID.
- **Atualizar Matéria** (PUT /materias/{id})
    - Atualiza os dados de uma matéria específica.
- **Excluir Matéria** (DELETE /materias/{id})
    - Exclui uma matéria específica.

#### Endpoints para entidade Notas

- **Listar Notas por Aluno** (GET /notas/alunos/{id_aluno}/notas)
    - Lista todas as notas de um aluno específico.
- **Criar Nota** (POST /notas)
    - Cria uma nova nota.
- **Obter Nota por ID** (GET /notas/{id})
    - Obtém uma nota específica pelo ID.
- **Atualizar Nota** (PUT /notas/{id})
    - Atualiza os dados de uma nota específica.
- **Excluir Nota** (DELETE /notas/{id})
    - Exclui uma nota específica.

#### Endpoint para obter Pontuação Total do Aluno

- **Obter Pontuação Total do Aluno** (GET /notas/alunos/{id}/pontuacao)
    - Calcula a pontuação total de um aluno específico.

## :mailbox: Contato

Email: andrewmatheus@gmail.com

Vamos criar conexão, segue linkedIn para contato: [LinkedIn](https://www.linkedin.com/in/andrew-cabral-developer/).
