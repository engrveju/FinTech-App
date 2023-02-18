package com.bank.FinTech.exceptions;

public class IncorrectTransactionPinException extends RuntimeException {
    public IncorrectTransactionPinException(String message) {
        super(message);
    }
}
