package com.wex.transaction.service;

import com.wex.transaction.domain.Transaction;
import com.wex.transaction.domain.dto.ExchangeRateDto;
import com.wex.transaction.domain.dto.PurchaseTransaction;
import com.wex.transaction.domain.form.PurchaseTransactionForm;
import com.wex.transaction.exception.ExceptionCurrencyCoversion;
import com.wex.transaction.exception.ExceptionNotFound;
import com.wex.transaction.exception.ExceptionUnauthorized;
import com.wex.transaction.repository.TransactionRepository;
import com.wex.transaction.service.feign.TreasuryRatesMsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    private final TreasuryRatesMsClient treasuryRatesMsClient;

    public Transaction storePurchaseTransaction(PurchaseTransactionForm purchase) {

        Transaction transaction = purchaseToTransaction(purchase);

        return repository.save(transaction);
    }

    public Transaction purchaseToTransaction(PurchaseTransactionForm purchase) {

        if (purchase.getDescription().length() > 50)
            throw new ExceptionUnauthorized("Description must not exceed 50 characters");
        if (purchase.getValue().compareTo(BigDecimal.ZERO) < 1)
            throw new ExceptionUnauthorized("Transaction Value must be positive");

        Transaction transaction = Transaction.builder()
                .date(LocalDateTime.now())
                .value(purchase.getValue().setScale(2, RoundingMode.HALF_UP))
                .description(purchase.getDescription()).build();

        return transaction;

    }

    public PurchaseTransaction retrieveTransaction(long id, String country) {

        Transaction transaction = findById(id);

        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(transaction.getDate().minusMonths(6));
        String filters = "country:eq:" + country + ",record_date:gt:" + date;

        ExchangeRateDto exchangeRateDto = getLastExchangeRate(filters, country);

        BigDecimal convertedValue = transaction.getValue().multiply(exchangeRateDto.getExchange_rate());

        PurchaseTransaction purchaseTransaction = PurchaseTransaction.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .date(transaction.getDate())
                .value(transaction.getValue())
                .exchangeRate(exchangeRateDto.getExchange_rate())
                .convertedAmount(convertedValue.setScale(2, RoundingMode.HALF_UP)).build();

        return purchaseTransaction;

    }

    private ExchangeRateDto getLastExchangeRate(String filters, String country) {
        try {
            return treasuryRatesMsClient.getLastExchangeRate(filters).getData().get(0);
        } catch (Exception e) {
            throw new ExceptionCurrencyCoversion(country);
        }
    }

    public Transaction findById(long id) {
        return repository.findById(id).orElseThrow(() -> new ExceptionNotFound(Long.toString(id), "Transaction"));
    }
}
