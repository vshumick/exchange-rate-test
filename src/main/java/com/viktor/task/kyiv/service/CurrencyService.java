package com.viktor.task.kyiv.service;

import com.viktor.task.kyiv.model.Currency;
import com.viktor.task.kyiv.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Импортируем аннотацию @Transactional
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CurrencyService {

    @Value("${currencylayer.api.access_key}")
    private String accessKey;

    @Value("${currencylayer.api.base_url}")
    private String baseUrl;

    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;

    public CurrencyService(CurrencyRepository currencyRepository, RestTemplate restTemplate) {
        this.currencyRepository = currencyRepository;
        this.restTemplate = restTemplate;
    }

    // Fetch all currencies and update the currency table
    @Transactional
    public void updateCurrencies() {
        String url = String.format("%s/list?access_key=%s", baseUrl, accessKey);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && (boolean) response.get("success")) {
            Map<String, String> currencies = (Map<String, String>) response.get("currencies");
            currencies.forEach((code, fullName) -> {
                if (!currencyRepository.existsByName(code)) {
                    Currency currency = new Currency();
                    currency.setName(code);
                    currency.setFullName(fullName);
                    currencyRepository.save(currency);
                }
            });
        }
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Transactional
    public Currency addOrUpdateCurrency(Currency currency) {
        Optional<Currency> existingCurrency = currencyRepository.findByName(currency.getName());
        if (existingCurrency.isPresent()) {
            Currency updatedCurrency = existingCurrency.get();
            updatedCurrency.setFullName(currency.getFullName());
            updatedCurrency.setDescription(currency.getDescription());
            return currencyRepository.save(updatedCurrency);
        } else {
            return currencyRepository.save(currency);
        }
    }
}
