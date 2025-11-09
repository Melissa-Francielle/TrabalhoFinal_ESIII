package com.exemplo.frete.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cupom {
    private final String codigo;
    private final LocalDate validade; // inclusive
    private final boolean freteGratis;
    private final BigDecimal descontoPercentual; // pode ser null se freteGratis

    public Cupom(String codigo, LocalDate validade, boolean freteGratis, BigDecimal descontoPercentual){
        this.codigo = codigo;
        this.validade = validade;
        this.freteGratis = freteGratis;
        this.descontoPercentual = descontoPercentual;
    }
    public boolean validoEm(LocalDate data){ return !data.isAfter(validade); }
    public String getCodigo(){ return codigo; }
    public boolean isFreteGratis(){ return freteGratis; }
    public BigDecimal getDescontoPercentual(){ return descontoPercentual; }
}
