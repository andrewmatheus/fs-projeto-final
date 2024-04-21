package com.andrewmatheus.labpcp.service;

import com.andrewmatheus.labpcp.controller.dto.Request.LoginRequest;
import com.andrewmatheus.labpcp.controller.dto.Response.LoginResponse;
import com.andrewmatheus.labpcp.datasource.entity.UsuarioEntity;
import com.andrewmatheus.labpcp.datasource.repository.UsuarioRepository;
import com.andrewmatheus.labpcp.service.interfaces.AuthServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements AuthServiceI {

    private final BCryptPasswordEncoder bCryptEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UsuarioRepository usuarioRepository;

    private static long TEMPO_EXPIRACAO = 36000L; //contante de tempo de expiração em segundos

    public LoginResponse gerarToken(@RequestBody LoginRequest loginRequest) {
        log.info("Iniciando Login!");
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findByLogin(loginRequest.login());

        if (usuarioEntity.isEmpty()) {
            log.error("Usuário não possui cadastro!");
            return new LoginResponse("",0, HttpStatus.NOT_FOUND.value(), "Usuário não possui cadastro");
        }

        if(!usuarioEntity.get().validaSenha(loginRequest, bCryptEncoder)){
            log.error("Erro, senha inválida");
            return new LoginResponse("",0, HttpStatus.UNAUTHORIZED.value(), "Erro senha inválida");
        }

        Instant now = Instant.now();
        Long idUsuario = usuarioEntity.get().getId();

        String scope = usuarioEntity.get().getPapel().toString();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("labPCP")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(TEMPO_EXPIRACAO))
                .subject(idUsuario.toString())
                .claim("scope", scope)
                .claim("userId", idUsuario)
                .build();

        var valorJWT = jwtEncoder.encode(
                        JwtEncoderParameters.from(claims)
                )
                .getTokenValue();

        log.info("Usuário efetuou login com sucesso!");
        return new LoginResponse(valorJWT, TEMPO_EXPIRACAO, HttpStatus.OK.value(), "Login realizado com sucesso!");
    }

    public String buscaCampoToken(String token, String claim) {

        String papel = jwtDecoder
                        .decode(token)
                        .getClaims()
                        .get(claim)
                        .toString();

        int startIndex = papel.indexOf("nome=") + 5;
        int endIndex = papel.indexOf(")", startIndex);
        return papel.substring(startIndex, endIndex);
    }

    public String buscaCampoUserIdToken(String token) {
        return jwtDecoder
                .decode(token)
                .getClaims()
                .get("userId")
                .toString();
    }
}
