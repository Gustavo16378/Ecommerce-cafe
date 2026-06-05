package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.CarrinhoResponseDTO;
import br.unitins.topicos1.cafe.dto.PedidoResponseDTO;
import br.unitins.topicos1.cafe.service.CarrinhoServiceImpl;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/carrinho")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class CarrinhoResource {

    @Inject CarrinhoServiceImpl service;
    @Inject SecurityIdentity identity;

    @GET
    public CarrinhoResponseDTO buscar() {
        return service.buscar(identity.getPrincipal().getName());
    }

    @POST
    @Path("/{produtoId}")
    public CarrinhoResponseDTO adicionarItem(
            @PathParam("produtoId") Long produtoId,
            @QueryParam("quantidade") @DefaultValue("1") Integer quantidade) {
        return service.adicionarItem(identity.getPrincipal().getName(), produtoId, quantidade);
    }

    @PATCH
    @Path("/{produtoId}")
    public CarrinhoResponseDTO atualizarQuantidade(
            @PathParam("produtoId") Long produtoId,
            @QueryParam("quantidade") Integer quantidade) {
        return service.atualizarQuantidade(identity.getPrincipal().getName(), produtoId, quantidade);
    }

    @DELETE
    @Path("/{produtoId}")
    public Response removerItem(@PathParam("produtoId") Long produtoId) {
        service.removerItem(identity.getPrincipal().getName(), produtoId);
        return Response.noContent().build();
    }

    @DELETE
    public Response limpar() {
        service.limpar(identity.getPrincipal().getName());
        return Response.noContent().build();
    }

    @POST
    @Path("/checkout")
    public Response checkout() {
        PedidoResponseDTO pedido = service.checkout(identity.getPrincipal().getName());
        return Response.status(Response.Status.CREATED).entity(pedido).build();
    }
}
