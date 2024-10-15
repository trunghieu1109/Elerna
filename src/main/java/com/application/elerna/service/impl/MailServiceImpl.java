package com.application.elerna.service.impl;

import com.application.elerna.service.JwtService;
import com.application.elerna.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private final TemplateEngine templateEngine;

//    @Autowired
//    public MailServiceImpl(@Qualifier("customTemplateEngine") TemplateEngine templateEngine) {
//        this.templateEngine = templateEngine;
//    }

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

    /**
     *
     * Send email to confirm signup
     *
     * @param email String
     * @param id Long
     * @param username String
     * @param secretKey String
     */
    @Override
    public void sendConfirmLink(String email, Long id, String username, String secretKey) throws MessagingException, UnsupportedEncodingException {
        log.info("Send email to confirm account registration");

        log.info("Create new message");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        log.info("Create confirm link");
        Context context = new Context();
        String linkConfirm = String.format("http://localhost:80/api/v1/auth/confirm-signup/%s?secretCode=%s", id, secretKey).toString();

        log.info("Link confirm: {}", linkConfirm);
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        log.info("Set email content");
        helper.setFrom(mailFrom, "Hieu Nguyen");
        helper.setTo("hieukunno1109@gmail.com");
        helper.setSubject("Please confirm your account");

        String html = templateEngine.process("confirmEmail.html", context);

        helper.setText(html, true);

        mailSender.send(message);

        log.info("Send email successfully");

    }

    /**
     *
     * Send email to confirm signup by Kafka
     *
     * @param message String
     */
    @Override
    @KafkaListener(topics = "confirm-account-topic", groupId = "confirm-account-group")
    public void sendConfirmLinkByKafka(String message) throws MessagingException, UnsupportedEncodingException {
        log.info("Send email to confirm account registration by Kafka");

        String[] arr = message.split(",");

        String email = arr[0].substring(arr[0].indexOf("=") + 1);
        String id = arr[1].substring(arr[1].indexOf("=") + 1);
        String secretKey = arr[2].substring(arr[2].indexOf("=") + 1);

        log.info("Create new message to send by kafka");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        log.info("Create confirm link");
        Context context = new Context();
        String linkConfirm = String.format("http://localhost:80/api/v1/auth/confirm-signup/%s?secretCode=%s", id, secretKey).toString();

        log.info("Link confirm: {}", linkConfirm);
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        log.info("Set email content");
        helper.setFrom(mailFrom, "Hieu Nguyen");
        helper.setTo("hieukunno1109@gmail.com");
        helper.setSubject("Please confirm your account");

        String html = templateEngine.process("confirmEmail.html", context);

        helper.setText(html, true);

        mailSender.send(mimeMessage);

        log.info("Send email successfully");

    }

}
