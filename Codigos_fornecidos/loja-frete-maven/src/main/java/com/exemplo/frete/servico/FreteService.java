package com.exemplo.frete.servico;

import com.exemplo.frete.modelo.*;
import com.exemplo.frete.portas.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

public class FreteService {
    private final String cepOrigem;
    private final TabelaFrete tabelaFrete;
    private final CorreiosClient correios;
    private final Clock clock;
    private final BigDecimal limiarFreteGratis; // ex.: 200.00

    public FreteService(String cepOrigem, TabelaFrete tabelaFrete, CorreiosClient correios, Clock clock, BigDecimal limiarFreteGratis){
        this.cepOrigem = Objects.requireNonNull(cepOrigem);
        this.tabelaFrete = Objects.requireNonNull(tabelaFrete);
        this.correios = Objects.requireNonNull(correios);
        this.clock = Objects.requireNonNull(clock);
        this.limiarFreteGratis = limiarFreteGratis == null ? BigDecimal.ZERO : limiarFreteGratis;
    }

    public FreteResultado calcular(Carrinho carrinho, Endereco destino, Cupom cupom){
        if (carrinho == null || carrinho.isVazio()) throw new RegraFreteException("Carrinho vazio");
        if (destino == null) throw new RegraFreteException("Destino obrigatório");

        BigDecimal subtotal = carrinho.getSubtotal();
        BigDecimal peso = carrinho.getPesoTotal();
        if (peso.signum() <= 0) throw new RegraFreteException("Peso total inválido");

        // Promoções: frete grátis por subtotal
        boolean isGratis = subtotal.compareTo(limiarFreteGratis) >= 0;

        // Cupom
        if (cupom != null){
            if (!cupom.validoEm(LocalDate.now(clock))) throw new RegraFreteException("Cupom expirado");
            if (cupom.isFreteGratis()) isGratis = true;
        }

        int prazo = correios.prazoDias(cepOrigem, destino.getCep());

        if (isGratis) return new FreteResultado(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), prazo);

        BigDecimal taxa = tabelaFrete.taxaBasePorKg(destino.getUf());
        if (taxa == null) throw new RegraFreteException("UF sem taxa configurada: " + destino.getUf());

        BigDecimal valor = taxa.multiply(peso).setScale(2, RoundingMode.HALF_UP);
        return new FreteResultado(valor, prazo);
    }
}
