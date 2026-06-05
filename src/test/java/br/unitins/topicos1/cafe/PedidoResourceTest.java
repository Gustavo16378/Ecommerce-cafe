package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class PedidoResourceTest {

    @Test
    @TestSecurity(user = "pedido_user", roles = {"USER"})
    void historico_deveRetornar200() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"pedido_user\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().get("/pedidos")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @TestSecurity(user = "pedido_user2", roles = {"USER"})
    void realizarCompra_deveFuncionar() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"pedido_user2\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = criarProdutoAtivo("Cafe Pedido", "55555555555555");

        given()
            .contentType(ContentType.JSON)
            .body("{\"produtoId\":" + produtoId + ",\"quantidade\":100,\"dataValidade\":\"2027-01-01\"}")
            .when().post("/lotes-estoque")
            .then().statusCode(201);

        String pedidoBody = "{\"itens\":[{\"produtoId\":" + produtoId + ",\"quantidade\":1}]}";

        Number pedidoId = given()
            .contentType(ContentType.JSON).body(pedidoBody)
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
    @TestSecurity(user = "pedido_user3", roles = {"USER"})
    void realizarCompra_semItens_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"pedido_user3\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON).body("{\"itens\":[]}")
            .when().post("/pedidos")
            .then()
            .statusCode(anyOf(equalTo(400), equalTo(422)));
    }

    @Test
    @TestSecurity(user = "pedido_user4", roles = {"USER"})
    void buscarPedidoInexistente_deveRetornar404() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"pedido_user4\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().get("/pedidos/99999")
            .then()
            .statusCode(404);
    }

    @TestSecurity(user = "admin", roles = {"ADMIN"})
    private Number criarProdutoAtivo(String nome, String cnpj) {
        Number catId = given().contentType(ContentType.JSON).body("{\"nome\":\"Cat Pedido\"}")
            .when().post("/categorias").then().statusCode(201).extract().path("id");
        Number torraId = given().contentType(ContentType.JSON).body("{\"tipo\":\"ESCURA\"}")
            .when().post("/torras").then().statusCode(201).extract().path("id");
        Number tamId = given().contentType(ContentType.JSON).body("{\"gramas\":400}")
            .when().post("/tamanhos-embalagem").then().statusCode(201).extract().path("id");
        Number matId = given().contentType(ContentType.JSON).body("{\"nome\":\"Metal\"}")
            .when().post("/materiais-embalagem").then().statusCode(201).extract().path("id");
        String fBody = "{\"nome\":\"FornPedido\",\"cnpj\":\"" + cnpj + "\","
            + "\"contato\":\"c@c.com\","
            + "\"endereco\":{\"rua\":\"R2\",\"cidade\":\"P2\",\"uf\":\"TO\",\"cep\":\"77000002\"}}";
        Number fornId = given().contentType(ContentType.JSON).body(fBody)
            .when().post("/fornecedores").then().statusCode(201).extract().path("id");

        String pBody = "{\"nome\":\"" + nome + "\",\"descricao\":\"desc\",\"preco\":30.0,"
            + "\"ativo\":true,\"torraId\":" + torraId + ",\"tamanhoEmbalagemId\":" + tamId
            + ",\"materialEmbalagemId\":" + matId + ",\"categoriasIds\":[" + catId + "]"
            + ",\"fornecedorId\":" + fornId + "}";
        return given().contentType(ContentType.JSON).body(pBody)
            .when().post("/produtos").then().statusCode(201).extract().path("id");
    }
}
