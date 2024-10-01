package com.viktor.task.kyiv.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateTest {

    @Test
    void testExchangeRateCreation() {
        Currency usd = new Currency(1L, "USD", "United States Dollar", "The official currency of the United States");
        Currency eur = new Currency(2L, "EUR", "Euro", "The official currency of the Eurozone");

        // Используем BigDecimal для поля rate
        ExchangeRate exchangeRate = new ExchangeRate(1L, usd, eur, BigDecimal.valueOf(0.85), LocalDateTime.now());

        assertEquals(usd, exchangeRate.getSourceCurrency());
        assertEquals(eur, exchangeRate.getQuoteCurrency());

        // Для сравнения BigDecimal используем compareTo, так как equals учитывает точность
        assertEquals(0, exchangeRate.getRate().compareTo(BigDecimal.valueOf(0.85)));

        assertNotNull(exchangeRate.getLastUpdated());
    }

    @Test
    void testExchangeRateSetters() {
        Currency usd = new Currency(1L, "USD", "United States Dollar", "The official currency of the United States");
        Currency eur = new Currency(2L, "EUR", "Euro", "The official currency of the Eurozone");
        ExchangeRate exchangeRate = new ExchangeRate();

        exchangeRate.setId(1L);
        exchangeRate.setSourceCurrency(usd);
        exchangeRate.setQuoteCurrency(eur);

        // Используем BigDecimal для поля rate
        exchangeRate.setRate(BigDecimal.valueOf(0.85));
        exchangeRate.setLastUpdated(LocalDateTime.now());

        assertEquals(1L, exchangeRate.getId());
        assertEquals(usd, exchangeRate.getSourceCurrency());
        assertEquals(eur, exchangeRate.getQuoteCurrency());

        // Для сравнения BigDecimal используем compareTo
        assertEquals(0, exchangeRate.getRate().compareTo(BigDecimal.valueOf(0.85)));
    }
}
