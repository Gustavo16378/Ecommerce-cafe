package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.AuthResponseDTO;
import br.unitins.topicos1.cafe.dto.LoginRequestDTO;
import br.unitins.topicos1.cafe.model.Usuario;
import br.unitins.topicos1.cafe.repository.UsuarioRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class AuthServiceImpl {

    @Inject
    UsuarioRepository repository;

    public AuthResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = repository.findByLogin(dto.login());

        if (usuario == null || !BcryptUtil.matches(dto.senha(), usuario.getSenha())) {
            throw new NotAuthorizedException("Login ou senha inválidos");
        }

        String token = Jwt.issuer("ecommerce-cafe")
                .subject(dto.login())
                .groups(Set.of(usuario.getPerfil().name()))
                .expiresIn(Duration.ofHours(24))
                .sign();

        return new AuthResponseDTO(token, "Bearer");
    }
}
