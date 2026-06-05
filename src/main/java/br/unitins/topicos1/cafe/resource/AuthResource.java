package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.AuthResponseDTO;
import br.unitins.topicos1.cafe.dto.LoginRequestDTO;
import br.unitins.topicos1.cafe.service.AuthServiceImpl;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthServiceImpl authService;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(LoginRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return Response.ok(response).build();
    }
}
