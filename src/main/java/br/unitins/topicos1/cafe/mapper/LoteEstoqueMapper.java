package br.unitins.topicos1.cafe.mapper;

import br.unitins.topicos1.cafe.dto.LoteEstoqueRequestDTO;
import br.unitins.topicos1.cafe.dto.LoteEstoqueResponseDTO;
import br.unitins.topicos1.cafe.model.LoteEstoque;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoteEstoqueMapper {

    public LoteEstoque toModel(LoteEstoqueRequestDTO dto) {
        LoteEstoque l = new LoteEstoque();
        l.setQuantidade(dto.getQuantidade());
        l.setDataValidade(dto.getDataValidade());
        return l;
    }

    public LoteEstoqueResponseDTO toResponseDTO(LoteEstoque l) {
        LoteEstoqueResponseDTO dto = new LoteEstoqueResponseDTO();
        dto.setId(l.getId());
        dto.setProduto(l.getProduto() != null ? l.getProduto().getNome() : null);
        dto.setQuantidade(l.getQuantidade());
        dto.setDataValidade(l.getDataValidade());
        return dto;
    }
}
