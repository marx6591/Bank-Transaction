package com.wex.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExceptionNotFound extends BusinessException {

    public static final String NOT_FOUND = "{1} {0} not found";

    public ExceptionNotFound(String object, String objectDescription) {

        String description = MessageFormat.format(NOT_FOUND, object, objectDescription);

        super.setMessage(description);
        super.setDescription(description);
        super.setHttpStatusCode(HttpStatus.NOT_FOUND);
        super.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
    }
}
