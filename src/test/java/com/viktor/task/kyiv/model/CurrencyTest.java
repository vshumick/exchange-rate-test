package com.viktor.task.kyiv.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void testCurrencyCreation() {
        Currency currency = new Currency(1L, "USD", "United States Dollar", "The official currency of the United States");

        assertEquals(1L, currency.getId());
        assertEquals("USD", currency.getName());
        assertEquals("United States Dollar", currency.getFullName());
        assertEquals("The official currency of the United States", currency.getDescription());
    }

    @Test
    void testCurrencySetters() {
        Currency currency = new Currency();
        currency.setId(2L);
        currency.setName("EUR");
        currency.setFullName("Euro");
        currency.setDescription("The official currency of the Eurozone");

        assertEquals(2L, currency.getId());
        assertEquals("EUR", currency.getName());
        assertEquals("Euro", currency.getFullName());
        assertEquals("The official currency of the Eurozone", currency.getDescription());
    }
}
