package com.exemplo.frete.servico;

import com.exemplo.frete.modelo.*;
import com.exemplo.frete.portas.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Disabled;

import java.math.BigDecimal;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Testes COMPLETOS **/
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

    @Test
    void calcula_frete_por_peso_e_uf() {
        when(tabela.taxaBasePorKg("SP")).thenReturn(new BigDecimal("10.00"));
        when(correios.prazoDias("01001-000", "01002-000")).thenReturn(3);

        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(new ItemPedido(p, 1));
        Endereco destino = new Endereco("SP", "01002-000");

        FreteResultado resultado = service.calcular(carrinho, destino, null);

        assertEquals(new BigDecimal("15.00"), resultado.getValor());
        assertEquals(3, resultado.getPrazoDias());
        verify(tabela).taxaBasePorKg("SP");
        verify(correios).prazoDias("01001-000", "01002-000");
    }

    @Test 
    void cupom_frete_gratis_valido() {
        Cupom cupom = new Cupom("FRETEGRATIS", LocalDate.of(2025, 1, 15), true, null);

        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(new ItemPedido(p, 1));
        Endereco destino = new Endereco("01002-000", "SP");

        FreteResultado resultado = service.calcular(carrinho, destino, cupom);

        assertEquals(BigDecimal.ZERO.setScale(2), resultado.getValor());
        verifyNoInteractions(tabela);	
    }

    @Test 
    void cupom_expirado_excecao() {
        Cupom cupom = new Cupom("EXPIRADO", LocalDate.of(2024, 12, 31), true, null);

        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(new ItemPedido(p, 1));
        Endereco destino = new Endereco("01002-000", "SP");

        assertThrows(RegraFreteException.class, () ->
            service.calcular(carrinho, destino, cupom)
        );
        verifyNoInteractions(tabela);
    }

    @Test
    void uf_sem_taxa_excecao() {
        when(tabela.taxaBasePorKg("RJ")).thenReturn(null);
        when(correios.prazoDias(any(), any())).thenReturn(7); 
        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(new ItemPedido(p, 1));
        Endereco destino = new Endereco("20000-000", "RJ");

        assertThrows(RegraFreteException.class, () ->
            service.calcular(carrinho, destino, null)
        );
    }

    @Test
    void carrinho_vazio_excecao() {
        Carrinho carrinho = new Carrinho();
        Endereco destino = new Endereco("01002-000", "SP");

        assertThrows(RegraFreteException.class, () ->
            service.calcular(carrinho, destino, null)
        );
    }

    @Test 
    void peso_invalido_produto_excecao() {
        assertThrows(IllegalArgumentException.class, () ->
        new Produto("X", "ProdutoZero", new BigDecimal("10.00"), new BigDecimal("0"))
        );
    assertThrows(IllegalArgumentException.class, () ->
        new Produto("Y", "ProdutoNegativo", new BigDecimal("10.00"), new BigDecimal("-1"))
    	);
    }

    @Test
    void prazo_correios_no_resultado() {
        when(tabela.taxaBasePorKg("SP")).thenReturn(new BigDecimal("10.00"));
        when(correios.prazoDias("01001-000", "01002-000")).thenReturn(5);

        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(new ItemPedido(p, 1));
        Endereco destino = new Endereco("01002-000", "SP");

        FreteResultado resultado = service.calcular(carrinho, destino, null);

        assertEquals(5, resultado.getPrazoDias());
        verify(correios).prazoDias("01001-000", "01002-000");
    }
}
