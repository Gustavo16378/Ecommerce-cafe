package br.unitins.topicos1.cafe.dto;

public class TamanhoEmbalagemResponseDTO {
    private Long id;
    private Integer gramas;

    public TamanhoEmbalagemResponseDTO() {}

    public TamanhoEmbalagemResponseDTO(Long id, Integer gramas) {
        this.id = id;
        this.gramas = gramas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGramas() {
        return gramas;
    }

    public void setGramas(Integer gramas) {
        this.gramas = gramas;
    }
}
