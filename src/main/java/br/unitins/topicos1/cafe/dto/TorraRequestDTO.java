package br.unitins.topicos1.cafe.dto;

import br.unitins.topicos1.cafe.model.TipoTorra;
import jakarta.validation.constraints.NotNull;

public class TorraRequestDTO {
    @NotNull
    private TipoTorra tipo;

    public TipoTorra getTipo() {
        return tipo;
    }

    public void setTipo(TipoTorra tipo) {
        this.tipo = tipo;
    }
}
