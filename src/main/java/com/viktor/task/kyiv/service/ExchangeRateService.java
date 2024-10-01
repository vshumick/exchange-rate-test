package com.viktor.task.kyiv.service;

import com.viktor.task.kyiv.model.Currency;
import com.viktor.task.kyiv.model.ExchangeRate;
import com.viktor.task.kyiv.repository.CurrencyRepository;
import com.viktor.task.kyiv.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExchangeRateService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;

    private final Map<String, ExchangeRate> exchangeRateCache = new ConcurrentHashMap<>();

    @Value("${currencylayer.api.access_key}")
    private String accessKey;

    @Value("${currencylayer.api.base_url}")
    private String baseUrl;

    public ExchangeRateService(CurrencyRepository currencyRepository,
                               ExchangeRateRepository exchangeRateRepository,
                               RestTemplate restTemplate) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public void updateExchangeRates() {
        List<Currency> currencies = currencyRepository.findAll();

        if (currencies.size() < 2) {
            throw new IllegalStateException("Not enough currencies to get exchange rates.");
        }

        List<ExchangeRate> exchangeRatesToSave = new ArrayList<>();

        for (Currency sourceCurrency : currencies) {
            StringBuilder targetCurrencyCodes = new StringBuilder();

            for (Currency targetCurrency : currencies) {
                if (!sourceCurrency.getName().equals(targetCurrency.getName())) {
                    targetCurrencyCodes.append(targetCurrency.getName()).append(",");
                }
            }

            if (targetCurrencyCodes.length() > 0) {
                targetCurrencyCodes.deleteCharAt(targetCurrencyCodes.length() - 1);
            }

            String url = String.format("%s/live?access_key=%s&currencies=%s&source=%s&format=1",
                    baseUrl, accessKey, targetCurrencyCodes, sourceCurrency.getName());

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && (boolean) response.get("success")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("quotes");

                for (Map.Entry<String, Object> rateEntry : rates.entrySet()) {
                    String quote = rateEntry.getKey();
                    String fromCurrencyCode = quote.substring(0, 3);
                    String toCurrencyCode = quote.substring(3);

                    Currency fromCurrency = currencyRepository.findByName(fromCurrencyCode).orElse(null);
                    Currency toCurrency = currencyRepository.findByName(toCurrencyCode).orElse(null);

                    if (fromCurrency != null && toCurrency != null) {
                        ExchangeRate exchangeRate = new ExchangeRate();
                        exchangeRate.setSourceCurrency(fromCurrency);
                        exchangeRate.setQuoteCurrency(toCurrency);

                        // Преобразование rate в BigDecimal
                        if (rateEntry.getValue() instanceof Integer) {
                            exchangeRate.setRate(BigDecimal.valueOf((Integer) rateEntry.getValue()));
                        } else if (rateEntry.getValue() instanceof Double) {
                            exchangeRate.setRate(BigDecimal.valueOf((Double) rateEntry.getValue()));
                        }

                        exchangeRate.setLastUpdated(LocalDateTime.now());

                        // Добавляем объект в список для сохранения
                        exchangeRatesToSave.add(exchangeRate);

                        exchangeRateCache.put(fromCurrency.getId() + "_" + toCurrency.getId(), exchangeRate);
                    }
                }
            }
        }

        // Сохраняем все объекты за один раз
        if (!exchangeRatesToSave.isEmpty()) {
            exchangeRateRepository.saveAll(exchangeRatesToSave);
        }
    }

    public List<ExchangeRate> getAllExchangeRatesFromCache() {
        return new ArrayList<>(exchangeRateCache.values());
    }

    public Optional<ExchangeRate> getExchangeRateFromCache(Long sourceId, Long quoteId) {
        String key = sourceId + "_" + quoteId;
        return Optional.ofNullable(exchangeRateCache.get(key));
    }
}
