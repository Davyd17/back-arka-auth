package com.arka.valueobjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
class EmailMessageTest {

    private EmailMessage buildEmail() {
        return EmailMessage.create(
                "recipient@arka.com",
                "Test Subject",
                "Test Body");
    }

    @Test
    void shouldCreateEmailWithEmptyAttachments() {
        EmailMessage email = buildEmail();

        assertThat(email.getAttachment()).isEmpty();
    }

    @Test
    void shouldAddAttachment() {
        EmailMessage email = buildEmail();
        byte[] fileData = "file content".getBytes();

        email.addAttachment("report.csv", fileData);

        assertThat(email.getAttachment()).containsKey("report.csv");
        assertThat(email.getAttachment().get("report.csv")).isEqualTo(fileData);
    }

    @Test
    void shouldRemoveAttachment() {
        EmailMessage email = buildEmail();
        email.addAttachment("report.csv", "file content".getBytes());

        email.removeAttachment("report.csv");

        assertThat(email.getAttachment()).doesNotContainKey("report.csv");
    }

    @Test
    void shouldSupportMultipleAttachments() {
        EmailMessage email = buildEmail();

        email.addAttachment("report.csv", "csv content".getBytes());
        email.addAttachment("report.pdf", "pdf content".getBytes());

        assertThat(email.getAttachment()).hasSize(2);
    }
}
