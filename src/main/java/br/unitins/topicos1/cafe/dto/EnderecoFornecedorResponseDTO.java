package br.unitins.topicos1.cafe.dto;

public class EnderecoFornecedorResponseDTO {
    private String rua;
    private String cidade;
    private String uf;
    private String cep;

    public EnderecoFornecedorResponseDTO() {}

    public EnderecoFornecedorResponseDTO(String rua, String cidade, String uf, String cep) {
        this.rua = rua;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

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
