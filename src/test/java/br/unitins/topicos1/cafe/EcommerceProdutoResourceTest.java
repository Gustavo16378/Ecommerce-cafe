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
            .contentType(ContentType.JSON)
            .body("$", not(empty())); // DataInitializerService já inseriu 4 produtos
    }

    @Test
    void listar_comFiltroNome_deveRetornarProdutos() {
        given()
            .queryParam("nome", "Café")
            .when().get("/ecommerce/produtos")
            .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    void listar_comFiltroNomeInexistente_deveRetornarListaVazia() {
        given()
            .queryParam("nome", "produtoxyz99999")
            .when().get("/ecommerce/produtos")
            .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    @Test
    void listar_comFiltroFornecedor_deveRetornarProdutos() {
        given()
            .queryParam("fornecedor", "Fazenda")
            .when().get("/ecommerce/produtos")
            .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    void listar_comFiltroMaterialEmbalagem_deveRetornarProdutos() {
        given()
            .queryParam("materialEmbalagem", "Papel")
            .when().get("/ecommerce/produtos")
            .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    void listar_comMultiplosFiltros_deveRetornar200() {
        given()
            .queryParam("nome", "Café")
            .queryParam("fornecedor", "Fazenda")
            .when().get("/ecommerce/produtos")
            .then()
            .statusCode(200);
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
    void buscarPorId_existente_deveRetornarCamposCorretos() {
        Number catId = criarCategoria("Cat Eco Test");
        Number torraId = criarTorra("MEDIA");
        Number tamId = criarTamanho(300);
        Number matId = criarMaterial("Kraft Test");
        Number fornId = criarFornecedor("Fazenda Eco Test", "22222222000001");

        String body = "{"
            + "\"nome\":\"Cafe Ecommerce Test\","
            + "\"descricao\":\"Produto de teste\","
            + "\"preco\":35.0,"
            + "\"ativo\":true,"
            + "\"torraId\":" + torraId + ","
            + "\"tamanhoEmbalagemId\":" + tamId + ","
            + "\"materialEmbalagemId\":" + matId + ","
            + "\"categoriasIds\":[" + catId + "],"
            + "\"fornecedorId\":" + fornId
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
            .body("nome", equalTo("Cafe Ecommerce Test"))
            .body("preco", equalTo(35.0f))
            .body("estoqueDisponivel", notNullValue());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void buscarPorId_produtoInativo_deveRetornar404() {
        Number catId = criarCategoria("Cat Inativo");
        Number torraId = criarTorra("CLARA");
        Number tamId = criarTamanho(250);
        Number matId = criarMaterial("Plastico Inativo");
        Number fornId = criarFornecedor("Forn Inativo", "22222222000002");

        String body = "{"
            + "\"nome\":\"Cafe Inativo\","
            + "\"descricao\":\"Produto inativo\","
            + "\"preco\":20.0,"
            + "\"ativo\":false,"
            + "\"torraId\":" + torraId + ","
            + "\"tamanhoEmbalagemId\":" + tamId + ","
            + "\"materialEmbalagemId\":" + matId + ","
            + "\"categoriasIds\":[" + catId + "],"
            + "\"fornecedorId\":" + fornId
            + "}";

        Number produtoId = given()
            .contentType(ContentType.JSON).body(body)
            .when().post("/produtos")
            .then().statusCode(201)
            .extract().path("id");

        given()
            .when().get("/ecommerce/produtos/" + produtoId)
            .then()
            .statusCode(404);
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
            + "\"contato\":\"eco@teste.com\","
            + "\"endereco\":{\"rua\":\"Rua Eco\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77000000\"}}";
        return given().contentType(ContentType.JSON).body(body)
            .when().post("/fornecedores").then().statusCode(201).extract().path("id");
    }
}
