package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class EcommerceProdutoResourceTest {

    @Test
    void listar_deveRetornar200() {
        given()
            .when().get("/ecommerce/produtos")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void listar_comFiltroNome_deveRetornar200() {
        given()
            .queryParam("nome", "cafe")
            .when().get("/ecommerce/produtos")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void buscarPorId_inexistente_deveRetornar404() {
        given()
            .when().get("/ecommerce/produtos/99999")
            .then()
            .statusCode(404);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void listar_comProdutoCriado_deveRetornarEstoque() {
        Number categoriaId = criarCategoria("Cat Ecommerce");
        Number torraId = criarTorra("MEDIA");
        Number tamanhoId = criarTamanho(300);
        Number materialId = criarMaterial("Plastico");
        Number fornecedorId = criarFornecedor("Forn Ecommerce", "33333333333333");

        String body = "{"
            + "\"nome\":\"Cafe Ecommerce\","
            + "\"descricao\":\"Produto para ecommerce\","
            + "\"preco\":25.0,"
            + "\"ativo\":true,"
            + "\"torraId\":" + torraId + ","
            + "\"tamanhoEmbalagemId\":" + tamanhoId + ","
            + "\"materialEmbalagemId\":" + materialId + ","
            + "\"categoriasIds\":[" + categoriaId + "],"
            + "\"fornecedorId\":" + fornecedorId
            + "}";

        Number produtoId = given()
            .contentType(ContentType.JSON).body(body)
            .when().post("/produtos")
            .then().statusCode(201)
            .extract().path("id");

        given()
            .when().get("/ecommerce/produtos/" + produtoId)
            .then()
            .statusCode(200)
            .body("id", equalTo(produtoId.intValue()))
            .body("nome", equalTo("Cafe Ecommerce"))
            .body("estoqueDisponivel", notNullValue());
    }

    private Number criarCategoria(String nome) {
        return given().contentType(ContentType.JSON).body("{\"nome\":\"" + nome + "\"}")
            .when().post("/categorias").then().statusCode(201).extract().path("id");
    }
    private Number criarTorra(String tipo) {
        return given().contentType(ContentType.JSON).body("{\"tipo\":\"" + tipo + "\"}")
            .when().post("/torras").then().statusCode(201).extract().path("id");
    }
    private Number criarTamanho(int gramas) {
        return given().contentType(ContentType.JSON).body("{\"gramas\":" + gramas + "}")
            .when().post("/tamanhos-embalagem").then().statusCode(201).extract().path("id");
    }
    private Number criarMaterial(String nome) {
        return given().contentType(ContentType.JSON).body("{\"nome\":\"" + nome + "\"}")
            .when().post("/materiais-embalagem").then().statusCode(201).extract().path("id");
    }
    private Number criarFornecedor(String nome, String cnpj) {
        String body = "{\"nome\":\"" + nome + "\",\"cnpj\":\"" + cnpj + "\","
            + "\"contato\":\"contato@teste.com\","
            + "\"endereco\":{\"rua\":\"Rua A\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77000000\"}}";
        return given().contentType(ContentType.JSON).body(body)
            .when().post("/fornecedores").then().statusCode(201).extract().path("id");
    }
}
