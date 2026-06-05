package br.unitins.topicos1.cafe.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class EnderecoFornecedor {
    private String rua;
    private String cidade;
    private String uf;
    private String cep;
    // Getters e setters

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
