package com.viktor.task.kyiv.scheduler;

import com.viktor.task.kyiv.service.CurrencyService;
import com.viktor.task.kyiv.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyScheduler {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    @Value("${scheduling.fixed-rate}")
    private long fixedRate;

    public CurrencyScheduler(CurrencyService currencyService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    // Runs according to the fixedRate from configuration
    @Scheduled(fixedRateString = "${scheduling.fixed-rate}")
    public void updateCurrencyAndExchangeRates() {
        //currencyService.updateCurrencies();
        exchangeRateService.updateExchangeRates();
    }
}
