package com.sandesDev.apiConsumer.services;

import com.sandesDev.apiConsumer.records.ClientDto;
import com.sandesDev.apiConsumer.repositories.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class EmailServicesTest {
    private static final String NAME = "jean";
    private static final String EMAIL = "sandesjean.sandes@gmail.com";
    private static final String PASSWORD = "123";
    @InjectMocks
    private EmailServices emailServices;
    @Mock
    private EmailRepository emailRepository;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private SimpleMailMessage mailMessage = new SimpleMailMessage();;
    String sender = "sandesjean.sandes@gmail.com";
    ClientDto dto;
    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @BeforeEach
    void setUp() {
        openMocks(this);
        startContent();
    }

    private void startContent() {
        dto = new ClientDto(NAME,EMAIL,PASSWORD);
    }

    @Test
    void whenSendMessageThenReturnEmailMessage() {
        // Arrange
        String sender = "sandesjean.sandes@gmail.com";
        String recipient = "recipient@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        // Mock the SimpleMailMessage
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(recipient);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        // Act
        emailServices.sendMessage(recipient, subject, message);
        // Assert
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        // Verify the captured SimpleMailMessage content
        assertEquals(recipient, capturedMessage.getTo()[0]); // Para um único destinatário
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals(message, capturedMessage.getText());

    }

    @Test
    void save() {

    }

    @Test
    void delete() {
    }
}