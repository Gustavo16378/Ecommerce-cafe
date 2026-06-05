package br.unitins.topicos1.cafe;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class ListaDesejoResourceTest {

    @Test
    @TestSecurity(user = "lista_user", roles = {"USER"})
    void adicionarERemoverProduto_deveFuncionar() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"lista_user\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        Number produtoId = criarProdutoCompleto("Cafe Lista Desejo", "44444444444444");

        given()
            .when().post("/lista-desejos/" + produtoId)
            .then()
            .statusCode(200)
            .body("produtos", hasSize(greaterThanOrEqualTo(1)));

        given()
            .when().get("/lista-desejos")
            .then()
            .statusCode(200)
            .body("produtos", hasSize(greaterThanOrEqualTo(1)));

        given()
            .when().delete("/lista-desejos/" + produtoId)
            .then()
            .statusCode(204);
    }

    @Test
    @TestSecurity(user = "lista_user2", roles = {"USER"})
    void buscar_semLista_deveRetornar404() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"lista_user2\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().get("/lista-desejos")
            .then()
            .statusCode(404);
    }

    @Test
    @TestSecurity(user = "lista_user3", roles = {"USER"})
    void adicionarProdutoInexistente_deveRetornar404() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"login\":\"lista_user3\",\"senha\":\"senha123\"}")
            .when().post("/usuarios/cadastro/simples");

        given()
            .when().post("/lista-desejos/99999")
            .then()
            .statusCode(404);
    }

    @TestSecurity(user = "admin", roles = {"ADMIN"})
    private Number criarProdutoCompleto(String nomeProduto, String cnpj) {
        Number catId = given().contentType(ContentType.JSON).body("{\"nome\":\"Cat Lista\"}")
            .when().post("/categorias").then().statusCode(201).extract().path("id");
        Number torraId = given().contentType(ContentType.JSON).body("{\"tipo\":\"CLARA\"}")
            .when().post("/torras").then().statusCode(201).extract().path("id");
        Number tamId = given().contentType(ContentType.JSON).body("{\"gramas\":200}")
            .when().post("/tamanhos-embalagem").then().statusCode(201).extract().path("id");
        Number matId = given().contentType(ContentType.JSON).body("{\"nome\":\"Vidro\"}")
            .when().post("/materiais-embalagem").then().statusCode(201).extract().path("id");
        String fBody = "{\"nome\":\"FornLista\",\"cnpj\":\"" + cnpj + "\","
            + "\"contato\":\"c@c.com\","
            + "\"endereco\":{\"rua\":\"R\",\"cidade\":\"P\",\"uf\":\"TO\",\"cep\":\"77000001\"}}";
        Number fornId = given().contentType(ContentType.JSON).body(fBody)
            .when().post("/fornecedores").then().statusCode(201).extract().path("id");

        String pBody = "{\"nome\":\"" + nomeProduto + "\",\"descricao\":\"desc\",\"preco\":10.0,"
            + "\"ativo\":true,\"torraId\":" + torraId + ",\"tamanhoEmbalagemId\":" + tamId
            + ",\"materialEmbalagemId\":" + matId + ",\"categoriasIds\":[" + catId + "]"
            + ",\"fornecedorId\":" + fornId + "}";
        return given().contentType(ContentType.JSON).body(pBody)
            .when().post("/produtos").then().statusCode(201).extract().path("id");
    }
}
