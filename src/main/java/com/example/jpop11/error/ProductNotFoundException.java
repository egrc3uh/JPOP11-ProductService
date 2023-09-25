package com.example.jpop11.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason ="Product not Found" ,code = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {
    private String message;

    public ProductNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
