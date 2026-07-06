package com.arka.aws.ses;

import com.arka.exceptions.EmailDeliveryException;
import com.arka.valueobjects.EmailMessage;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@RequiredArgsConstructor
@Service
public class SESEmailService {

    private static final Logger log =
            LoggerFactory.getLogger(SESEmailService.class);

    private final SesClient client;

    @Value("${notifications.email-settings.sender}")
    private String sender;

    public void send(EmailMessage email) {
        sendRaw(email);
    }

    private void sendRaw(EmailMessage email){

        try{

            Session session = Session.getDefaultInstance(new Properties());

            MimeMessage message = new MimeMessage(session);
            MimeMultipart multipart = new MimeMultipart("mixed");

            setEmailHeaders(message, email);
            addEmailTextBody(email.getBody(), multipart);

            message.setContent(multipart);

            sendEmail(mimeToRawMessage(message));

        } catch (Exception e) {
            log.error("SES error sending to {}: {}", email.getRecipient(), e.getMessage());
            throw new EmailDeliveryException("Failed to send email via AWS SES", e);
        }
    }

    private void setEmailHeaders(MimeMessage message,
                                 EmailMessage input) throws MessagingException {

        message.setSubject(input.getSubject(), "UTF-8");
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(input.getRecipient()));
    }

    private void addEmailTextBody(String textBody, MimeMultipart multiPart)
            throws MessagingException {

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(textBody, "text/html; charset=UTF-8");
        multiPart.addBodyPart(textPart);
    }

    private RawMessage mimeToRawMessage(MimeMessage message)
            throws MessagingException, IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);

        SdkBytes data = SdkBytes.fromByteArray(outputStream.toByteArray());
        return RawMessage.builder().data(data).build();
    }

    private void sendEmail(RawMessage rawMessage){

        SendRawEmailRequest rawEmailRequest =
                SendRawEmailRequest.builder()
                        .rawMessage(rawMessage).build();

        client.sendRawEmail(rawEmailRequest);
        log.info("Email sent successfully via SES");
    }
}
