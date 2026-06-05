package br.unitins.topicos1.cafe.dto;

import br.unitins.topicos1.cafe.model.TipoTorra;

public class TorraResponseDTO {
    private Long id;
    private TipoTorra tipo;

    public TorraResponseDTO() {}

    public TorraResponseDTO(Long id, TipoTorra tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoTorra getTipo() {
        return tipo;
    }

    public void setTipo(TipoTorra tipo) {
        this.tipo = tipo;
    }
}
