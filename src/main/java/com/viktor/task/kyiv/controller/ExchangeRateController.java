package com.viktor.task.kyiv.controller;

import com.viktor.task.kyiv.model.ExchangeRate;
import com.viktor.task.kyiv.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateService.getAllExchangeRatesFromCache();
    }

    @GetMapping("/{sourceId}/{quoteId}")
    public ResponseEntity<ExchangeRate> getExchangeRate(
            @PathVariable Long sourceId,
            @PathVariable Long quoteId) {
        Optional<ExchangeRate> exchangeRate = exchangeRateService.getExchangeRateFromCache(sourceId, quoteId);
        return exchangeRate.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
