package com.wex.transaction.domain.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseTransaction {

    private Long id;
    private String description;
    private LocalDateTime date;
    private BigDecimal value;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;
}
