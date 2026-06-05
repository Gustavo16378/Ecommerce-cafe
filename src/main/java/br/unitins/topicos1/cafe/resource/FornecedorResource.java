package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.FornecedorRequestDTO;
import br.unitins.topicos1.cafe.dto.FornecedorResponseDTO;
import br.unitins.topicos1.cafe.service.FornecedorServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/fornecedores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FornecedorResource {

    @Inject
    FornecedorServiceImpl service;

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public List<FornecedorResponseDTO> listar() {
        return service.listar();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public FornecedorResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id);
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response salvar(@Valid FornecedorRequestDTO dto) {
        FornecedorResponseDTO response = service.salvar(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public FornecedorResponseDTO atualizar(@PathParam("id") Long id, @Valid FornecedorRequestDTO dto) {
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
