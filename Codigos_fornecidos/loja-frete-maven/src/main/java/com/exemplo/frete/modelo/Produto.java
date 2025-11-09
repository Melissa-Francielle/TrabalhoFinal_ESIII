package com.exemplo.frete.modelo;

import java.math.BigDecimal;
import java.util.Objects;

public class Produto {
    private final String id;
    private final String nome;
    private final BigDecimal preco;
    private final BigDecimal pesoKg;

    public Produto(String id, String nome, BigDecimal preco, BigDecimal pesoKg) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id inválido");
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("nome inválido");
        if (preco == null || preco.signum() < 0) throw new IllegalArgumentException("preço inválido");
        if (pesoKg == null || pesoKg.signum() <= 0) throw new IllegalArgumentException("peso inválido");
        this.id = id; this.nome = nome; this.preco = preco; this.pesoKg = pesoKg;
    }
    public String getId(){ return id; }
    public String getNome(){ return nome; }
    public BigDecimal getPreco(){ return preco; }
    public BigDecimal getPesoKg(){ return pesoKg; }

    @Override public boolean equals(Object o){ return o instanceof Produto p && Objects.equals(id, p.id); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
