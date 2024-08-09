package com.sandesDev.apiConsumer.services;

import com.sandesDev.apiConsumer.models.EmailModels;
import com.sandesDev.apiConsumer.records.ClientDto;
import com.sandesDev.apiConsumer.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServices {

    private static final String EMAIL = "sandesjean.sandes@gmail.com" ;
    private EmailRepository emailRepository;
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    public EmailServices(JavaMailSender mailSender, EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
    }
    public void sendMessage(String recipient,String subject, String message) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(recipient);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);
        }catch (Exception e){
            System.out.println("erro "+e.getMessage());
            System.out.println("por causa +"+e.getCause());
            System.out.println("por causa +"+e.getStackTrace());
            System.out.println("por causa +"+e.getLocalizedMessage());
        }

    }

    public void save(ClientDto dto) {
        String message = "parabéns, "+dto.username()+"! voce foi cadastrado com sucesso no twitter, " +
                "espero que voce aproveite !";
        String subject = "Novo usuário!";
        String user = "Novo usuário, nome:  "+ dto.username();
        EmailModels email = new EmailModels();
        email.setEmail(dto.email());
        email.setMessage(message);
        email.setUsername(dto.username());
        emailRepository.save(email);
        // enviar o email para o cliente cadastrado
        sendMessage(dto.email(),subject,message);
        // enviar o email de confirmação de mais um cliente para o administrador
        sendMessage(EMAIL,subject,user);
    }

    public void delete(ClientDto dto) {
        String message = "Olá, "+dto.username()+"! infelizmente sua conta foi removida da nossa plataforma!";
        String subject = "Conta removida!";
        String user = "Cliente removido, nome:  "+ dto.username();
        var email = emailRepository.findByUsername(dto.username());
        emailRepository.delete(email.get());
        // enviar o email para o cliente removido
        sendMessage(dto.email(),subject,message);
        // enviar o email de confirmação de menos um cliente para o administrador
        sendMessage(EMAIL,subject,user);
    }
}
