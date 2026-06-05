package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.TorraRequestDTO;
import br.unitins.topicos1.cafe.dto.TorraResponseDTO;
import br.unitins.topicos1.cafe.service.TorraServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/torras")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TorraResource {

    @Inject
    TorraServiceImpl service;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public List<TorraResponseDTO> listar() {
        return service.listar();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public TorraResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id);
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response salvar(@Valid TorraRequestDTO dto) {
        TorraResponseDTO response = service.salvar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public TorraResponseDTO atualizar(@PathParam("id") Long id, @Valid TorraRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response excluir(@PathParam("id") Long id) {
        service.excluir(id);
        return Response.noContent().build();
    }
}
