package com.wex.transaction.service.feign;

import com.wex.transaction.domain.dto.ExchangeRateListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${ms-rates.url}", name = "feignMSRates")
public interface TreasuryRatesMsClient {

    @GetMapping(value = "/accounting/od/rates_of_exchange?filter={filters}&sort=-record_date&format=json&page[number]=1&page[size]=1")
    ExchangeRateListDto getLastExchangeRate(@PathVariable("filters") String filters);
}
