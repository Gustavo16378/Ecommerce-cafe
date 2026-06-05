package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.ProdutoRequestDTO;
import br.unitins.topicos1.cafe.dto.ProdutoResponseDTO;
import br.unitins.topicos1.cafe.service.ProdutoServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {
    @Inject
    ProdutoServiceImpl service;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public List<ProdutoResponseDTO> listar(@QueryParam("nome") String nome) {
        if (nome != null && !nome.isBlank()) {
            return service.buscarPorNome(nome);
        }
        return service.listar();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response salvar(@Valid ProdutoRequestDTO dto) {
        ProdutoResponseDTO response = service.salvar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public ProdutoResponseDTO atualizar(@PathParam("id") Long id, @Valid ProdutoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response excluir(@PathParam("id") Long id) {
        service.excluir(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public ProdutoResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id);
    }
}
