package br.unitins.topicos1.cafe.mapper;

import br.unitins.topicos1.cafe.dto.TorraRequestDTO;
import br.unitins.topicos1.cafe.dto.TorraResponseDTO;
import br.unitins.topicos1.cafe.model.Torra;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TorraMapper {
    public Torra toModel(TorraRequestDTO dto) {
        Torra t = new Torra();
        t.setTipo(dto.getTipo());
        return t;
    }

    public TorraResponseDTO toResponseDTO(Torra torra) {
        return new TorraResponseDTO(torra.getId(), torra.getTipo());
    }
}
