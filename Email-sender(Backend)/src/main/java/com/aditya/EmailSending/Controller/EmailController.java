package com.aditya.EmailSending.Controller;

import com.aditya.EmailSending.Helper.CustomResponse;
import com.aditya.EmailSending.Modal.EmailRequest;
import com.aditya.EmailSending.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {
    @Autowired
     private EmailService emailService;
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest)
    {

      emailService.sendEmailWithHtml(emailRequest.getTo(),emailRequest.getSubject(),emailRequest.getMessage());
      return ResponseEntity.ok(
              CustomResponse.builder().message("Email send Successfully !!").httpStatus(HttpStatus.OK).success(true).build()
      );
    }
    @PostMapping("/send-with-file")
    public ResponseEntity<CustomResponse> sendWithFile(@RequestPart EmailRequest emailRequest, @RequestPart MultipartFile multipartFile) throws IOException {
    emailService.sendEmailWithFile(emailRequest.getTo(),emailRequest.getSubject(),emailRequest.getMessage(),multipartFile.getInputStream());
        return ResponseEntity.ok(
                CustomResponse.builder().message("Email send Successfully !!").httpStatus(HttpStatus.OK).success(true).build()
        );
    }

}
