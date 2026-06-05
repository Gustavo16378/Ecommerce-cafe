package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class ListaDesejoResourceTest {

    private Number getProdutoId() {
        return given()
            .when().get("/ecommerce/produtos")
            .then().statusCode(200)
            .extract().path("[0].id");
    }

    @Test
    @TestSecurity(user = "lista_add", roles = {"USER"})
    void adicionarERemoverProduto_deveFuncionar() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"lista_add\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = getProdutoId();

        given()
            .contentType(ContentType.JSON)
            .when().post("/lista-desejos/" + produtoId)
            .then()
            .statusCode(200)
            .body("produtos", hasSize(greaterThanOrEqualTo(1)));

        given()
            .when().get("/lista-desejos")
            .then()
            .statusCode(200)
            .body("produtos", hasSize(greaterThanOrEqualTo(1)));

        given()
            .when().delete("/lista-desejos/" + produtoId)
            .then()
            .statusCode(204);
    }

    @Test
    @TestSecurity(user = "lista_vazia", roles = {"USER"})
    void buscar_semLista_deveRetornar404() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"lista_vazia\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().get("/lista-desejos")
            .then()
            .statusCode(404);
    }

    @Test
    @TestSecurity(user = "lista_404", roles = {"USER"})
    void adicionarProdutoInexistente_deveRetornar404() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"lista_404\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON)
            .when().post("/lista-desejos/99999")
            .then()
            .statusCode(404);
    }
}
