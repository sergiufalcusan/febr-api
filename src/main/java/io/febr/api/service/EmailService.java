package io.febr.api.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.febr.api.config.SendGridProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class EmailService {
    SendGridProperties sendGridProperties;

    /**
     * Send email
     *
     * @param subject   email subject
     * @param body      email body
     * @param toAddress email address to send to
     */
    public void sendEmail(String subject, String body, String toAddress) throws IOException {
        Email from = new Email(sendGridProperties.fromEmail());
        Email to = new Email(toAddress);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridProperties.apiKey());
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);
    }
}
