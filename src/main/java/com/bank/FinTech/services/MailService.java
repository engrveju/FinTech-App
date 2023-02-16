package com.bank.FinTech.services;

import com.bank.FinTech.dto.MailServiceDto;
import org.springframework.mail.MailException;

public interface MailService {

    public void sendNotification(MailServiceDto mailServiceDto) throws MailException;
}

