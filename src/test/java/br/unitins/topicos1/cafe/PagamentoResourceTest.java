package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class PagamentoResourceTest {

    private Number getProdutoId() {
        return given()
            .when().get("/ecommerce/produtos")
            .then().statusCode(200)
            .extract().path("[0].id");
    }

    private Number criarPedido(Number produtoId) {
        String body = "{\"itens\":[{\"produtoId\":" + produtoId + ",\"quantidade\":1}]}";
        return given()
            .contentType(ContentType.JSON).body(body)
            .when().post("/pedidos")
            .then().statusCode(201)
            .extract().path("id");
    }

    @Test
    @TestSecurity(user = "pag_pix", roles = {"USER"})
    void pagarComPix_deveRetornar201ComCodigo() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pag_pix\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number pedidoId = criarPedido(getProdutoId());

        given()
            .contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"PIX\"}")
            .when().post("/pedidos/" + pedidoId + "/pagamento")
            .then()
            .statusCode(201)
            .body("formaPagamento", equalTo("PIX"))
            .body("status", equalTo("APROVADO"))
            .body("codigoPagamento", notNullValue())
            .body("valor", greaterThan(0f));
    }

    @Test
    @TestSecurity(user = "pag_cred", roles = {"USER"})
    void pagarComCartaoCredito_deveCalcularParcelas() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pag_cred\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number pedidoId = criarPedido(getProdutoId());

        given()
            .contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"CARTAO_CREDITO\",\"parcelas\":3}")
            .when().post("/pedidos/" + pedidoId + "/pagamento")
            .then()
            .statusCode(201)
            .body("formaPagamento", equalTo("CARTAO_CREDITO"))
            .body("parcelas", equalTo(3))
            .body("valorParcela", greaterThan(0f))
            .body("status", equalTo("APROVADO"));
    }

    @Test
    @TestSecurity(user = "pag_deb", roles = {"USER"})
    void pagarComCartaoDebito_deveRetornar201() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pag_deb\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number pedidoId = criarPedido(getProdutoId());

        given()
            .contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"CARTAO_DEBITO\"}")
            .when().post("/pedidos/" + pedidoId + "/pagamento")
            .then()
            .statusCode(201)
            .body("formaPagamento", equalTo("CARTAO_DEBITO"))
            .body("parcelas", equalTo(1))
            .body("status", equalTo("APROVADO"));
    }

    @Test
    @TestSecurity(user = "pag_bol", roles = {"USER"})
    void pagarComBoleto_deveRetornar201ComCodigo() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pag_bol\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number pedidoId = criarPedido(getProdutoId());

        given()
            .contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"BOLETO\"}")
            .when().post("/pedidos/" + pedidoId + "/pagamento")
            .then()
            .statusCode(201)
            .body("formaPagamento", equalTo("BOLETO"))
            .body("codigoPagamento", notNullValue())
            .body("status", equalTo("APROVADO"));
    }

    @Test
    @TestSecurity(user = "pag_deb2", roles = {"USER"})
    void pagarComDebitoParcelado_deveRetornarErro() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pag_deb2\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number pedidoId = criarPedido(getProdutoId());

        given()
            .contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"CARTAO_DEBITO\",\"parcelas\":3}")
            .when().post("/pedidos/" + pedidoId + "/pagamento")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)));
    }

    @Test
    @TestSecurity(user = "pag_duplo", roles = {"USER"})
    void pagarPedidoJaPago_deveRetornarErro() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pag_duplo\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number pedidoId = criarPedido(getProdutoId());

        given().contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"PIX\"}")
            .when().post("/pedidos/" + pedidoId + "/pagamento")
            .then().statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"PIX\"}")
            .when().post("/pedidos/" + pedidoId + "/pagamento")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)));
    }

    @Test
    @TestSecurity(user = "pag_404", roles = {"USER"})
    void pagarPedidoInexistente_deveRetornar404() {
        given().contentType(ContentType.JSON)
            .body("{\"login\":\"pag_404\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"PIX\"}")
            .when().post("/pedidos/99999/pagamento")
            .then()
            .statusCode(404);
    }
}
