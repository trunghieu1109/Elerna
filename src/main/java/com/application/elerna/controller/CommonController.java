package com.application.elerna.controller;

import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {

    private final MailService mailService;

    @PostMapping("/send-email")
    public ResponseData<String> sendEmail(@RequestParam String recipients, @RequestParam String subject, @RequestParam String content, @RequestParam(required = false) MultipartFile[] files) {

        try {
            return new ResponseData<>(HttpStatus.OK, "Send message", mailService.sendEmail(recipients, subject, content, files));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseData<>(HttpStatus.BAD_REQUEST, "Cant send message");
        }
    }

}
