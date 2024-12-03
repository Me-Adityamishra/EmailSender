package com.aditya.EmailSending.Impl;

import com.aditya.EmailSending.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("adityakumar03569@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        mailSender.send(simpleMailMessage);
        logger.info("Email has been sent successfully...");
    }

    @Override
    public void sendEmail(String[] to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("adityakumar03569@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        mailSender.send(simpleMailMessage);
        logger.info("Email to multiple recipients has been sent successfully...");
    }

    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("adityakumar03569@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Enable HTML content

            mailSender.send(mimeMessage);
            logger.info("HTML Email has been sent successfully...");
        } catch (MessagingException e) {
            logger.error("Error while sending HTML email", e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    @Override
    public void sendEmailWithFile(String to, String subject, String message, File file) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("adityakumar03569@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message);

            FileSystemResource fileResource = new FileSystemResource(file);
            helper.addAttachment(fileResource.getFilename(), fileResource);

            mailSender.send(mimeMessage);
            logger.info("Email with file attachment has been sent successfully...");
        } catch (MessagingException e) {
            logger.error("Error while sending email with file attachment", e);
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }

    @Override
    public void sendEmailWithFile(String to, String subject, String message, InputStream inputStream) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("adityakumar03569@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message,true);

            // Create a temporary file from InputStream
            File tempFile = File.createTempFile("attachment-", ".png");
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            FileSystemResource fileResource = new FileSystemResource(tempFile);
            helper.addAttachment(fileResource.getFilename(), fileResource);

            mailSender.send(mimeMessage);
            logger.info("Email with file attachment (from InputStream) has been sent successfully...");

            // Clean up temporary file
            tempFile.deleteOnExit();
        } catch (MessagingException e) {
            logger.error("Error while sending email with file attachment", e);
            throw new RuntimeException("Failed to send email with attachment", e);
        } catch (IOException e) {
            logger.error("Error while processing InputStream for file attachment", e);
            throw new RuntimeException("Failed to handle file attachment", e);
        }
    }
}
