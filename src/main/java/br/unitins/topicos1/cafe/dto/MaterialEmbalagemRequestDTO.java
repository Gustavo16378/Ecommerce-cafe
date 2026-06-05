package br.unitins.topicos1.cafe.dto;

import jakarta.validation.constraints.NotBlank;

public class MaterialEmbalagemRequestDTO {
    @NotBlank
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
