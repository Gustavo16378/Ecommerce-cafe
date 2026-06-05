package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
@TestSecurity(user = "admin", roles = {"ADMIN"})
public class TorraResourceTest {

    @Test
    void listar_deveRetornar200() {
        given()
            .when().get("/torras")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void crud_basico_deveFuncionar() {
        Number id =
            given()
                .contentType(ContentType.JSON)
                .body("{\"tipo\":\"MEDIA\"}")
            .when()
                .post("/torras")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("tipo", equalTo("MEDIA"))
                .extract()
                .path("id");

        given()
            .when().get("/torras/{id}", id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("tipo", equalTo("MEDIA"));

        given()
            .contentType(ContentType.JSON)
            .body("{\"tipo\":\"ESCURA\"}")
        .when()
            .put("/torras/{id}", id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("tipo", equalTo("ESCURA"));

        given()
            .when().delete("/torras/{id}", id)
            .then()
            .statusCode(204);

        given()
            .when().get("/torras/{id}", id)
            .then()
            .statusCode(404);
    }

    @Test
    void salvar_semTipo_deveRetornar400() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/torras")
        .then()
            .statusCode(400);
    }
}
