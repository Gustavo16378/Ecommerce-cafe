package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.CarrinhoResponseDTO;
import br.unitins.topicos1.cafe.dto.ItemCarrinhoResponseDTO;
import br.unitins.topicos1.cafe.dto.PedidoResponseDTO;
import br.unitins.topicos1.cafe.exception.ValidationException;
import br.unitins.topicos1.cafe.model.*;
import br.unitins.topicos1.cafe.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CarrinhoServiceImpl {

    @Inject CarrinhoRepository carrinhoRepository;
    @Inject UsuarioRepository usuarioRepository;
    @Inject ProdutoRepository produtoRepository;
    @Inject PedidoServiceImpl pedidoService;

    public CarrinhoResponseDTO buscar(String login) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new NotFoundException("Carrinho não encontrado"));
        return toDTO(carrinho);
    }

    @Transactional
    public CarrinhoResponseDTO adicionarItem(String login, Long produtoId, Integer quantidade) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");

        Produto produto = produtoRepository.findById(produtoId);
        if (produto == null) throw new NotFoundException("Produto não encontrado");

        int estoque = produto.getLotesEstoque() == null ? 0 :
                produto.getLotesEstoque().stream().mapToInt(LoteEstoque::getQuantidade).sum();
        if (estoque < quantidade) {
            throw new ValidationException("Estoque insuficiente para o produto: " + produto.getNome(), "quantidade");
        }

        Carrinho carrinho = carrinhoRepository.findByUsuarioLogin(login).orElseGet(() -> {
            Carrinho novo = new Carrinho();
            novo.setUsuario(usuario);
            carrinhoRepository.persist(novo);
            return novo;
        });

        ItemCarrinho itemExistente = carrinho.getItens().stream()
                .filter(i -> i.getProduto().getId().equals(produtoId))
                .findFirst().orElse(null);

        if (itemExistente != null) {
            itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
        } else {
            ItemCarrinho item = new ItemCarrinho();
            item.setCarrinho(carrinho);
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            carrinho.getItens().add(item);
        }

        return toDTO(carrinho);
    }

    @Transactional
    public CarrinhoResponseDTO atualizarQuantidade(String login, Long produtoId, Integer quantidade) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new NotFoundException("Carrinho não encontrado"));

        ItemCarrinho item = carrinho.getItens().stream()
                .filter(i -> i.getProduto().getId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Produto não encontrado no carrinho"));

        if (quantidade <= 0) {
            carrinho.getItens().remove(item);
        } else {
            item.setQuantidade(quantidade);
        }

        return toDTO(carrinho);
    }

    @Transactional
    public void removerItem(String login, Long produtoId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new NotFoundException("Carrinho não encontrado"));
        carrinho.getItens().removeIf(i -> i.getProduto().getId().equals(produtoId));
    }

    @Transactional
    public void limpar(String login) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new NotFoundException("Carrinho não encontrado"));
        carrinho.getItens().clear();
    }

    @Transactional
    public PedidoResponseDTO checkout(String login) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new NotFoundException("Carrinho não encontrado"));

        if (carrinho.getItens().isEmpty())
            throw new ValidationException("O carrinho está vazio", "itens");

        List<br.unitins.topicos1.cafe.dto.ItemPedidoRequestDTO> itensDTO = carrinho.getItens().stream()
                .map(i -> new br.unitins.topicos1.cafe.dto.ItemPedidoRequestDTO(
                        i.getProduto().getId(), i.getQuantidade()))
                .collect(Collectors.toList());

        PedidoResponseDTO pedido = pedidoService.realizarCompra(login,
                new br.unitins.topicos1.cafe.dto.PedidoRequestDTO(itensDTO));

        carrinho.getItens().clear();
        return pedido;
    }

    private CarrinhoResponseDTO toDTO(Carrinho carrinho) {
        List<ItemCarrinhoResponseDTO> itens = carrinho.getItens().stream().map(item -> {
            ItemCarrinhoResponseDTO dto = new ItemCarrinhoResponseDTO();
            dto.setId(item.getId());
            dto.setProdutoId(item.getProduto().getId());
            dto.setNomeProduto(item.getProduto().getNome());
            dto.setPrecoUnitario(item.getProduto().getPreco());
            dto.setQuantidade(item.getQuantidade());
            dto.setSubtotal(item.getProduto().getPreco() * item.getQuantidade());
            return dto;
        }).collect(Collectors.toList());

        double total = itens.stream().mapToDouble(ItemCarrinhoResponseDTO::getSubtotal).sum();

        CarrinhoResponseDTO dto = new CarrinhoResponseDTO();
        dto.setId(carrinho.getId());
        dto.setItens(itens);
        dto.setTotal(total);
        return dto;
    }
}
