package com.exemplo.frete.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carrinho {
    private final List<ItemPedido> itens = new ArrayList<>();

    public void adicionar(ItemPedido item){ itens.add(item); }
    public List<ItemPedido> getItens(){ return Collections.unmodifiableList(itens); }

    public BigDecimal getSubtotal(){
        return itens.stream().map(ItemPedido::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getPesoTotal(){
        return itens.stream().map(ItemPedido::getPesoTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public boolean isVazio(){ return itens.isEmpty(); }
}
