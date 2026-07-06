package com.arka.valueobjects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class EmailMessage {

    private String recipient;
    private String subject;
    private String body;
    private Map<String, Object> attachment;

    public static EmailMessage create(
            String recipient,
            String subject,
            String body
    ){

        return EmailMessage.builder()
                .recipient(recipient)
                .subject(subject)
                .body(body)
                .attachment(new HashMap<>())
                .build();
    }

    public void addAttachment(String fileName, byte[] fileData){
        this.attachment.put(fileName, fileData);
    }

    public void removeAttachment(String fileName){
        this.attachment.remove(fileName);
    }
}
