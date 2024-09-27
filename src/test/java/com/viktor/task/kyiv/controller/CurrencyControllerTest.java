package com.viktor.task.kyiv.controller;

import com.viktor.task.kyiv.model.Currency;
import com.viktor.task.kyiv.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyControllerTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(1L, "USD", "United States Dollar", "The official currency of the United States"));

        when(currencyService.getAllCurrencies()).thenReturn(currencies);

        List<Currency> result = currencyController.getAllCurrencies();
        assertEquals(1, result.size());
    }

    @Test
    void testAddOrUpdateCurrency() {
        Currency currency = new Currency(1L, "USD", "United States Dollar", "The official currency of the United States");

        when(currencyService.addOrUpdateCurrency(currency)).thenReturn(currency);

        ResponseEntity<Currency> result = currencyController.addOrUpdateCurrency(currency);
        assertEquals(200, result.getStatusCodeValue());
    }
}
