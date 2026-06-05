package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.PagamentoRequestDTO;
import br.unitins.topicos1.cafe.dto.PagamentoResponseDTO;
import br.unitins.topicos1.cafe.service.PagamentoServiceImpl;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/pedidos/{pedidoId}/pagamento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "USER"})
public class PagamentoResource {

    @Inject PagamentoServiceImpl service;
    @Inject JsonWebToken jwt;

    @POST
    public Response pagar(@PathParam("pedidoId") Long pedidoId, @Valid PagamentoRequestDTO dto) {
        PagamentoResponseDTO response = service.pagar(jwt.getSubject(), pedidoId, dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
