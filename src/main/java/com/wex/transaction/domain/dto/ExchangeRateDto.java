package com.wex.transaction.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {

    private Date record_date;
    private String country;
    private String currency;
    private String country_currency_desc;
    private BigDecimal exchange_rate;
    private Date effective_date;
    private Integer src_line_nbr;
    private Integer record_fiscal_year;
    private Integer record_fiscal_quarter;
    private Integer record_calendar_year;
    private Integer record_calendar_quarter;
    private Integer record_calendar_month;
    private Integer record_calendar_day;
}
