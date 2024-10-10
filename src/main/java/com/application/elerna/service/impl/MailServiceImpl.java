package com.application.elerna.service.impl;

import com.application.elerna.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String mailFrom;

    /**
     *
     * Send email
     *
     * @param recipients String
     * @param subject String
     * @param content String
     * @param filePaths String[]
     */
    @Async
    @Override
    public void sendEmail(String recipients, String subject, String content, String[] filePaths) throws MessagingException, UnsupportedEncodingException {

        log.info(" Send Message ");

        // create mime message
        MimeMessage message = mailSender.createMimeMessage();

        // config helper
        log.info("Config message");
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(mailFrom, "Hieu Nguyen");

        // setup receivers
        log.info("Set recipients");
        if (recipients.contains(",")) {
            helper.setTo(InternetAddress.parse(recipients));
        } else {
            helper.setTo(recipients);
        }

        // attach files
        log.info("Attach files");
        if (filePaths != null) {
            for (String filePath : filePaths) {
                System.out.println(filePath);
                FileSystemResource file = new FileSystemResource(new File(filePath));
                helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            }
        }

        // set subject and content
        log.info("Set subject");
        helper.setSubject(subject);

        log.info("Set content");
        helper.setText(content, true);

        // send message
        log.info("Send");
        mailSender.send(message);

//        return "Send email successfully";
    }

}
