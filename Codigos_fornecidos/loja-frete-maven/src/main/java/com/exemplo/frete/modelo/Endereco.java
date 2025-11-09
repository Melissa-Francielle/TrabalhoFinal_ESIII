package com.exemplo.frete.modelo;

public class Endereco {
    private final String cep;
    private final String uf;

    public Endereco(String cep, String uf){
        if (cep == null || cep.isBlank()) throw new IllegalArgumentException("cep inválido");
        if (uf == null || uf.isBlank()) throw new IllegalArgumentException("uf inválida");
        this.cep = cep; this.uf = uf;
    }
    public String getCep(){ return cep; }
    public String getUf(){ return uf; }
}
