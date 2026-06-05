package br.unitins.topicos1.cafe.model;

import jakarta.persistence.*;

@Entity
public class Torra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoTorra tipo;

    // Getters e setters
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
