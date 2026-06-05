package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class CarrinhoResourceTest {

    // Busca o primeiro produto disponível (criado pelo DataInitializerService)
    private Number getProdutoId() {
        return given()
            .when().get("/ecommerce/produtos")
            .then().statusCode(200)
            .extract().path("[0].id");
    }

    @Test
    @TestSecurity(user = "carrinho_vazio", roles = {"USER"})
    void buscar_semCarrinho_deveRetornarCarrinhoVazio() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_vazio\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().get("/carrinho")
            .then()
            .statusCode(200)
            .body("itens", hasSize(0))
            .body("total", equalTo(0.0f));
    }

    @Test
    @TestSecurity(user = "carrinho_add", roles = {"USER"})
    void adicionarItem_deveFuncionar() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_add\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = getProdutoId();

        given()
            .contentType(ContentType.JSON)
            .when().post("/carrinho/" + produtoId + "?quantidade=2")
            .then()
            .statusCode(200)
            .body("itens", hasSize(1))
            .body("itens[0].quantidade", equalTo(2))
            .body("total", greaterThan(0f));
    }

    @Test
    @TestSecurity(user = "carrinho_acum", roles = {"USER"})
    void adicionarMesmoItem_deveAcumularQuantidade() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_acum\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = getProdutoId();

        given().contentType(ContentType.JSON).when().post("/carrinho/" + produtoId + "?quantidade=1");
        given()
            .contentType(ContentType.JSON)
            .when().post("/carrinho/" + produtoId + "?quantidade=2")
            .then()
            .statusCode(200)
            .body("itens[0].quantidade", equalTo(3));
    }

    @Test
    @TestSecurity(user = "carrinho_rem", roles = {"USER"})
    void removerItem_deveFuncionar() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_rem\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = getProdutoId();

        given().contentType(ContentType.JSON).when().post("/carrinho/" + produtoId + "?quantidade=1");

        given()
            .when().delete("/carrinho/" + produtoId)
            .then().statusCode(204);

        given()
            .when().get("/carrinho")
            .then()
            .statusCode(200)
            .body("itens", hasSize(0));
    }

    @Test
    @TestSecurity(user = "carrinho_limpar", roles = {"USER"})
    void limpar_deveEsvaziarCarrinho() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_limpar\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = getProdutoId();

        given().contentType(ContentType.JSON).when().post("/carrinho/" + produtoId + "?quantidade=1");

        given().contentType(ContentType.JSON).when().delete("/carrinho").then().statusCode(204);

        given()
            .when().get("/carrinho")
            .then()
            .statusCode(200)
            .body("itens", hasSize(0));
    }

    @Test
    @TestSecurity(user = "carrinho_checkout", roles = {"USER"})
    void checkout_deveCriarPedidoELimparCarrinho() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_checkout\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = getProdutoId();

        given().contentType(ContentType.JSON).when().post("/carrinho/" + produtoId + "?quantidade=1");

        given()
            .contentType(ContentType.JSON)
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
    @TestSecurity(user = "carrinho_chk_vazio", roles = {"USER"})
    void checkout_carrinhoVazio_deveRetornarErro() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"carrinho_chk_vazio\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .contentType(ContentType.JSON)
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
}
