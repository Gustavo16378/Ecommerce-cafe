package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class PedidoResourceTest {

    private Number getProdutoId() {
        return given()
            .when().get("/ecommerce/produtos")
            .then().statusCode(200)
            .extract().path("[0].id");
    }

    @Test
    @TestSecurity(user = "pedido_hist", roles = {"USER"})
    void historico_deveRetornar200() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pedido_hist\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().get("/pedidos")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @TestSecurity(user = "pedido_compra", roles = {"USER"})
    void realizarCompra_deveFuncionar() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pedido_compra\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = getProdutoId();
        String body = "{\"itens\":[{\"produtoId\":" + produtoId + ",\"quantidade\":1}]}";

        Number pedidoId = given()
            .contentType(ContentType.JSON).body(body)
            .when().post("/pedidos")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("status", equalTo("AGUARDANDO_PAGAMENTO"))
            .body("total", greaterThan(0f))
            .extract().path("id");

        given()
            .when().get("/pedidos/" + pedidoId)
            .then()
            .statusCode(200)
            .body("id", equalTo(pedidoId.intValue()));
    }

    @Test
    @TestSecurity(user = "pedido_vazio", roles = {"USER"})
    void realizarCompra_semItens_deveRetornarErro() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pedido_vazio\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON).body("{\"itens\":[]}")
            .when().post("/pedidos")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)));
    }

    @Test
    @TestSecurity(user = "pedido_404", roles = {"USER"})
    void buscarPedidoInexistente_deveRetornar404() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pedido_404\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().get("/pedidos/99999")
            .then()
            .statusCode(404);
    }
}
