package br.unitins.topicos1.cafe.mapper;

import br.unitins.topicos1.cafe.dto.TamanhoEmbalagemRequestDTO;
import br.unitins.topicos1.cafe.dto.TamanhoEmbalagemResponseDTO;
import br.unitins.topicos1.cafe.model.TamanhoEmbalagem;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TamanhoEmbalagemMapper {
    public TamanhoEmbalagem toModel(TamanhoEmbalagemRequestDTO dto) {
        TamanhoEmbalagem t = new TamanhoEmbalagem();
        t.setGramas(dto.getGramas());
        return t;
    }

    public TamanhoEmbalagemResponseDTO toResponseDTO(TamanhoEmbalagem t) {
        return new TamanhoEmbalagemResponseDTO(t.getId(), t.getGramas());
    }
}
