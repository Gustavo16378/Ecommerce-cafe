package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.LoteEstoqueRequestDTO;
import br.unitins.topicos1.cafe.dto.LoteEstoqueResponseDTO;
import br.unitins.topicos1.cafe.service.LoteEstoqueServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/lotes-estoque")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoteEstoqueResource {

    @Inject
    LoteEstoqueServiceImpl service;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public List<LoteEstoqueResponseDTO> listar() {
        return service.listar();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public LoteEstoqueResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id);
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response salvar(@Valid LoteEstoqueRequestDTO dto) {
        LoteEstoqueResponseDTO response = service.salvar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public LoteEstoqueResponseDTO atualizar(@PathParam("id") Long id, @Valid LoteEstoqueRequestDTO dto) {
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
