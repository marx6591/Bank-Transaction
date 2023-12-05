package com.wex.transaction.service;

import com.wex.transaction.domain.Transaction;
import com.wex.transaction.domain.dto.ExchangeRateDto;
import com.wex.transaction.domain.dto.ExchangeRateListDto;
import com.wex.transaction.domain.dto.PurchaseTransaction;
import com.wex.transaction.domain.form.PurchaseTransactionForm;
import com.wex.transaction.exception.ExceptionCurrencyCoversion;
import com.wex.transaction.exception.ExceptionNotFound;
import com.wex.transaction.exception.ExceptionUnauthorized;
import com.wex.transaction.repository.TransactionRepository;
import com.wex.transaction.service.feign.TreasuryRatesMsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    TransactionService service;

    @Mock
    TransactionRepository repository;

    @Mock
    TreasuryRatesMsClient treasuryRatesMsClient;

    PurchaseTransactionForm purchase;
    Transaction transaction;
    ExchangeRateListDto exchangeRateListDto;
    ExchangeRateDto exchangeRateDto;

    @BeforeEach
    public void setUp() {
        purchase = PurchaseTransactionForm.builder().description("Description correct").value(new BigDecimal("500.35")).build();
        transaction = Transaction.builder().id(1L).description("Purchase of Air Conditioning").date(LocalDateTime.now()).value(new BigDecimal("500.35")).build();
        exchangeRateDto = ExchangeRateDto.builder().exchange_rate(new BigDecimal("5.033")).build();
        exchangeRateListDto = ExchangeRateListDto.builder().data(List.of(exchangeRateDto)).build();
    }

    @Test
    void storePurchaseTransactionTestWithValidData() {

        when(repository.save(Mockito.any())).thenReturn(Transaction.builder().build());

        service.storePurchaseTransaction(purchase);

        Mockito.verify(repository).save(Mockito.any());

    }

    @Test
    void storePurchaseTransactionTestWithInvalidDescription() {

        purchase.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed congue, felis vel tempus fermentum");

        assertThrows(ExceptionUnauthorized.class, () -> service.storePurchaseTransaction(purchase));

    }

    @Test
    void storePurchaseTransactionTestWithZeroValue() {

        purchase.setValue(new BigDecimal("0"));

        assertThrows(ExceptionUnauthorized.class, () -> service.storePurchaseTransaction(purchase));

    }

    @Test
    void storePurchaseTransactionTestWithNegativeValue() {

        purchase.setValue(new BigDecimal("-10"));

        assertThrows(ExceptionUnauthorized.class, () -> service.storePurchaseTransaction(purchase));

    }

    @Test
    void retrieveTransactionWithValidData() {

        when(repository.findById(1L)).thenReturn(java.util.Optional.ofNullable(transaction));
        when(treasuryRatesMsClient.getLastExchangeRate(Mockito.any())).thenReturn(exchangeRateListDto);

        PurchaseTransaction purchaseTransaction = service.retrieveTransaction(1, "Brazil");

        Mockito.verify(repository).findById(1L);
        assertEquals(new BigDecimal("2518.26"), purchaseTransaction.getConvertedAmount());
    }

    @Test
    void retrieveTransactionWithInvalidTransactionId() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ExceptionNotFound.class, () -> service.retrieveTransaction(1, "Brazil"));
    }

    @Test
    void retrieveTransactionWithInvalidCurrencyConversion() {

        when(repository.findById(1L)).thenReturn(java.util.Optional.ofNullable(transaction));
        when(treasuryRatesMsClient.getLastExchangeRate(Mockito.any())).thenReturn(null);

        assertThrows(ExceptionCurrencyCoversion.class, () -> service.retrieveTransaction(1, "Brazil"));
    }

}
