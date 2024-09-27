package com.viktor.task.kyiv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
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
    private Double rate;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
}
