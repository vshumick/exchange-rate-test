package com.viktor.task.kyiv.repository;

import com.viktor.task.kyiv.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByName(String name);
    boolean existsByName(String name);
}
