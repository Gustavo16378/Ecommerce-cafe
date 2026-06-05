package br.unitins.topicos1.cafe.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TamanhoEmbalagemRequestDTO {
    @NotNull
    @Positive
    private Integer gramas;

    public Integer getGramas() {
        return gramas;
    }

    public void setGramas(Integer gramas) {
        this.gramas = gramas;
    }
}
