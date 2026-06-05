package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
@TestSecurity(user = "admin", roles = {"ADMIN"})
public class CategoriaResourceTest {

    @Test
    void listar_deveRetornar200() {
        given()
            .when().get("/categorias")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void crud_basico_deveFuncionar() {
        Number id =
            given()
                .contentType(ContentType.JSON)
                .body("{\"nome\":\"Categoria Teste\"}")
            .when()
                .post("/categorias")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", equalTo("Categoria Teste"))
                .extract()
                .path("id");

        given()
            .when().get("/categorias/{id}", id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("nome", equalTo("Categoria Teste"));

        given()
            .contentType(ContentType.JSON)
            .body("{\"nome\":\"Categoria Atualizada\"}")
        .when()
            .put("/categorias/{id}", id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("nome", equalTo("Categoria Atualizada"));

        given()
            .when().delete("/categorias/{id}", id)
            .then()
            .statusCode(204);

        given()
            .when().get("/categorias/{id}", id)
            .then()
            .statusCode(404);
    }

    @Test
    void salvar_semNome_deveRetornar400() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/categorias")
        .then()
            .statusCode(400);
    }
}
