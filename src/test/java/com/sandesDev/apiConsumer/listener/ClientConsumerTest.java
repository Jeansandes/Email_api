package com.sandesDev.apiConsumer.listener;

import com.sandesDev.apiConsumer.records.ClientDto;
import com.sandesDev.apiConsumer.services.EmailServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.openMocks;

class ClientConsumerTest {

    @InjectMocks
    private ClientConsumer clientConsumer;

    @Mock
    private EmailServices emailServices;

    private final Logger logger = LoggerFactory.getLogger(ClientConsumerTest.class);
    private ClientDto dto;


    @BeforeEach
    void setUp() {
        openMocks(this);
        startContent();
    }

    private void startContent() {
        dto = new ClientDto("Jean","sandesjean.sandes@gmail.com","123");
    }

    @Test
    void orderListener() {
        doNothing().when(emailServices).save(any());

        clientConsumer.orderListener(dto);
        verify(emailServices, times(1)).save(dto);
        verify(emailServices,times(1)).save(argThat(dto -> dto.username().equals("Jean")));
        verify(emailServices,times(1)).save(argThat(dto -> dto.email().contains("sandesjean.sandes@gmail.com")));
        verify(emailServices,times(1)).save(argThat(dto -> dto.password().equals("123")));

    }
}