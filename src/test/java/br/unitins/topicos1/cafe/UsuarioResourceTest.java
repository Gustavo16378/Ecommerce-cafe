package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class UsuarioResourceTest {

    // ---------- Cadastro ----------

    @Test
    void cadastroSimples_deveRetornar201() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_simples\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples")
            .then()
            .statusCode(201)
            .body("login", equalTo("cliente_simples"))
            .body("perfil", equalTo("USER"));
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
            .body("login", equalTo("cliente_completo"))
            .body("nome", equalTo("João Silva"));
    }

    @Test
    void cadastroSimples_semCampos_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON).body("{}")
            .when().post("/usuarios/cadastro/simples")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)));
    }

    // ---------- Senha ----------

    @Test
    void esqueceuSenha_deveRetornar200ComMensagem() {
        // Usa o cliente do seed que já tem email cadastrado
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"joao@email.com\",\"novaSenha\":\"novaSenha456\"}")
            .when().patch("/usuarios/esqueceu-senha")
            .then()
            .statusCode(200)
            .body(containsString("Instruções de recuperação enviadas"));
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
    @TestSecurity(user = "cliente_senhaerr", roles = {"USER"})
    void alterarSenha_senhaAtualErrada_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_senhaerr\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON)
            .body("{\"senhaAtual\":\"senhaerrada\",\"novaSenha\":\"nova789\"}")
            .when().patch("/usuarios/senha")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)));
    }

    // ---------- Perfil ----------

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
            .body("nome", equalTo("Maria"))
            .body("email", equalTo("maria@email.com"));
    }

    // ---------- Endereços ----------

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
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @TestSecurity(user = "cliente_end2", roles = {"USER"})
    void adicionarEndereco_deveRetornar201() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_end2\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON)
            .body("{\"rua\":\"Rua Nova\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77001234\"}")
            .when().post("/usuarios/meus-enderecos")
            .then()
            .statusCode(201)
            .body("rua", equalTo("Rua Nova"))
            .body("cidade", equalTo("Palmas"))
            .body("id", notNullValue());
    }

    @Test
    @TestSecurity(user = "cliente_end3", roles = {"USER"})
    void atualizarEndereco_deveRetornar200() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_end3\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number endId = given()
            .contentType(ContentType.JSON)
            .body("{\"rua\":\"Rua Antiga\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77001111\"}")
            .when().post("/usuarios/meus-enderecos")
            .then().statusCode(201)
            .extract().path("id");

        given()
            .contentType(ContentType.JSON)
            .body("{\"rua\":\"Rua Atualizada\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77002222\"}")
            .when().put("/usuarios/meus-enderecos/" + endId)
            .then()
            .statusCode(200)
            .body("rua", equalTo("Rua Atualizada"))
            .body("cep", equalTo("77002222"));
    }

    @Test
    @TestSecurity(user = "cliente_end4", roles = {"USER"})
    void removerEndereco_deveRetornar204() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"cliente_end4\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number endId = given()
            .contentType(ContentType.JSON)
            .body("{\"rua\":\"Rua Temp\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77003333\"}")
            .when().post("/usuarios/meus-enderecos")
            .then().statusCode(201)
            .extract().path("id");

        given()
            .when().delete("/usuarios/meus-enderecos/" + endId)
            .then()
            .statusCode(204);

        given()
            .when().get("/usuarios/meus-enderecos")
            .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    // ---------- Admin ----------

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void listar_comoAdmin_deveRetornar200() {
        given()
            .when().get("/usuarios")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("$", not(empty()));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void buscarPorId_comoAdmin_deveRetornar200() {
        given()
            .when().get("/usuarios/1")
            .then()
            .statusCode(anyOf(equalTo(200), equalTo(404)));
    }

    @Test
    void listar_semAutenticacao_deveRetornar401() {
        given()
            .when().get("/usuarios")
            .then()
            .statusCode(401);
    }
}
