package com.andrewmatheus.labpcp.service;

import com.andrewmatheus.labpcp.controller.dto.Request.CadastrarMateriaRequest;
import com.andrewmatheus.labpcp.datasource.entity.CursoEntity;
import com.andrewmatheus.labpcp.datasource.entity.MateriaEntity;
import com.andrewmatheus.labpcp.datasource.repository.CursoRepository;
import com.andrewmatheus.labpcp.datasource.repository.MateriaRepository;
import com.andrewmatheus.labpcp.exceptions.GenericException;
import com.andrewmatheus.labpcp.service.interfaces.MateriaServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MateriaService implements MateriaServiceI {

    private final AuthService authService;
    private final MateriaRepository materiaRepository;
    private final CursoRepository cursoRepository;

    @Override
    public MateriaEntity criarMateria(
            CadastrarMateriaRequest cadastrarMateriaRequest,
            String token)
    {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("buscando curso informado!");
        Optional<CursoEntity> curso = cursoRepository.findById(cadastrarMateriaRequest.id_curso());

        if (curso.isEmpty()) {
            log.error("Curso não encontrado!");
            throw new RuntimeException("Curso não encontrado!");
        }

        MateriaEntity novaMateria = new MateriaEntity();
        novaMateria.setNome(cadastrarMateriaRequest.nome());
        novaMateria.setCurso(curso.get());

        log.info("Salvando novo curso!");
        materiaRepository.save(novaMateria);

        return novaMateria;
    }

    @Override
    public MateriaEntity atualizaMateriaPorId(
            Long id,
            CadastrarMateriaRequest cadastrarMateriaRequest,
            String token)
    {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("buscando materia...");
        Optional<MateriaEntity> materiaBuscado = materiaRepository.findById(id);

        if (materiaBuscado.isEmpty()) {
            log.error("Materia não encontrada!");
            throw new GenericException("Materia não encontrada!", HttpStatus.NOT_FOUND);
        }

        log.info("buscando curso informado!");
        Optional<CursoEntity> curso = cursoRepository.findById(cadastrarMateriaRequest.id_curso());

        if (curso.isEmpty()) {
            log.error("Curso não encontrado!");
            throw new RuntimeException("Curso não encontrado!");
        }

        MateriaEntity materiaExistente = materiaBuscado.get();

        materiaExistente.setNome(cadastrarMateriaRequest.nome());
        materiaExistente.setCurso(curso.get());

        log.info("Atualizando materia!");
        materiaRepository.save(materiaExistente);

        return materiaExistente;
    }

    @Override
    public MateriaEntity buscaMateriaPorId(
            Long id,
            String token)
    {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("Buscando materia por ID!");
        Optional<MateriaEntity> materia = materiaRepository.findById(id);

        if (materia.isEmpty()) {
            log.error("Materia não encontrado!");
            throw new GenericException("Materia não encontrado!", HttpStatus.NOT_FOUND);
        }

        return materia.get();
    }

    @Override
    public List<MateriaEntity> buscaTodos(String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") &&
                !papelToken.equalsIgnoreCase("PEDAGOGICO")
        )  {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("Retornando lista de materias!");
        return materiaRepository.findAll();
    }

    @Override
    public void removerMateriaPorId(Long id, String token) {
        log.info("Iniciando verificação de autenticação!");
        String papelToken =  authService.buscaCampoToken(token, "scope");

        if (!papelToken.equalsIgnoreCase("ADM") ) {
            log.error("Usuario não tem acesso a essa funcionalidade!");
            throw new RuntimeException("Usuario não tem acesso a essa funcionalidade");
        }

        log.info("Buscando materia por ID!");
        Optional<MateriaEntity> materia = materiaRepository.findById(id);

        if (materia.isEmpty()) {
            log.error("Matéria não encontrada!");
            throw new GenericException("Matéria não encontrada!", HttpStatus.NOT_FOUND);
        }

        log.info("Removendo Matéria por ID!");
        materiaRepository.delete(materia.get());
    }
}
