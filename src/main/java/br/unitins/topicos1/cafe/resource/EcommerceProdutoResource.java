package br.unitins.topicos1.cafe.resource;

import br.unitins.topicos1.cafe.dto.ProdutoEcommerceResponseDTO;
import br.unitins.topicos1.cafe.mapper.ProdutoMapper;
import br.unitins.topicos1.cafe.repository.ProdutoRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/ecommerce/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EcommerceProdutoResource {

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    ProdutoMapper mapper;

    @GET
    @PermitAll
    public List<ProdutoEcommerceResponseDTO> listar(
            @QueryParam("nome") String nome,
            @QueryParam("fornecedor") String fornecedor,
            @QueryParam("materialEmbalagem") String materialEmbalagem) {

        return produtoRepository.buscarAtivosComFiltros(nome, fornecedor, materialEmbalagem)
                .stream()
                .map(mapper::toEcommerceResponseDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public ProdutoEcommerceResponseDTO buscarPorId(@PathParam("id") Long id) {
        var produto = produtoRepository.find("id = ?1 and ativo = true", id).firstResult();
        if (produto == null) throw new NotFoundException("Produto não encontrado");
        return mapper.toEcommerceResponseDTO(produto);
    }
}
