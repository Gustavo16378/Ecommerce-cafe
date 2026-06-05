package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.CarrinhoResponseDTO;
import br.unitins.topicos1.cafe.dto.PedidoResponseDTO;
import br.unitins.topicos1.cafe.service.CarrinhoServiceImpl;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/carrinho")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class CarrinhoResource {

    @Inject CarrinhoServiceImpl service;
    @Inject JsonWebToken jwt;

    @GET
    public CarrinhoResponseDTO buscar() {
        return service.buscar(jwt.getSubject());
    }

    @POST
    @Path("/{produtoId}")
    public CarrinhoResponseDTO adicionarItem(
            @PathParam("produtoId") Long produtoId,
            @QueryParam("quantidade") @DefaultValue("1") Integer quantidade) {
        return service.adicionarItem(jwt.getSubject(), produtoId, quantidade);
    }

    @PATCH
    @Path("/{produtoId}")
    public CarrinhoResponseDTO atualizarQuantidade(
            @PathParam("produtoId") Long produtoId,
            @QueryParam("quantidade") Integer quantidade) {
        return service.atualizarQuantidade(jwt.getSubject(), produtoId, quantidade);
    }

    @DELETE
    @Path("/{produtoId}")
    public Response removerItem(@PathParam("produtoId") Long produtoId) {
        service.removerItem(jwt.getSubject(), produtoId);
        return Response.noContent().build();
    }

    @DELETE
    public Response limpar() {
        service.limpar(jwt.getSubject());
        return Response.noContent().build();
    }

    @POST
    @Path("/checkout")
    public Response checkout() {
        PedidoResponseDTO pedido = service.checkout(jwt.getSubject());
        return Response.status(Response.Status.CREATED).entity(pedido).build();
    }
}
