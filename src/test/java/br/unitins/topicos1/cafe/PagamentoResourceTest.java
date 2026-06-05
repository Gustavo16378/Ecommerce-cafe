package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class PagamentoResourceTest {

    @Test
    @TestSecurity(user = "pag_user_pix", roles = {"USER"})
    void pagarComPix_deveRetornar201ComCodigo() {
        criarUsuario("pag_user_pix");
        Number pedidoId = criarPedido("pag_user_pix", "Cafe Pix", "11111111000001");

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
    @TestSecurity(user = "pag_user_credito", roles = {"USER"})
    void pagarComCartaoCredito_deveCalcularParcelas() {
        criarUsuario("pag_user_credito");
        Number pedidoId = criarPedido("pag_user_credito", "Cafe Credito", "11111111000002");

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
    @TestSecurity(user = "pag_user_debito", roles = {"USER"})
    void pagarComCartaoDebito_deveRetornar201() {
        criarUsuario("pag_user_debito");
        Number pedidoId = criarPedido("pag_user_debito", "Cafe Debito", "11111111000003");

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
    @TestSecurity(user = "pag_user_boleto", roles = {"USER"})
    void pagarComBoleto_deveRetornar201ComCodigo() {
        criarUsuario("pag_user_boleto");
        Number pedidoId = criarPedido("pag_user_boleto", "Cafe Boleto", "11111111000004");

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
    @TestSecurity(user = "pag_user_debito2", roles = {"USER"})
    void pagarComDebitoParcelado_deveRetornarErro() {
        criarUsuario("pag_user_debito2");
        Number pedidoId = criarPedido("pag_user_debito2", "Cafe Debito2", "11111111000005");

        given()
            .contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"CARTAO_DEBITO\",\"parcelas\":3}")
            .when().post("/pedidos/" + pedidoId + "/pagamento")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)));
    }

    @Test
    @TestSecurity(user = "pag_user_duplo", roles = {"USER"})
    void pagarPedidoJaPago_deveRetornarErro() {
        criarUsuario("pag_user_duplo");
        Number pedidoId = criarPedido("pag_user_duplo", "Cafe Duplo", "11111111000006");

        given()
            .contentType(ContentType.JSON)
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
    @TestSecurity(user = "pag_user_404", roles = {"USER"})
    void pagarPedidoInexistente_deveRetornar404() {
        criarUsuario("pag_user_404");

        given()
            .contentType(ContentType.JSON)
            .body("{\"formaPagamento\":\"PIX\"}")
            .when().post("/pedidos/99999/pagamento")
            .then()
            .statusCode(404);
    }

    // ---------- helpers ----------

    private void criarUsuario(String login) {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"" + login + "\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");
    }

    @TestSecurity(user = "admin", roles = {"ADMIN"})
    private Number criarPedido(String userLogin, String nomeProduto, String cnpj) {
        Number catId = given().contentType(ContentType.JSON).body("{\"nome\":\"Cat Pag\"}")
            .when().post("/categorias").then().statusCode(201).extract().path("id");
        Number torraId = given().contentType(ContentType.JSON).body("{\"tipo\":\"MEDIA\"}")
            .when().post("/torras").then().statusCode(201).extract().path("id");
        Number tamId = given().contentType(ContentType.JSON).body("{\"gramas\":250}")
            .when().post("/tamanhos-embalagem").then().statusCode(201).extract().path("id");
        Number matId = given().contentType(ContentType.JSON).body("{\"nome\":\"Kraft Pag\"}")
            .when().post("/materiais-embalagem").then().statusCode(201).extract().path("id");
        String fBody = "{\"nome\":\"FornPag\",\"cnpj\":\"" + cnpj + "\","
            + "\"contato\":\"pag@pag.com\","
            + "\"endereco\":{\"rua\":\"Rua Pag\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77000099\"}}";
        Number fornId = given().contentType(ContentType.JSON).body(fBody)
            .when().post("/fornecedores").then().statusCode(201).extract().path("id");

        String pBody = "{\"nome\":\"" + nomeProduto + "\",\"descricao\":\"desc\",\"preco\":49.90,"
            + "\"ativo\":true,\"torraId\":" + torraId + ",\"tamanhoEmbalagemId\":" + tamId
            + ",\"materialEmbalagemId\":" + matId + ",\"categoriasIds\":[" + catId + "]"
            + ",\"fornecedorId\":" + fornId + "}";
        Number produtoId = given().contentType(ContentType.JSON).body(pBody)
            .when().post("/produtos").then().statusCode(201).extract().path("id");

        given().contentType(ContentType.JSON)
            .body("{\"produtoId\":" + produtoId + ",\"quantidade\":100,\"dataValidade\":\"2027-12-31\"}")
            .when().post("/lotes-estoque").then().statusCode(201);

        String pedidoBody = "{\"itens\":[{\"produtoId\":" + produtoId + ",\"quantidade\":1}]}";
        return given()
            .contentType(ContentType.JSON).body(pedidoBody)
            .when().post("/pedidos")
            .then().statusCode(201)
            .extract().path("id");
    }
}
