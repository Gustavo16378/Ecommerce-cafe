package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.PedidoRequestDTO;
import br.unitins.topicos1.cafe.dto.PedidoResponseDTO;
import br.unitins.topicos1.cafe.service.PedidoServiceImpl;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class PedidoResource {

    @Inject PedidoServiceImpl service;
    @Inject SecurityIdentity identity;

    // Realizar compra
    @POST
    public Response realizarCompra(@Valid PedidoRequestDTO dto) {
        return Response.status(Response.Status.CREATED)
                .entity(service.realizarCompra(identity.getPrincipal().getName(), dto))
                .build();
    }

    // Histórico de compras do usuário logado
    @GET
    public List<PedidoResponseDTO> historico() {
        return service.historico(identity.getPrincipal().getName());
    }

    // Buscar pedido específico por ID
    @GET
    @Path("/{id}")
    public PedidoResponseDTO buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(identity.getPrincipal().getName(), id);
    }
}
