package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class UsuarioResourceTest {

    @Test
    void cadastroSimples_deveRetornar201() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_simples\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples")
            .then()
            .statusCode(201)
            .body("login", equalTo("cliente_simples"));
    }

    @Test
    void cadastroCompleto_deveRetornar201() {
        String body = "{"
            + "\"login\":\"cliente_completo\","
            + "\"senha\":\"senha123\","
            + "\"nome\":\"João Silva\","
            + "\"cpf\":\"12345678901\","
            + "\"email\":\"joao@email.com\","
            + "\"telefone\":\"63999999999\","
            + "\"enderecos\":[{\"rua\":\"Rua B\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77001000\"}]"
            + "}";

        given()
            .contentType(ContentType.JSON).body(body)
            .when().post("/usuarios/cadastro/completo")
            .then()
            .statusCode(201)
            .body("login", equalTo("cliente_completo"));
    }

    @Test
    void cadastroSimples_semCampos_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON).body("{}")
            .when().post("/usuarios/cadastro/simples")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)));
    }

    @Test
    void esqueceuSenha_deveRetornar204() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_simples\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_simples\",\"novaSenha\":\"novaSenha456\"}")
            .when().patch("/usuarios/esqueceu-senha")
            .then()
            .statusCode(204);
    }

    @Test
    @TestSecurity(user = "cliente_jwt", roles = {"USER"})
    void alterarSenha_deveRetornar204() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_jwt\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON)
            .body("{\"senhaAtual\":\"senha123\",\"novaSenha\":\"novaSenha789\"}")
            .when().patch("/usuarios/senha")
            .then()
            .statusCode(204);
    }

    @Test
    @TestSecurity(user = "cliente_perfil", roles = {"USER"})
    void editarPerfil_deveRetornar200() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_perfil\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON)
            .body("{\"nome\":\"Maria\",\"email\":\"maria@email.com\",\"telefone\":\"63988887777\"}")
            .when().patch("/usuarios/meu-perfil")
            .then()
            .statusCode(200)
            .body("login", equalTo("cliente_perfil"));
    }

    @Test
    @TestSecurity(user = "cliente_end", roles = {"USER"})
    void meusEnderecos_deveRetornar200() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_end\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().get("/usuarios/meus-enderecos")
            .then()
            .statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void listar_comoAdmin_deveRetornar200() {
        given()
            .when().get("/usuarios")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }
}
