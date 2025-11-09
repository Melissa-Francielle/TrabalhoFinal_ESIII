\
package com.exemplo.frete.servico;

import com.exemplo.frete.modelo.*;
import com.exemplo.frete.portas.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Disabled;

import java.math.BigDecimal;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Testes INCOMPLETOS para os alunos implementarem. */
class FreteServiceStudentTest {
    TabelaFrete tabela; CorreiosClient correios; Clock clock; FreteService service;
    Produto p = new Produto("P","Produto", new BigDecimal("120.00"), new BigDecimal("1.5"));

    @BeforeEach
    void setup(){
        tabela = mock(TabelaFrete.class);
        correios = mock(CorreiosClient.class);
        clock = Clock.fixed(LocalDate.of(2025,1,10).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        service = new FreteService("01001-000", tabela, correios, clock, new BigDecimal("200.00"));
    }

    @Test @Disabled("TODO: subtotal abaixo do limiar -> calcula frete por peso e UF")
    void calcula_frete_por_peso_e_uf() {
        // TODO: carrinho com 1 item, UF = SP, taxa 10.00/kg, peso 1.5 => valor esperado 15.00
    }

    @Test @Disabled("TODO: cupom frete grátis válido -> valor 0.00")
    void cupom_frete_gratis_valido() {
        // TODO: criar cupom com validade futura; verifyNoInteractions(tabela)
    }

    @Test @Disabled("TODO: cupom expirado -> lança RegraFreteException")
    void cupom_expirado_excecao() {
        // TODO
    }

    @Test @Disabled("TODO: UF sem taxa configurada -> lança RegraFreteException")
    void uf_sem_taxa_excecao() {
        // TODO
    }

    @Test @Disabled("TODO: carrinho vazio -> lança RegraFreteException")
    void carrinho_vazio_excecao() {
        // TODO
    }

    @Test @Disabled("TODO: peso total inválido (produto com peso <= 0) -> IllegalArgumentException ao criar produto")
    void peso_invalido_produto_excecao() {
        // TODO: new Produto(..., peso <= 0) deve lançar IllegalArgumentException (não precisa chamar o serviço)
    }

    @Test @Disabled("TODO: prazo dos Correios é repassado no resultado")
    void prazo_correios_no_resultado() {
        // TODO: mock correios.prazoDias(...) e verifique FreteResultado.getPrazoDias()
    }
}
