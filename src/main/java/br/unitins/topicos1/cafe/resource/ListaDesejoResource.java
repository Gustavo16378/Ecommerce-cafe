package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.ListaDesejoResponseDTO;
import br.unitins.topicos1.cafe.service.ListaDesejoServiceImpl;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/lista-desejos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class ListaDesejoResource {

    @Inject ListaDesejoServiceImpl service;
    @Inject JsonWebToken jwt;

    @GET
    public ListaDesejoResponseDTO buscar() {
        return service.buscarPorUsuario(jwt.getSubject());
    }

    @POST
    @Path("/{produtoId}")
    public Response adicionar(@PathParam("produtoId") Long produtoId) {
        return Response.ok(service.adicionarProduto(jwt.getSubject(), produtoId)).build();
    }

    @DELETE
    @Path("/{produtoId}")
    public Response remover(@PathParam("produtoId") Long produtoId) {
        service.removerProduto(jwt.getSubject(), produtoId);
        return Response.noContent().build();
    }
}
