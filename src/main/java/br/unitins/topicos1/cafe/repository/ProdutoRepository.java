package br.unitins.topicos1.cafe.repository;

import br.unitins.topicos1.cafe.model.Produto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {

    public List<Produto> buscarAtivosComFiltros(String nome, String fornecedor, String materialEmbalagem) {
        List<String> condicoes = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        condicoes.add("ativo = true");

        if (nome != null && !nome.isBlank()) {
            condicoes.add("lower(nome) like lower(?1)".replace("?1", "?" + (params.size() + 1)));
            params.add("%" + nome + "%");
        }

        if (fornecedor != null && !fornecedor.isBlank()) {
            condicoes.add("lower(fornecedor.nome) like lower(?" + (params.size() + 1) + ")");
            params.add("%" + fornecedor + "%");
        }

        if (materialEmbalagem != null && !materialEmbalagem.isBlank()) {
            condicoes.add("lower(materialEmbalagem.nome) like lower(?" + (params.size() + 1) + ")");
            params.add("%" + materialEmbalagem + "%");
        }

        String query = String.join(" and ", condicoes);
        return list(query, params.toArray());
    }
}
