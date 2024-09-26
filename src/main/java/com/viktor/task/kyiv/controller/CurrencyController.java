package com.viktor.task.kyiv.controller;

import com.viktor.task.kyiv.model.Currency;
import com.viktor.task.kyiv.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @GetMapping
    public List<Currency> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @PostMapping
    public ResponseEntity<Currency> addOrUpdateCurrency(@RequestBody Currency currency) {
        Currency savedCurrency = currencyService.addOrUpdateCurrency(currency);
        return ResponseEntity.ok(savedCurrency);
    }
}
