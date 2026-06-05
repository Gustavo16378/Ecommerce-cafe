package br.unitins.topicos1.cafe.mapper;

import br.unitins.topicos1.cafe.dto.MaterialEmbalagemRequestDTO;
import br.unitins.topicos1.cafe.dto.MaterialEmbalagemResponseDTO;
import br.unitins.topicos1.cafe.model.MaterialEmbalagem;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MaterialEmbalagemMapper {
    public MaterialEmbalagem toModel(MaterialEmbalagemRequestDTO dto) {
        MaterialEmbalagem m = new MaterialEmbalagem();
        m.setNome(dto.getNome());
        return m;
    }

    public MaterialEmbalagemResponseDTO toResponseDTO(MaterialEmbalagem m) {
        return new MaterialEmbalagemResponseDTO(m.getId(), m.getNome());
    }
}
