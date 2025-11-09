package com.exemplo.frete.servico;

import com.exemplo.frete.modelo.*;
import com.exemplo.frete.portas.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FreteServiceTest {
    TabelaFrete tabela; CorreiosClient correios; Clock clock; FreteService service;

    Produto teclado = new Produto("P1","Teclado", new BigDecimal("100.00"), new BigDecimal("0.8"));
    Produto mouse = new Produto("P2","Mouse", new BigDecimal("50.00"), new BigDecimal("0.2"));

    @BeforeEach
    void setup(){
        tabela = mock(TabelaFrete.class);
        correios = mock(CorreiosClient.class);
        clock = Clock.fixed(LocalDate.of(2025,1,10).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        service = new FreteService("01001-000", tabela, correios, clock, new BigDecimal("200.00"));
    }

    private Carrinho carrinhoBasico(){
        Carrinho c = new Carrinho();
        c.adicionar(new ItemPedido(teclado, 1)); // peso 0.8, subtotal 100
        c.adicionar(new ItemPedido(mouse, 2));   // peso 0.4, subtotal 100
        return c; // peso total 1.2, subtotal 200
    }

    @Test
    void frete_gratis_por_limiar_subtotal(){
        Carrinho c = carrinhoBasico(); // subtotal = 200 -> grátis
        Endereco destino = new Endereco("20000-000","RJ");
        when(correios.prazoDias(any(), any())).thenReturn(5);

        FreteResultado r = service.calcular(c, destino, null);
        assertEquals(new BigDecimal("0.00"), r.getValor());
        assertEquals(5, r.getPrazoDias());
        verifyNoInteractions(tabela); // não deve consultar taxa se é grátis
    }

    @Test
    void frete_normal_por_peso_e_uf(){
        Carrinho c = new Carrinho();
        c.adicionar(new ItemPedido(mouse, 1)); // subtotal 50, peso 0.2
        Endereco destino = new Endereco("80000-000","PR");
        when(correios.prazoDias(any(), any())).thenReturn(7);
        when(tabela.taxaBasePorKg("PR")).thenReturn(new BigDecimal("12.00"));

        FreteResultado r = service.calcular(c, destino, null);
        assertEquals(new BigDecimal("2.40"), r.getValor()); // 12 * 0.2
        assertEquals(7, r.getPrazoDias());
        verify(tabela).taxaBasePorKg("PR");
    }

    @Test
    void cupom_frete_gratis_valido(){
        Carrinho c = new Carrinho();
        c.adicionar(new ItemPedido(teclado, 1)); // subtotal 100
        Endereco destino = new Endereco("70000-000","DF");
        when(correios.prazoDias(any(), any())).thenReturn(3);

        Cupom freteGratis = new Cupom("FRETEGRATIS", LocalDate.of(2025,12,31), true, null);
        FreteResultado r = service.calcular(c, destino, freteGratis);
        assertEquals(new BigDecimal("0.00"), r.getValor());
        verifyNoInteractions(tabela);
    }

    @Test
    void cupom_expirado_dispara_excecao(){
        Carrinho c = new Carrinho();
        c.adicionar(new ItemPedido(teclado, 1));
        Endereco destino = new Endereco("70000-000","DF");
        when(correios.prazoDias(any(), any())).thenReturn(3);

        Cupom expirado = new Cupom("FRETEGRATIS", LocalDate.of(2024,12,31), true, null);
        assertThrows(RegraFreteException.class, () -> service.calcular(c, destino, expirado));
        verifyNoInteractions(tabela);
    }

    @Test
    void uf_sem_taxa_configurada(){
        Carrinho c = new Carrinho();
        c.adicionar(new ItemPedido(mouse, 1)); // subtotal 50 < 200
        Endereco destino = new Endereco("59000-000","RN");
        when(correios.prazoDias(any(), any())).thenReturn(9);
        when(tabela.taxaBasePorKg("RN")).thenReturn(null);

        assertThrows(RegraFreteException.class, () -> service.calcular(c, destino, null));
    }

    @Test
    void carrinho_vazio_lanca_excecao(){
        Carrinho c = new Carrinho();
        Endereco destino = new Endereco("20000-000","RJ");
        assertThrows(RegraFreteException.class, () -> service.calcular(c, destino, null));
    }
}
