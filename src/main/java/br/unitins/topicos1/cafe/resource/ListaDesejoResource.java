package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.ListaDesejoResponseDTO;
import br.unitins.topicos1.cafe.service.ListaDesejoServiceImpl;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/lista-desejos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class ListaDesejoResource {

    @Inject ListaDesejoServiceImpl service;
    @Inject SecurityIdentity identity;

    @GET
    public ListaDesejoResponseDTO buscar() {
        return service.buscarPorUsuario(identity.getPrincipal().getName());
    }

    @POST
    @Path("/{produtoId}")
    public Response adicionar(@PathParam("produtoId") Long produtoId) {
        return Response.ok(service.adicionarProduto(identity.getPrincipal().getName(), produtoId)).build();
    }

    @DELETE
    @Path("/{produtoId}")
    public Response remover(@PathParam("produtoId") Long produtoId) {
        service.removerProduto(identity.getPrincipal().getName(), produtoId);
        return Response.noContent().build();
    }
}
