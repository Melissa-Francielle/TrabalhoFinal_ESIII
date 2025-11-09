package com.exemplo.frete.portas;

public interface CorreiosClient {
    /** Retorna prazo estimado em dias entre origem e destino (CEPs). */
    int prazoDias(String cepOrigem, String cepDestino);
}
