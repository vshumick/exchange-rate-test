package com.viktor.task.kyiv.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String fullName;

    private String description;
}
