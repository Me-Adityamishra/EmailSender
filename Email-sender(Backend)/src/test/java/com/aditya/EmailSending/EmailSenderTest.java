package com.aditya.EmailSending;

import com.aditya.EmailSending.Service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
public class EmailSenderTest {
    @Autowired
    private EmailService emailService;

    @Test
    void emailSendTest() {
        System.out.println("Sending email");
        // Corrected method call syntax
        emailService.sendEmail("mishraaditya62096@gmail.com",
                "Email from Spring Boot",
                "This email is sent using Spring Boot while creating email service.");
    }
    @Test
    void sendHtmlInEmail() {
        String html =
                "<h1 style='color:red; border:1px solid red;'>Welcome to Learn Code with Aditya</h1>";
        emailService.sendEmailWithHtml("mishraaditya62096@gmail.com",
                "HTML Email from Spring Boot",
                html); // Pass the HTML content here
    }
    @Test
    void sendEmailWithFile()
    {
        emailService.sendEmailWithFile("mishraaditya62096@gmail.com",
                " Email With File",
                 "This email contains file",
                  new File("C:\\Users\\Aditya\\OneDrive\\Desktop\\JavaProject\\EmailSending\\src\\main\\resources\\static\\Dp.jpg")
                );

    }
    @Test
    void sendEmailWithFileWithStream() {
        // Define email details
        String to = "mishraaditya62096@gmail.com";
        String subject = "Test Email with Attachment";
        String message = "This is a test email sent using Spring Boot with a file attachment.";

        // Load the file as InputStream
        try (InputStream inputStream = new FileInputStream("C:\\Users\\Aditya\\OneDrive\\Desktop\\JavaProject\\EmailSending\\src\\main\\resources\\static\\Dp.jpg")) {
            // Call the email service method
            emailService.sendEmailWithFile(to, subject, message, inputStream);
            System.out.println("Email with file attachment has been sent successfully.");
        } catch (IOException e) {
            // Log and fail the test in case of an exception
            System.err.println("Error loading the file for testing: " + e.getMessage());
            throw new RuntimeException("Failed to send email with file attachment due to an error.", e);
        }
    }


}
