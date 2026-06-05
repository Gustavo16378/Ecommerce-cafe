package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
@TestSecurity(user = "admin", roles = {"ADMIN"})
public class LoteEstoqueResourceTest {

    @Test
    void listar_deveRetornar200() {
        given()
            .when().get("/lotes-estoque")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void crud_basico_deveFuncionar() {
        Number produtoId = criarProduto();

        String bodyCriar = "{"
            + "\"produtoId\":" + produtoId.intValue() + ","
            + "\"quantidade\":100,"
            + "\"dataValidade\":\"2027-12-31\""
            + "}";

        Number id =
            given()
                .contentType(ContentType.JSON)
                .body(bodyCriar)
            .when()
                .post("/lotes-estoque")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("quantidade", equalTo(100))
                .body("dataValidade", equalTo("2027-12-31"))
                .body("produto", notNullValue())
                .extract()
                .path("id");

        given()
            .when().get("/lotes-estoque/{id}", id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("quantidade", equalTo(100));

        String bodyAtualizar = "{"
            + "\"produtoId\":" + produtoId.intValue() + ","
            + "\"quantidade\":200,"
            + "\"dataValidade\":\"2028-06-15\""
            + "}";

        given()
            .contentType(ContentType.JSON)
            .body(bodyAtualizar)
        .when()
            .put("/lotes-estoque/{id}", id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id.intValue()))
            .body("quantidade", equalTo(200))
            .body("dataValidade", equalTo("2028-06-15"));

        given()
            .when().delete("/lotes-estoque/{id}", id)
            .then()
            .statusCode(204);

        given()
            .when().get("/lotes-estoque/{id}", id)
            .then()
            .statusCode(404);
    }

    @Test
    void salvar_semCamposObrigatorios_deveRetornar400() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/lotes-estoque")
        .then()
            .statusCode(400);
    }

    private Number criarProduto() {
        Number categoriaId = criarCategoria("Categoria Lote");
        Number torraId = criarTorra("MEDIA");
        Number tamanhoId = criarTamanho(500);
        Number materialId = criarMaterial("Vidro");
        Number fornecedorId = criarFornecedor("Fornecedor Lote", "99999999999999");

        String body = "{"
            + "\"nome\":\"Produto p/ Lote\","
            + "\"descricao\":\"Descrição\","
            + "\"preco\":10.0,"
            + "\"ativo\":true,"
            + "\"torraId\":" + torraId.intValue() + ","
            + "\"tamanhoEmbalagemId\":" + tamanhoId.intValue() + ","
            + "\"materialEmbalagemId\":" + materialId.intValue() + ","
            + "\"categoriasIds\":[" + categoriaId.intValue() + "],"
            + "\"fornecedorId\":" + fornecedorId.intValue()
            + "}";

        return given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/produtos")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    private Number criarCategoria(String nome) {
        return given()
            .contentType(ContentType.JSON)
            .body("{\"nome\":\"" + nome + "\"}")
        .when()
            .post("/categorias")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    private Number criarTorra(String tipo) {
        return given()
            .contentType(ContentType.JSON)
            .body("{\"tipo\":\"" + tipo + "\"}")
        .when()
            .post("/torras")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    private Number criarTamanho(int gramas) {
        return given()
            .contentType(ContentType.JSON)
            .body("{\"gramas\":" + gramas + "}")
        .when()
            .post("/tamanhos-embalagem")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    private Number criarMaterial(String nome) {
        return given()
            .contentType(ContentType.JSON)
            .body("{\"nome\":\"" + nome + "\"}")
        .when()
            .post("/materiais-embalagem")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    private Number criarFornecedor(String nome, String cnpj) {
        String body = "{"
            + "\"nome\":\"" + nome + "\","
            + "\"cnpj\":\"" + cnpj + "\","
            + "\"contato\":\"contato@lote.com\","
            + "\"endereco\":{\"rua\":\"Rua C\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77000001\"}"
            + "}";
        return given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/fornecedores")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }
}
