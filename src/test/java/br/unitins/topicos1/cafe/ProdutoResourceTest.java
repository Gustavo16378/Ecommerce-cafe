package br.unitins.topicos1.cafe;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

@QuarkusTest
@TestSecurity(user = "admin", roles = {"ADMIN"})
public class ProdutoResourceTest {

    @Test
    void listar_deveRetornar200() {
        given()
            .when().get("/produtos")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void crud_comRelacionamentos_deveFuncionar() {
        Number categoriaId = criarCategoria("Cafés Especiais");
        Number torraId = criarTorra("CLARA");
        Number tamanhoId = criarTamanho(250);
        Number materialId = criarMaterial("Papel");
        Number fornecedorId = criarFornecedor("Fornecedor A", "11111111111111");

        String bodyCriar = "{"
            + "\"nome\":\"Produto Teste\"," 
            + "\"descricao\":\"Descrição\"," 
            + "\"preco\":19.9," 
            + "\"ativo\":true," 
            + "\"torraId\":" + torraId.intValue() + "," 
            + "\"tamanhoEmbalagemId\":" + tamanhoId.intValue() + "," 
            + "\"materialEmbalagemId\":" + materialId.intValue() + "," 
            + "\"categoriasIds\":[" + categoriaId.intValue() + "]," 
            + "\"fornecedorId\":" + fornecedorId.intValue() + ""
            + "}";

        Number produtoId =
            given()
                .contentType(ContentType.JSON)
                .body(bodyCriar)
            .when()
                .post("/produtos")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", equalTo("Produto Teste"))
                .body("ativo", equalTo(true))
                .body("torra", equalTo("CLARA"))
                .body("tamanhoEmbalagem", equalTo("250g"))
                .body("materialEmbalagem", equalTo("Papel"))
                .body("fornecedor", equalTo("Fornecedor A"))
                .body("categorias", hasItem("Cafés Especiais"))
                .extract()
                .path("id");

        given()
            .when().get("/produtos/{id}", produtoId)
            .then()
                .statusCode(200)
                .body("id", equalTo(produtoId.intValue()))
                .body("nome", equalTo("Produto Teste"));

        List<?> filtrados =
            given()
                .when().get("/produtos?nome=Produto")
                .then()
                .statusCode(200)
                .extract()
                .as(List.class);
        org.junit.jupiter.api.Assertions.assertFalse(filtrados.isEmpty());

        String bodyAtualizar = "{"
            + "\"nome\":\"Produto Atualizado\"," 
            + "\"descricao\":\"Descrição 2\"," 
            + "\"preco\":29.9," 
            + "\"ativo\":false," 
            + "\"torraId\":" + torraId.intValue() + "," 
            + "\"tamanhoEmbalagemId\":" + tamanhoId.intValue() + "," 
            + "\"materialEmbalagemId\":" + materialId.intValue() + "," 
            + "\"categoriasIds\":[" + categoriaId.intValue() + "]," 
            + "\"fornecedorId\":" + fornecedorId.intValue() + ""
            + "}";

        given()
            .contentType(ContentType.JSON)
            .body(bodyAtualizar)
        .when()
            .put("/produtos/{id}", produtoId)
        .then()
            .statusCode(200)
            .body("id", equalTo(produtoId.intValue()))
            .body("nome", equalTo("Produto Atualizado"))
            .body("ativo", equalTo(false));

        given()
            .when().delete("/produtos/{id}", produtoId)
            .then()
            .statusCode(204);

        given()
            .when().get("/produtos/{id}", produtoId)
            .then()
            .statusCode(404);
    }

    @Test
    void crud_produtoCafe_deveFuncionar() {
        Number categoriaId = criarCategoria("Cafés Especiais Cafe");
        Number torraId = criarTorra("ESCURA");
        Number tamanhoId = criarTamanho(500);
        Number materialId = criarMaterial("Lata");
        Number fornecedorId = criarFornecedor("Fornecedor Cafe", "22222222222222");

        String bodyCriar = "{"
            + "\"nome\":\"Café Premium\","
            + "\"descricao\":\"Café de alta qualidade\","
            + "\"preco\":49.9,"
            + "\"ativo\":true,"
            + "\"torraId\":" + torraId.intValue() + ","
            + "\"tamanhoEmbalagemId\":" + tamanhoId.intValue() + ","
            + "\"materialEmbalagemId\":" + materialId.intValue() + ","
            + "\"categoriasIds\":[" + categoriaId.intValue() + "],"
            + "\"fornecedorId\":" + fornecedorId.intValue() + ","
            + "\"tipoCafe\":\"GRAO\","
            + "\"tipoMoagem\":\"FINA\""
            + "}";

        Number produtoId =
            given()
                .contentType(ContentType.JSON)
                .body(bodyCriar)
            .when()
                .post("/produtos")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", equalTo("Café Premium"))
                .body("tipoCafe", equalTo("GRAO"))
                .body("tipoMoagem", equalTo("FINA"))
                .extract()
                .path("id");

        given()
            .when().delete("/produtos/{id}", produtoId)
            .then()
            .statusCode(204);
    }

    @Test
    void salvar_semCamposObrigatorios_deveRetornar400() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/produtos")
        .then()
            .statusCode(400);
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
        String bodyCriar = "{"
            + "\"nome\":\"" + nome + "\"," 
            + "\"cnpj\":\"" + cnpj + "\"," 
            + "\"contato\":\"contato@teste.com\"," 
            + "\"endereco\":{\"rua\":\"Rua A\",\"cidade\":\"Palmas\",\"uf\":\"TO\",\"cep\":\"77000000\"}"
            + "}";

        return given()
            .contentType(ContentType.JSON)
            .body(bodyCriar)
        .when()
            .post("/fornecedores")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }
}
