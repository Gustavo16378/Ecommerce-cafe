package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class AuthResourceTest {

    @Test
    void login_comAdminValido_deveRetornar200ComToken() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"admin\",\"senha\":\"admin123\"}")
            .when().post("/auth/login")
            .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("tipo", equalTo("Bearer"));
    }

    @Test
    void login_comClienteValido_deveRetornar200ComToken() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente\",\"senha\":\"cliente123\"}")
            .when().post("/auth/login")
            .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("tipo", equalTo("Bearer"));
    }

    @Test
    void login_senhaErrada_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"admin\",\"senha\":\"senhaerrada\"}")
            .when().post("/auth/login")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(401)));
    }

    @Test
    void login_usuarioInexistente_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"naoexiste\",\"senha\":\"qualquer\"}")
            .when().post("/auth/login")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(401)));
    }

    @Test
    void login_semCampos_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when().post("/auth/login")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(401), equalTo(422)));
    }

    @Test
    void login_semBody_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON)
            .when().post("/auth/login")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(422), equalTo(500)));
    }
}
