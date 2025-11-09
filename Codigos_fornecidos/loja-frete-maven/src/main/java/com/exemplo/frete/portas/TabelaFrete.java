package com.exemplo.frete.portas;

import java.math.BigDecimal;

public interface TabelaFrete {
    /** Retorna taxa base por kg para uma UF. */
    BigDecimal taxaBasePorKg(String uf);
}
