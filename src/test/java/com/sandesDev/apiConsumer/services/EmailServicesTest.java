package com.sandesDev.apiConsumer.services;

import com.sandesDev.apiConsumer.models.EmailModels;
import com.sandesDev.apiConsumer.records.ClientDto;
import com.sandesDev.apiConsumer.repositories.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class EmailServicesTest {
    private static final String NAME = "jean";
    private static final String SUBJECT = "TITULOO";
    private static final String MESSAGE = "MENSAGEM DE TEXTE";
    private static final String EMAIL = "sandesjean.sandes@gmail.com";
    private static final String PASSWORD = "123";
    String sender = "text.sender@gmail.com";
    @InjectMocks
    private EmailServices emailServices;
    @Mock
    private EmailRepository emailRepository;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private SimpleMailMessage mailMessage = new SimpleMailMessage();;

    ClientDto dto;
    EmailModels emailModels;
    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;
    @Captor
    private ArgumentCaptor<EmailModels> emailModelsCaptor;

    @BeforeEach
    void setUp() {
        openMocks(this);
        startContent();
    }

    private void startContent() {
        dto = new ClientDto(NAME,EMAIL,PASSWORD);
        emailModels = new EmailModels(UUID.randomUUID(),NAME,EMAIL,PASSWORD);
        emailServices = new EmailServices(mailSender,emailRepository);
    }

    @Test
    void whenSendMessageThenReturnEmailMessage() {
        mailMessage.setFrom(sender);
        mailMessage.setTo(EMAIL);
        mailMessage.setSubject(SUBJECT);
        mailMessage.setText(MESSAGE);

        // Act
        emailServices.sendMessage(EMAIL, SUBJECT, MESSAGE);
        // Assert
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        // Verify the captured SimpleMailMessage content
        assertEquals(EMAIL, capturedMessage.getTo()[0]); // Para um único destinatário
        assertEquals(SUBJECT, capturedMessage.getSubject());
        assertEquals(MESSAGE, capturedMessage.getText());

    }
    @Test
    void testSendMessageHandlesException() {

        // Simule uma exceção ao chamar o método send do JavaMailSender
        doThrow(new RuntimeException("Simulated Exception")).when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailServices.sendMessage(EMAIL, SUBJECT, MESSAGE);

        // Não precisa de assert aqui, pois estamos testando se o bloco catch funciona
        // Você pode verificar as saídas do console, mas geralmente isso não é feito em testes unitários
        // Outra abordagem seria capturar logs ou usar uma abordagem mais avançada para verificar a execução do catch
    }

    @Test
    void whenSaveThenReturnSuccess() {
        // Arrange
        String expectedMessage = "parabéns, "+dto.username()+"! voce foi cadastrado com sucesso no twitter, " +
                "espero que voce aproveite !";
        String expectedSubject = "Novo usuário!";
        String expectedAdminMessage = "Novo usuário, nome:  "+ dto.username();

        // Act
        emailServices.save(dto);

        // Assert
        // Verificar se o objeto EmailModels foi salvo corretamente
        verify(emailRepository).save(emailModelsCaptor.capture());
        EmailModels savedEmail = emailModelsCaptor.getValue();
        assertEquals(NAME, savedEmail.getUsername());
        assertEquals(EMAIL, savedEmail.getEmail());
        assertEquals(expectedMessage, savedEmail.getMessage());

        // Verificar se os dois emails foram enviados
        verify(mailSender, times(2)).send(messageCaptor.capture());
        SimpleMailMessage firstMessage = messageCaptor.getAllValues().get(0);
        SimpleMailMessage secondMessage = messageCaptor.getAllValues().get(1);

        // Verificar o email enviado ao cliente
        assertEquals(EMAIL, firstMessage.getTo()[0]);
        assertEquals(expectedSubject, firstMessage.getSubject());
        assertEquals(expectedMessage, firstMessage.getText());

        // Verificar o email enviado ao administrador
        assertEquals("sandesjean.sandes@gmail.com", secondMessage.getTo()[0]);
        assertEquals(expectedSubject, secondMessage.getSubject());
        assertEquals(expectedAdminMessage, secondMessage.getText());
    }
    @Test
    void whenDeleteThenReturnSuccess() {
        String message = "Olá, "+dto.username()+"! infelizmente sua conta foi removida da nossa plataforma!";
        String subject = "Conta removida!";
        String user = "Cliente removido, nome:  "+ dto.username();
        when(emailRepository.findByUsername(any())).thenReturn(Optional.of(emailModels));
        doNothing().when(emailRepository).delete(any());

        emailServices.delete(dto);

        verify(emailRepository , times(1)).findByUsername(any());
        verify(emailRepository , times(1)).delete(any());

        // Verificar se os dois emails foram enviados
        verify(mailSender, times(2)).send(messageCaptor.capture());
        SimpleMailMessage firstMessage = messageCaptor.getAllValues().get(0);
        SimpleMailMessage secondMessage = messageCaptor.getAllValues().get(1);

        // Verificar o email enviado ao cliente
        assertEquals(subject, firstMessage.getSubject());
        assertEquals(message, firstMessage.getText());

        // Verificar o email enviado ao administrador
        assertEquals("sandesjean.sandes@gmail.com", secondMessage.getTo()[0]);
        assertEquals(subject, secondMessage.getSubject());
        assertEquals(user, secondMessage.getText());
    }


}
