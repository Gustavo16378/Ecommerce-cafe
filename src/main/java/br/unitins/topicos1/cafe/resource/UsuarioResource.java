package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.*;
import br.unitins.topicos1.cafe.service.UsuarioServiceImpl;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioServiceImpl service;

    @Inject
    SecurityIdentity identity;

    @GET
    @RolesAllowed("ADMIN")
    public List<UsuarioResponseDTO> listar() {
        return service.listar();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public UsuarioResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id);
    }

    @POST
    @PermitAll
    public Response salvar(@Valid UsuarioRequestDTO dto) {
        return Response.status(Response.Status.CREATED).entity(service.salvar(dto)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public UsuarioResponseDTO atualizar(@PathParam("id") Long id, @Valid UsuarioRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response excluir(@PathParam("id") Long id) {
        service.excluir(id);
        return Response.noContent().build();
    }

    // Cadastro e-commerce simples (só login e senha)
    @POST
    @Path("/cadastro/simples")
    @PermitAll
    public Response cadastroSimples(@Valid CadastroClienteSimplesRequestDTO dto) {
        return Response.status(Response.Status.CREATED).entity(service.cadastroSimples(dto)).build();
    }

    // Cadastro e-commerce completo (dados pessoais + endereço)
    @POST
    @Path("/cadastro/completo")
    @PermitAll
    public Response cadastroCompleto(@Valid CadastroClienteCompletoRequestDTO dto) {
        return Response.status(Response.Status.CREATED).entity(service.cadastroCompleto(dto)).build();
    }

    // Alterar senha (usuário logado)
    @PATCH
    @Path("/senha")
    @RolesAllowed({"ADMIN", "USER"})
    public Response alterarSenha(@Valid AlterarSenhaRequestDTO dto) {
        service.alterarSenha(identity.getPrincipal().getName(), dto);
        return Response.noContent().build();
    }

    // Esqueceu a senha (sem autenticação)
    @PATCH
    @Path("/esqueceu-senha")
    @PermitAll
    public Response esqueceuSenha(@Valid EsqueceuSenhaRequestDTO dto) {
        service.esqueceuSenha(dto);
        return Response.noContent().build();
    }

    // Editar dados do próprio perfil (usuário logado)
    @PATCH
    @Path("/meu-perfil")
    @RolesAllowed({"ADMIN", "USER"})
    public UsuarioResponseDTO editarPerfil(@Valid EditarPerfilRequestDTO dto) {
        return service.editarPerfil(identity.getPrincipal().getName(), dto);
    }

    // Buscar endereços do cliente logado
    @GET
    @Path("/meus-enderecos")
    @RolesAllowed({"ADMIN", "USER"})
    public List<EnderecoClienteResponseDTO> meusEnderecos() {
        return service.buscarEnderecosPorCliente(identity.getPrincipal().getName());
    }

    // Adicionar endereço
    @POST
    @Path("/meus-enderecos")
    @RolesAllowed({"ADMIN", "USER"})
    public Response adicionarEndereco(@Valid EnderecoClienteRequestDTO dto) {
        return Response.status(Response.Status.CREATED)
                .entity(service.adicionarEndereco(identity.getPrincipal().getName(), dto))
                .build();
    }

    // Atualizar endereço
    @PUT
    @Path("/meus-enderecos/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public EnderecoClienteResponseDTO atualizarEndereco(@PathParam("id") Long id, @Valid EnderecoClienteRequestDTO dto) {
        return service.atualizarEndereco(identity.getPrincipal().getName(), id, dto);
    }

    // Remover endereço
    @DELETE
    @Path("/meus-enderecos/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public Response removerEndereco(@PathParam("id") Long id) {
        service.removerEndereco(identity.getPrincipal().getName(), id);
        return Response.noContent().build();
    }
}
