package com.exemplo.frete.model;

import com.exemplo.frete.modelo.*;
import com.exemplo.frete.portas.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Disabled;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FreteModeloTest {

    @Test
    void produto_equals_hashcode_tostring() {
        Produto p1 = new Produto("A", "Produto A", new BigDecimal("50.00"), new BigDecimal("1.2"));
        Produto p2 = new Produto("A", "Produto A", new BigDecimal("50.00"), new BigDecimal("1.2"));
        Produto p3 = new Produto("B", "Produto B", new BigDecimal("60.00"), new BigDecimal("2.0"));

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertTrue(p1.toString().contains("Produto A"));
    }

    @Test
    void itempedido_calcula_subtotal() {
        Produto p = new Produto("C", "Produto C", new BigDecimal("10.00"), new BigDecimal("2.0"));
        ItemPedido item = new ItemPedido(p, 3);

        assertEquals(new BigDecimal("30.00"), item.getSubtotal());
        assertEquals(new BigDecimal("6.0"), item.getPesoTotal());
        assertEquals(p, item.getProduto());
        assertEquals(3, item.getQuantidade());
    }

    @Test
    void carrinho_soma_itens_e_pesos() {
        Produto p1 = new Produto("P1", "Produto 1", new BigDecimal("20.00"), new BigDecimal("1.0"));
        Produto p2 = new Produto("P2", "Produto 2", new BigDecimal("10.00"), new BigDecimal("0.5"));

        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(new ItemPedido(p1, 2)); 
        carrinho.adicionar(new ItemPedido(p2, 4)); 

        assertEquals(new BigDecimal("80.00"), carrinho.getSubtotal());
        assertEquals(new BigDecimal("4.0"), carrinho.getPesoTotal());
        assertEquals(2, carrinho.getItens().size());
    }

    @Test
    void carrinho_com_item_repetido_soma_quantidade() {
        Produto p = new Produto("Z", "Produto Z", new BigDecimal("5.00"), new BigDecimal("0.2"));
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new ItemPedido(p, 1));
        carrinho.adicionar(new ItemPedido(p, 2)); // deve somar quantidades

        assertEquals(new BigDecimal("15.00"), carrinho.getSubtotal());
        assertEquals(new BigDecimal("0.6"), carrinho.getPesoTotal());
    }

    @Test
    void cupom_valido_e_expirado() {
        Instant dataAtual = LocalDate.of(2025, 1, 10)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        LocalDate hoje = LocalDate.from(dataAtual);

        Cupom valido = new Cupom("OK", LocalDate.of(2025, 1, 15), false, new BigDecimal("0.10"));
        Cupom expirado = new Cupom("FAIL", LocalDate.of(2025, 1, 5), false, new BigDecimal("0.10"));

        assertTrue(valido.validoEm(hoje));     
        assertFalse(expirado.validoEm(hoje));   
    }

    @Test
    void freteresultado_getters_e_equals() {
        FreteResultado f1 = new FreteResultado(new BigDecimal("20.00"), 5);
        FreteResultado f2 = new FreteResultado(new BigDecimal("20.00"), 5);
        FreteResultado f3 = new FreteResultado(new BigDecimal("10.00"), 3);

        assertEquals(new BigDecimal("20.00"), f1.getValor());
        assertEquals(5, f1.getPrazoDias());
        assertEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertTrue(f1.toString().contains("20.00"));
    }

    @Test
    void endereco_equals_hashcode_tostring() {
        Endereco e1 = new Endereco("01001-000", "SP");
        Endereco e2 = new Endereco("01001-000", "SP");
        Endereco e3 = new Endereco("20000-000", "RJ");

        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertTrue(e1.toString().contains("01001"));
    }
}
