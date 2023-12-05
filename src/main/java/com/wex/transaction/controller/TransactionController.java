package com.wex.transaction.controller;

import com.wex.transaction.domain.Transaction;
import com.wex.transaction.domain.dto.PurchaseTransaction;
import com.wex.transaction.domain.form.PurchaseTransactionForm;
import com.wex.transaction.exception.ExceptionCurrencyCoversion;
import com.wex.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/purchase")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Transaction> storePurchase(@RequestBody PurchaseTransactionForm purchaseTransactionForm) {
        log.info("class=TransactionController method=storePurchase step=start");

        Transaction data = service.storePurchaseTransaction(purchaseTransactionForm);

        log.info("class=TransactionController method=storePurchase step=end");

        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/{country}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PurchaseTransaction> retrieveTransaction(@PathVariable("id") long id, @PathVariable("country") String country){
        log.info("class=TransactionController method=retrieveTransaction step=start");

        PurchaseTransaction data = service.retrieveTransaction(id, country);

        log.info("class=TransactionController method=retrieveTransaction step=end");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
