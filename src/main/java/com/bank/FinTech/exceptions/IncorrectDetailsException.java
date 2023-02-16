package com.bank.FinTech.exceptions;

public class IncorrectDetailsException extends RuntimeException{
    public IncorrectDetailsException(String message) {
        super(message);
    }
}
