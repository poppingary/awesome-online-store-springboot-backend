package com.gyl.awesome_inc.domain.exception;

public class CustomerIdFoundException  extends RuntimeException {
    public CustomerIdFoundException(String msg) {
        super(msg);
    }
}