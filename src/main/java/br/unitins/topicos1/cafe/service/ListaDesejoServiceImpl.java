package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.ListaDesejoResponseDTO;
import br.unitins.topicos1.cafe.dto.ProdutoEcommerceResponseDTO;
import br.unitins.topicos1.cafe.exception.ValidationException;
import br.unitins.topicos1.cafe.mapper.ProdutoMapper;
import br.unitins.topicos1.cafe.model.ListaDesejo;
import br.unitins.topicos1.cafe.model.Produto;
import br.unitins.topicos1.cafe.repository.ListaDesejoRepository;
import br.unitins.topicos1.cafe.repository.ProdutoRepository;
import br.unitins.topicos1.cafe.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ListaDesejoServiceImpl {

    @Inject ListaDesejoRepository listaDesejoRepository;
    @Inject UsuarioRepository usuarioRepository;
    @Inject ProdutoRepository produtoRepository;
    @Inject ProdutoMapper produtoMapper;

    public ListaDesejoResponseDTO buscarPorUsuario(String login) {
        ListaDesejo lista = listaDesejoRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new NotFoundException("Lista de desejos não encontrada"));
        return toDTO(lista);
    }

    @Transactional
    public ListaDesejoResponseDTO adicionarProduto(String login, Long produtoId) {
        var usuario = usuarioRepository.findByLogin(login);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");

        Produto produto = produtoRepository.findById(produtoId);
        if (produto == null) throw new NotFoundException("Produto não encontrado");

        ListaDesejo lista = listaDesejoRepository.findByUsuarioLogin(login)
                .orElseGet(() -> {
                    ListaDesejo nova = new ListaDesejo();
                    nova.setUsuario(usuario);
                    listaDesejoRepository.persist(nova);
                    return nova;
                });

        boolean jaExiste = lista.getProdutos().stream().anyMatch(p -> p.getId().equals(produtoId));
        if (jaExiste) throw new ValidationException("Produto já está na lista de desejos", "produtoId");

        lista.getProdutos().add(produto);
        return toDTO(lista);
    }

    @Transactional
    public void removerProduto(String login, Long produtoId) {
        ListaDesejo lista = listaDesejoRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new NotFoundException("Lista de desejos não encontrada"));
        lista.getProdutos().removeIf(p -> p.getId().equals(produtoId));
    }

    private ListaDesejoResponseDTO toDTO(ListaDesejo lista) {
        List<ProdutoEcommerceResponseDTO> produtos = lista.getProdutos().stream()
                .map(produtoMapper::toEcommerceResponseDTO)
                .collect(Collectors.toList());
        ListaDesejoResponseDTO dto = new ListaDesejoResponseDTO();
        dto.setId(lista.getId());
        dto.setProdutos(produtos);
        return dto;
    }
}
