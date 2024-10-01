package com.viktor.task.kyiv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;  // Импортируем BigDecimal
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id", nullable = false)
    private Currency sourceCurrency;

    @ManyToOne
    @JoinColumn(name = "quote_id", referencedColumnName = "id", nullable = false)
    private Currency quoteCurrency;

    @Column(nullable = false)
    private BigDecimal rate;  // Изменяем тип на BigDecimal

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(sourceCurrency, that.sourceCurrency) &&
                Objects.equals(quoteCurrency, that.quoteCurrency) &&
                Objects.equals(rate, that.rate) &&
                Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceCurrency, quoteCurrency, rate, lastUpdated);
    }
}
