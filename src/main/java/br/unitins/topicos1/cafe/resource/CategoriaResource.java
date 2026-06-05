package br.unitins.topicos1.cafe.resource;

import java.util.List;

import br.unitins.topicos1.cafe.dto.CategoriaRequestDTO;
import br.unitins.topicos1.cafe.dto.CategoriaResponseDTO;
import br.unitins.topicos1.cafe.service.CategoriaServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaResource {

    @Inject
    CategoriaServiceImpl service;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public List<CategoriaResponseDTO> listar() {
        return service.listar();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response salvar(@Valid CategoriaRequestDTO dto) {
        CategoriaResponseDTO response = service.salvar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public CategoriaResponseDTO atualizar(@PathParam("id") Long id, @Valid CategoriaRequestDTO dto) {
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
    public CategoriaResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id);
    }
}
