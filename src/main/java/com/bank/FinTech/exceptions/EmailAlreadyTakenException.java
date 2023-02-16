package com.bank.FinTech.exceptions;

public class EmailAlreadyTakenException extends RuntimeException{

    public EmailAlreadyTakenException(String message){
        super(message);
    }
}
