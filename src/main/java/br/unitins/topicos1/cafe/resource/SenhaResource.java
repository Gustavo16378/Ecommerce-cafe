package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.AlterarSenhaRequestDTO;
import br.unitins.topicos1.cafe.dto.EsqueceuSenhaRequestDTO;
import br.unitins.topicos1.cafe.dto.RedefinirSenhaRequestDTO;
import br.unitins.topicos1.cafe.service.UsuarioServiceImpl;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/senha")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SenhaResource {

    @Inject UsuarioServiceImpl service;
    @Inject SecurityIdentity identity;

    // Solicita recuperação — envia token por email
    @PATCH
    @Path("/esqueceu")
    @PermitAll
    public Response esqueceuSenha(@Valid EsqueceuSenhaRequestDTO dto) {
        String mensagem = service.esqueceuSenha(dto);
        return Response.ok(mensagem).build();
    }

    // Redefine a senha com o token recebido por email
    @PATCH
    @Path("/redefinir")
    @PermitAll
    public Response redefinirSenha(@Valid RedefinirSenhaRequestDTO dto) {
        service.redefinirSenha(dto);
        return Response.noContent().build();
    }

    // Altera senha do usuário logado
    @PATCH
    @Path("/alterar")
    @RolesAllowed({"ADMIN", "USER"})
    public Response alterarSenha(@Valid AlterarSenhaRequestDTO dto) {
        service.alterarSenha(identity.getPrincipal().getName(), dto);
        return Response.noContent().build();
    }
}
