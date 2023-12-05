package com.wex.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExceptionUnauthorized extends BusinessException {

    public static final String UNAUTHORIZED = "Operation not permitted: {0}";

    public ExceptionUnauthorized(String msg) {

        String description = MessageFormat.format(UNAUTHORIZED, msg);

        super.setMessage(description);
        super.setDescription(description);
        super.setHttpStatusCode(HttpStatus.UNAUTHORIZED);
        super.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
    }
}
