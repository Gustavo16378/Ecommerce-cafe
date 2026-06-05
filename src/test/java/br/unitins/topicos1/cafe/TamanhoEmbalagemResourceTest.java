package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
@TestSecurity(user = "admin", roles = {"ADMIN"})
public class TamanhoEmbalagemResourceTest {

    @Test
    void listar_deveRetornar200() {
        given()
            .when().get("/tamanhos-embalagem")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void crud_basico_deveFuncionar() {
        Number id =
            given()
                .contentType(ContentType.JSON)
                .body("{\"gramas\":250}")
            .when()
                .post("/tamanhos-embalagem")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("gramas", equalTo(250))
                .extract()
                .path("id");

        given()
            .when().get("/tamanhos-embalagem/{id}", id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("gramas", equalTo(250));

        given()
            .contentType(ContentType.JSON)
            .body("{\"gramas\":500}")
        .when()
            .put("/tamanhos-embalagem/{id}", id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("gramas", equalTo(500));

        given()
            .when().delete("/tamanhos-embalagem/{id}", id)
            .then()
            .statusCode(204);

        given()
            .when().get("/tamanhos-embalagem/{id}", id)
            .then()
            .statusCode(404);
    }

    @Test
    void salvar_semGramas_deveRetornarErroValidacao() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/tamanhos-embalagem")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)));
    }
}
