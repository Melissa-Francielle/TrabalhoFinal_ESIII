package com.exemplo.frete.modelo;

import java.math.BigDecimal;

public class FreteResultado {
    private final BigDecimal valor;
    private final int prazoDias;

    public FreteResultado(BigDecimal valor, int prazoDias){
        this.valor = valor; this.prazoDias = prazoDias;
    }
    public BigDecimal getValor(){ return valor; }
    public int getPrazoDias(){ return prazoDias; }
}
