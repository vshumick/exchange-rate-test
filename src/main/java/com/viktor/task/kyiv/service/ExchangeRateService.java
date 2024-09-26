package com.viktor.task.kyiv.service;

import com.viktor.task.kyiv.model.Currency;
import com.viktor.task.kyiv.model.ExchangeRate;
import com.viktor.task.kyiv.repository.CurrencyRepository;
import com.viktor.task.kyiv.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExchangeRateService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;

    // Map для хранения обменных курсов в памяти
    private final Map<String, ExchangeRate> exchangeRateCache = new HashMap<>();

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

    // Обновление кэша обменных курсов
    public void updateExchangeRates() {
        // Получаем список всех валют
        List<Currency> currencies = currencyRepository.findAll();

        // Проверяем, что есть хотя бы две валюты для выполнения конверсий
        if (currencies.size() < 2) {
            throw new IllegalStateException("Недостаточно валют для получения курсов обмена.");
        }

        // Для каждой валюты из списка будем подтягивать курсы по отношению к другим валютам
        for (Currency sourceCurrency : currencies) {
            StringBuilder targetCurrencyCodes = new StringBuilder();

            // Формируем список кодов валют для которых будем получать курс
            for (Currency targetCurrency : currencies) {
                // Исключаем sourceCurrency из списка валют для конверсии
                if (!sourceCurrency.getName().equals(targetCurrency.getName())) {
                    targetCurrencyCodes.append(targetCurrency.getName()).append(",");
                }
            }

            // Убираем последнюю запятую
            if (targetCurrencyCodes.length() > 0) {
                targetCurrencyCodes.deleteCharAt(targetCurrencyCodes.length() - 1);
            }

            // Формируем URL для API с параметром "source" для текущей валюты
            String url = String.format("%s/live?access_key=%s&currencies=%s&source=%s&format=1",
                    baseUrl, accessKey, targetCurrencyCodes, sourceCurrency.getName());

            // Выполняем запрос к API
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // Проверяем успешность запроса
            if (response != null && (boolean) response.get("success")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("quotes");

                // Проходим по каждому полученному курсу валют
                for (Map.Entry<String, Object> rateEntry : rates.entrySet()) {
                    String quote = rateEntry.getKey();
                    String fromCurrencyCode = quote.substring(0, 3); // Валюта source
                    String toCurrencyCode = quote.substring(3); // Валюта target

                    Currency fromCurrency = currencyRepository.findByName(fromCurrencyCode).orElse(null);
                    Currency toCurrency = currencyRepository.findByName(toCurrencyCode).orElse(null);

                    // Если обе валюты существуют в базе, сохраняем курс
                    if (fromCurrency != null && toCurrency != null) {
                        ExchangeRate exchangeRate = new ExchangeRate();
                        exchangeRate.setSourceCurrency(fromCurrency);
                        exchangeRate.setQuoteCurrency(toCurrency);

                        // Проверяем тип курса (Double или Integer) и устанавливаем его значение
                        if (rateEntry.getValue() instanceof Integer) {
                            exchangeRate.setRate(((Integer) rateEntry.getValue()).doubleValue());
                        } else if (rateEntry.getValue() instanceof Double) {
                            exchangeRate.setRate((Double) rateEntry.getValue());
                        }

                        // Устанавливаем время обновления как текущую дату и время
                        exchangeRate.setLastUpdated(LocalDateTime.now());

                        // Сохраняем новую запись о курсе в таблицу exchange_rate
                        exchangeRateRepository.save(exchangeRate);

                        // Обновляем данные в in-memory карте
                        exchangeRateCache.put(fromCurrency.getId() + "_" + toCurrency.getId(), exchangeRate);
                    }
                }
            }
        }
    }

    // Метод для получения всех курсов из кэша
    public List<ExchangeRate> getAllExchangeRatesFromCache() {
        return new ArrayList<>(exchangeRateCache.values());
    }

    // Метод для получения конкретного курса из кэша
    public Optional<ExchangeRate> getExchangeRateFromCache(Long sourceId, Long quoteId) {
        String key = sourceId + "_" + quoteId;
        return Optional.ofNullable(exchangeRateCache.get(key));
    }
}
