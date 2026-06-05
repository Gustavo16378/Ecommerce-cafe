package br.unitins.topicos1.cafe.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PRODUTO_CAFE")
public class ProdutoCafe extends Produto {
    @Enumerated(EnumType.STRING)
    private TipoCafe tipoCafe;
    @Enumerated(EnumType.STRING)
    private TipoMoagem tipoMoagem;
    // Getters e setters

    public TipoCafe getTipoCafe() {
        return tipoCafe;
    }

    public void setTipoCafe(TipoCafe tipoCafe) {
        this.tipoCafe = tipoCafe;
    }

    public TipoMoagem getTipoMoagem() {
        return tipoMoagem;
    }

    public void setTipoMoagem(TipoMoagem tipoMoagem) {
        this.tipoMoagem = tipoMoagem;
    }

    
}
