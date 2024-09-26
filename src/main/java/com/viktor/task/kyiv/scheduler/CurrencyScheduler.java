package com.viktor.task.kyiv.scheduler;

import com.viktor.task.kyiv.service.CurrencyService;
import com.viktor.task.kyiv.service.ExchangeRateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyScheduler {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    public CurrencyScheduler(CurrencyService currencyService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    // Runs every hour to update currencies and exchange rates
    //@Scheduled(fixedRate = 3600000)
    @Scheduled(fixedRate = 120000)
    public void updateCurrencyAndExchangeRates() {
        //currencyService.updateCurrencies();
        exchangeRateService.updateExchangeRates();
    }
}
