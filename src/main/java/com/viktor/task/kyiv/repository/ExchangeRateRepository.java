package com.viktor.task.kyiv.repository;

import com.viktor.task.kyiv.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findBySourceCurrencyIdAndQuoteCurrencyId(Long sourceId, Long quoteId);
}
