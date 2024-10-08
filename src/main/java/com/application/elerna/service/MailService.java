package com.application.elerna.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@Service
public interface MailService {

    void sendEmail(String recipients, String subject, String content, String[] filePaths) throws MessagingException, UnsupportedEncodingException;

}
