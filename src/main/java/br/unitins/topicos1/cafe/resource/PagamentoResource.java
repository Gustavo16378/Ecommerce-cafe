package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.PagamentoRequestDTO;
import br.unitins.topicos1.cafe.dto.PagamentoResponseDTO;
import br.unitins.topicos1.cafe.service.PagamentoServiceImpl;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pedidos/{pedidoId}/pagamento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class PagamentoResource {

    @Inject PagamentoServiceImpl service;
    @Inject SecurityIdentity identity;

    @POST
    public Response pagar(@PathParam("pedidoId") Long pedidoId, @Valid PagamentoRequestDTO dto) {
        PagamentoResponseDTO response = service.pagar(identity.getPrincipal().getName(), pedidoId, dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
