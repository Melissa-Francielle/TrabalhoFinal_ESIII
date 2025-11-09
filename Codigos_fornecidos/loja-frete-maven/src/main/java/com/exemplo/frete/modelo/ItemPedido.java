package com.exemplo.frete.modelo;

import java.math.BigDecimal;

public class ItemPedido {
    private final Produto produto;
    private final int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        if (produto == null) throw new IllegalArgumentException("produto nulo");
        if (quantidade <= 0) throw new IllegalArgumentException("quantidade deve ser > 0");
        this.produto = produto; this.quantidade = quantidade;
    }
    public Produto getProduto(){ return produto; }
    public int getQuantidade(){ return quantidade; }
    public BigDecimal getSubtotal(){ return produto.getPreco().multiply(BigDecimal.valueOf(quantidade)); }
    public BigDecimal getPesoTotal(){ return produto.getPesoKg().multiply(BigDecimal.valueOf(quantidade)); }
}
