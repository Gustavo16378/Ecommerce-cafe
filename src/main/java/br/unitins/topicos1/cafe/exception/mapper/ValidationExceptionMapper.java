package br.unitins.topicos1.cafe.exception.mapper;

import br.unitins.topicos1.cafe.exception.ProblemDetail;
import br.unitins.topicos1.cafe.exception.ValidationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ValidationException exception) {
        ProblemDetail problemDetail = new ProblemDetail(
            422,
            "Erro de validação",
            exception.getMessage()
        );

        problemDetail.setType("http://localhost:8080/errors/validation-error");

        if (uriInfo != null) {
            problemDetail.setInstance(uriInfo.getPath());
        }

        if (exception.getField() != null) {
            problemDetail.addError(exception.getField(), exception.getMessage());
        }

        return Response.status(422).entity(problemDetail).build();
    }
}
