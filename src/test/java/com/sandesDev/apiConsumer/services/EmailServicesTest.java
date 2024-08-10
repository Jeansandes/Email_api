package com.sandesDev.apiConsumer.services;

import com.sandesDev.apiConsumer.records.ClientDto;
import com.sandesDev.apiConsumer.repositories.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class EmailServicesTest {
    @InjectMocks
    private EmailServices emailServices;
    @Mock
    private EmailRepository emailRepository;
    @Mock
    private JavaMailSender mailSender;
    private SimpleMailMessage mailMessage = new SimpleMailMessage();;
    String sender = "sandesjean.sandes@gmail.com";
    ClientDto dto;

    @BeforeEach
    void setUp() {
        openMocks(this);
        startContent();
    }

    private void startContent() {
        dto = new ClientDto("Jean","sandesjean.sandes@gmail.com","123");
    }

    @Test
    void whenSendMessageThenReturnEmailMessage() {
        doNothing().when(mailSender).send(any());
        emailServices.sendMessage("jean@gmail.com","text","teeext");
        verify(mailSender, times(1)).send(mailMessage);
        verify(emailServices,times(1)).save(argThat(dto -> dto.username().equals("Jean")));
        verify(emailServices,times(1)).save(argThat(dto -> dto.email().contains("sandesjean.sandes@gmail.com")));
        verify(emailServices,times(1)).save(argThat(dto -> dto.password().equals("123")));

    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }
}