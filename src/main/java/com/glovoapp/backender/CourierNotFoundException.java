package com.glovoapp.backender;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourierNotFoundException extends Exception {
    CourierNotFoundException(String exception)
    {
        super(exception);
    }
}
