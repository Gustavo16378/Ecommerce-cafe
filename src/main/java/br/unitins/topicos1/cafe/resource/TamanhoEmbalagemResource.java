package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.TamanhoEmbalagemRequestDTO;
import br.unitins.topicos1.cafe.dto.TamanhoEmbalagemResponseDTO;
import br.unitins.topicos1.cafe.service.TamanhoEmbalagemServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/tamanhos-embalagem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TamanhoEmbalagemResource {

    @Inject
    TamanhoEmbalagemServiceImpl service;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public List<TamanhoEmbalagemResponseDTO> listar() {
        return service.listar();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public TamanhoEmbalagemResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id);
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response salvar(@Valid TamanhoEmbalagemRequestDTO dto) {
        TamanhoEmbalagemResponseDTO response = service.salvar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public TamanhoEmbalagemResponseDTO atualizar(@PathParam("id") Long id, @Valid TamanhoEmbalagemRequestDTO dto) {
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
