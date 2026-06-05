package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
@TestSecurity(user = "admin", roles = {"ADMIN"})
public class FornecedorResourceTest {

    @Test
    void listar_deveRetornar200() {
        given()
            .when().get("/fornecedores")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void crud_basico_deveFuncionar() {
        String bodyCriar = "{"
            + "\"nome\":\"Fornecedor Teste\"," 
            + "\"cnpj\":\"12345678901234\"," 
            + "\"contato\":\"contato@teste.com\"," 
            + "\"endereco\":{\"rua\":\"Rua A\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77000000\"}"
            + "}";

        Number id =
            given()
                .contentType(ContentType.JSON)
                .body(bodyCriar)
            .when()
                .post("/fornecedores")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", equalTo("Fornecedor Teste"))
                .body("cnpj", equalTo("12345678901234"))
                .body("endereco.cep", equalTo("77000000"))
                .extract()
                .path("id");

        given()
            .when().get("/fornecedores/{id}", id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("nome", equalTo("Fornecedor Teste"));

        String bodyAtualizar = "{"
            + "\"nome\":\"Fornecedor Atualizado\"," 
            + "\"cnpj\":\"98765432109876\"," 
            + "\"contato\":\"(63)99999-0000\"," 
            + "\"endereco\":{\"rua\":\"Rua B\",\"cidade\":\"Araguaína\",\"uf\":\"TO\",\"cep\":\"77888888\"}"
            + "}";

        given()
            .contentType(ContentType.JSON)
            .body(bodyAtualizar)
        .when()
            .put("/fornecedores/{id}", id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("nome", equalTo("Fornecedor Atualizado"))
            .body("cnpj", equalTo("98765432109876"))
            .body("endereco.cep", equalTo("77888888"));

        given()
            .when().delete("/fornecedores/{id}", id)
            .then()
            .statusCode(204);

        given()
            .when().get("/fornecedores/{id}", id)
            .then()
            .statusCode(404);
    }

    @Test
    void salvar_semEndereco_deveRetornar400() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"nome\":\"X\",\"cnpj\":\"12345678901234\",\"contato\":\"Y\"}")
        .when()
            .post("/fornecedores")
        .then()
            .statusCode(400);
    }
}
