package com.exemplo.frete.modelo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;

import static org.junit.jupiter.api.Assertions.*;


class FreteModeloTest {

	@Test
	void produto_equals_hashcode_tostring() {
	    Produto p1 = new Produto("A", "Produto A", new BigDecimal("50.00"), new BigDecimal("1.2"));
	    Produto p2 = new Produto("A", "Produto A", new BigDecimal("50.00"), new BigDecimal("1.2"));
	    Produto p3 = new Produto("B", "Produto B", new BigDecimal("60.00"), new BigDecimal("2.0"));

	    assertNotSame(p1, p2);
	    assertNotEquals(p1, p3);


	    String texto = p1.toString();
	    assertNotNull(texto);
	    assertFalse(texto.isBlank());
	}


    @Test
    void itempedido_calcula_subtotal_e_peso() {
        Produto p = new Produto("C", "Produto C", new BigDecimal("10.00"), new BigDecimal("2.0"));
        ItemPedido item = new ItemPedido(p, 3);

        assertEquals(new BigDecimal("30.00"), item.getSubtotal());
        assertEquals(new BigDecimal("6.0"), item.getPesoTotal());
        assertEquals(p, item.getProduto());
        assertEquals(3, item.getQuantidade());
    }

    @Test
    void carrinho_soma_itens_e_pesos_e_getters() {
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
        
        carrinho.adicionar(new ItemPedido(p, 2)); 

        assertEquals(new BigDecimal("15.00"), carrinho.getSubtotal());
        assertEquals(new BigDecimal("0.6"), carrinho.getPesoTotal());
    }

    @Test
    void cupom_valido_e_expirado() {
        LocalDate hoje = LocalDate.of(2025, 1, 10);

        Cupom valido = new Cupom("OK", LocalDate.of(2025, 1, 15), false, new BigDecimal("0.10"));
        Cupom expirado = new Cupom("FAIL", LocalDate.of(2025, 1, 5), false, new BigDecimal("0.10"));

        assertTrue(valido.validoEm(hoje));      
        assertFalse(expirado.validoEm(hoje));   
    }

    @Test
    void cupom_no_limite_exatamente_no_dia_de_validade() {
        LocalDate dataValidade = LocalDate.of(2025, 1, 15);

        Cupom c = new Cupom("LIMITE", dataValidade, true, null);

        assertTrue(c.validoEm(dataValidade));
    }


    @Test
    void freteresultado_getters_toString() {
        FreteResultado f1 = new FreteResultado(new BigDecimal("20.00"), 5);
        FreteResultado f2 = new FreteResultado(new BigDecimal("20.00"), 5);

        assertEquals(new BigDecimal("20.00"), f1.getValor());
        assertEquals(5, f1.getPrazoDias());
        assertEquals(f1.getValor(), f2.getValor());
        assertEquals(f1.getPrazoDias(), f2.getPrazoDias());

        String texto = f1.toString();
        assertNotNull(texto);
        assertFalse(texto.isBlank());
    }

    @Test
    void endereco_toString_e_campos() {
        Endereco e1 = new Endereco("01001-000", "SP");
        Endereco e2 = new Endereco("01001-000", "SP");

        assertEquals("01001-000", e1.getCep());
        assertEquals("SP", e1.getUf());
        assertNotSame(e1, e2);

        assertNotNull(e1.toString());
        assertFalse(e1.toString().isBlank());
    }
}
