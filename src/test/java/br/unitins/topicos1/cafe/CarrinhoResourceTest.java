package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class CarrinhoResourceTest {

    @Test
    @TestSecurity(user = "carrinho_user", roles = {"USER"})
    void buscar_semCarrinho_deveRetornarCarrinhoVazio() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_user\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().get("/carrinho")
            .then()
            .statusCode(200)
            .body("itens", hasSize(0))
            .body("total", equalTo(0.0f));
    }

    @Test
    @TestSecurity(user = "carrinho_user2", roles = {"USER"})
    void adicionarItem_deveFuncionar() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_user2\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = criarProdutoComEstoque("Cafe Carrinho", "66666666666666");

        given()
            .when().post("/carrinho/" + produtoId + "?quantidade=2")
            .then()
            .statusCode(200)
            .body("itens", hasSize(1))
            .body("itens[0].quantidade", equalTo(2))
            .body("total", greaterThan(0f));
    }

    @Test
    @TestSecurity(user = "carrinho_user3", roles = {"USER"})
    void adicionarMesmoItem_deveAcumularQuantidade() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_user3\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = criarProdutoComEstoque("Cafe Acumulo", "66666666666667");

        given().when().post("/carrinho/" + produtoId + "?quantidade=1");
        given()
            .when().post("/carrinho/" + produtoId + "?quantidade=2")
            .then()
            .statusCode(200)
            .body("itens[0].quantidade", equalTo(3));
    }

    @Test
    @TestSecurity(user = "carrinho_user4", roles = {"USER"})
    void removerItem_deveFuncionar() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_user4\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = criarProdutoComEstoque("Cafe Remover", "66666666666668");

        given().when().post("/carrinho/" + produtoId + "?quantidade=1");

        given()
            .when().delete("/carrinho/" + produtoId)
            .then()
            .statusCode(204);

        given()
            .when().get("/carrinho")
            .then()
            .statusCode(200)
            .body("itens", hasSize(0));
    }

    @Test
    @TestSecurity(user = "carrinho_user5", roles = {"USER"})
    void limpar_deveEsvaziarCarrinho() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_user5\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = criarProdutoComEstoque("Cafe Limpar", "66666666666669");

        given().when().post("/carrinho/" + produtoId + "?quantidade=1");

        given()
            .when().delete("/carrinho")
            .then()
            .statusCode(204);

        given()
            .when().get("/carrinho")
            .then()
            .statusCode(200)
            .body("itens", hasSize(0));
    }

    @Test
    @TestSecurity(user = "carrinho_user6", roles = {"USER"})
    void checkout_deveCriarPedido() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_user6\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = criarProdutoComEstoque("Cafe Checkout", "77777777777777");

        given().when().post("/carrinho/" + produtoId + "?quantidade=1");

        given()
            .when().post("/carrinho/checkout")
            .then()
            .statusCode(201)
            .body("status", equalTo("AGUARDANDO_PAGAMENTO"))
            .body("total", greaterThan(0f));

        given()
            .when().get("/carrinho")
            .then()
            .statusCode(200)
            .body("itens", hasSize(0));
    }

    @Test
    @TestSecurity(user = "carrinho_user7", roles = {"USER"})
    void checkout_carrinhoVazio_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_user7\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().post("/carrinho/checkout")
            .then()
            .statusCode(anyOf(equalTo(404), equalTo(422)));
    }

    @Test
    void buscar_semAutenticacao_deveRetornar401() {
        given()
            .when().get("/carrinho")
            .then()
            .statusCode(401);
    }

    @TestSecurity(user = "admin", roles = {"ADMIN"})
    private Number criarProdutoComEstoque(String nome, String cnpj) {
        Number catId = given().contentType(ContentType.JSON).body("{\"nome\":\"Cat Carrinho\"}")
            .when().post("/categorias").then().statusCode(201).extract().path("id");
        Number torraId = given().contentType(ContentType.JSON).body("{\"tipo\":\"MEDIA\"}")
            .when().post("/torras").then().statusCode(201).extract().path("id");
        Number tamId = given().contentType(ContentType.JSON).body("{\"gramas\":500}")
            .when().post("/tamanhos-embalagem").then().statusCode(201).extract().path("id");
        Number matId = given().contentType(ContentType.JSON).body("{\"nome\":\"Saco\"}")
            .when().post("/materiais-embalagem").then().statusCode(201).extract().path("id");
        String fBody = "{\"nome\":\"FornCarrinho\",\"cnpj\":\"" + cnpj + "\","
            + "\"contato\":\"c@c.com\","
            + "\"endereco\":{\"rua\":\"R3\",\"cidade\":\"P3\",\"uf\":\"TO\",\"cep\":\"77000003\"}}";
        Number fornId = given().contentType(ContentType.JSON).body(fBody)
            .when().post("/fornecedores").then().statusCode(201).extract().path("id");

        String pBody = "{\"nome\":\"" + nome + "\",\"descricao\":\"desc\",\"preco\":20.0,"
            + "\"ativo\":true,\"torraId\":" + torraId + ",\"tamanhoEmbalagemId\":" + tamId
            + ",\"materialEmbalagemId\":" + matId + ",\"categoriasIds\":[" + catId + "]"
            + ",\"fornecedorId\":" + fornId + "}";
        Number produtoId = given().contentType(ContentType.JSON).body(pBody)
            .when().post("/produtos").then().statusCode(201).extract().path("id");

        given().contentType(ContentType.JSON)
            .body("{\"produtoId\":" + produtoId + ",\"quantidade\":100,\"dataValidade\":\"2027-12-31\"}")
            .when().post("/lotes-estoque").then().statusCode(201);

        return produtoId;
    }
}
