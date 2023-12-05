package com.wex.transaction.exception;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

public class ExceptionCurrencyCoversion extends BusinessException {

    public static final String MESSAGE = "purchase cannot be converted to the target currency. Country: {0}";

    public ExceptionCurrencyCoversion(String country) {

        String description = MessageFormat.format(MESSAGE, country);

        super.setMessage(description);
        super.setDescription(description);
        super.setHttpStatusCode(HttpStatus.NOT_FOUND);
        super.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
    }
}
