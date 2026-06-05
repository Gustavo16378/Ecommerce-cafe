package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.MaterialEmbalagemRequestDTO;
import br.unitins.topicos1.cafe.dto.MaterialEmbalagemResponseDTO;
import br.unitins.topicos1.cafe.service.MaterialEmbalagemServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/materiais-embalagem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaterialEmbalagemResource {

    @Inject
    MaterialEmbalagemServiceImpl service;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public List<MaterialEmbalagemResponseDTO> listar() {
        return service.listar();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public MaterialEmbalagemResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id);
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response salvar(@Valid MaterialEmbalagemRequestDTO dto) {
        MaterialEmbalagemResponseDTO response = service.salvar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public MaterialEmbalagemResponseDTO atualizar(@PathParam("id") Long id, @Valid MaterialEmbalagemRequestDTO dto) {
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
