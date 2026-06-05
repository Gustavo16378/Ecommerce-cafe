package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.ItemPedidoResponseDTO;
import br.unitins.topicos1.cafe.dto.PedidoRequestDTO;
import br.unitins.topicos1.cafe.dto.PedidoResponseDTO;
import br.unitins.topicos1.cafe.exception.ValidationException;
import br.unitins.topicos1.cafe.model.*;
import br.unitins.topicos1.cafe.repository.PedidoRepository;
import br.unitins.topicos1.cafe.repository.ProdutoRepository;
import br.unitins.topicos1.cafe.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PedidoServiceImpl {

    @Inject PedidoRepository pedidoRepository;
    @Inject UsuarioRepository usuarioRepository;
    @Inject ProdutoRepository produtoRepository;

    @Transactional
    public PedidoResponseDTO realizarCompra(String login, PedidoRequestDTO dto, String enderecoEntrega) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setEnderecoEntrega(enderecoEntrega);
        pedido.setDataEntregaPrevista(LocalDate.now().plusDays(7));

        List<ItemPedido> itens = new ArrayList<>();
        for (var itemDTO : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDTO.produtoId());
            if (produto == null) throw new NotFoundException("Produto não encontrado: " + itemDTO.produtoId());

            int estoqueTotal = produto.getLotesEstoque() == null ? 0 :
                    produto.getLotesEstoque().stream()
                        .filter(l -> l.getDataValidade().isAfter(LocalDate.now()))
                        .mapToInt(LoteEstoque::getQuantidade).sum();

            if (estoqueTotal < itemDTO.quantidade()) {
                throw new ValidationException("Estoque insuficiente para o produto: " + produto.getNome(), "quantidade");
            }

            // Decrementa estoque FIFO — lotes com validade mais próxima primeiro
            int restante = itemDTO.quantidade();
            List<LoteEstoque> lotes = produto.getLotesEstoque().stream()
                    .filter(l -> l.getDataValidade().isAfter(LocalDate.now()) && l.getQuantidade() > 0)
                    .sorted(Comparator.comparing(LoteEstoque::getDataValidade))
                    .collect(Collectors.toList());

            for (LoteEstoque lote : lotes) {
                if (restante <= 0) break;
                int debito = Math.min(lote.getQuantidade(), restante);
                lote.setQuantidade(lote.getQuantidade() - debito);
                restante -= debito;
            }

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.quantidade());
            item.setPrecoUnitario(produto.getPreco());
            itens.add(item);
        }

        pedido.setItens(itens);
        pedidoRepository.persist(pedido);
        return toDTO(pedido);
    }

    // Mantido para compatibilidade com testes diretos de pedido
    @Transactional
    public PedidoResponseDTO realizarCompra(String login, PedidoRequestDTO dto) {
        return realizarCompra(login, dto, null);
    }

    public List<PedidoResponseDTO> historico(String login) {
        return pedidoRepository.findByUsuarioLogin(login).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO buscarPorId(String login, Long id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null || !pedido.getUsuario().getLogin().equals(login))
            throw new NotFoundException("Pedido não encontrado");
        return toDTO(pedido);
    }

    private PedidoResponseDTO toDTO(Pedido pedido) {
        List<ItemPedidoResponseDTO> itensDTO = pedido.getItens().stream().map(item -> {
            ItemPedidoResponseDTO i = new ItemPedidoResponseDTO();
            i.setId(item.getId());
            i.setNomeProduto(item.getProduto().getNome());
            i.setQuantidade(item.getQuantidade());
            i.setPrecoUnitario(item.getPrecoUnitario());
            i.setSubtotal(item.getQuantidade() * item.getPrecoUnitario());
            return i;
        }).collect(Collectors.toList());

        double total = itensDTO.stream().mapToDouble(ItemPedidoResponseDTO::getSubtotal).sum();

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setStatus(pedido.getStatus().name());
        dto.setEnderecoEntrega(pedido.getEnderecoEntrega());
        dto.setDataEntregaPrevista(pedido.getDataEntregaPrevista());
        dto.setItens(itensDTO);
        dto.setTotal(total);
        return dto;
    }
}
