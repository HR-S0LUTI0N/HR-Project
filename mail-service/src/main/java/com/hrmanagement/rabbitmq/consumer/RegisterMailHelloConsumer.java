package com.hrmanagement.rabbitmq.consumer;

import com.hrmanagement.rabbitmq.model.RegisterMailHelloModel;
import com.hrmanagement.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterMailHelloConsumer {

    private final MailService mailService;

    @RabbitListener(queues = "${rabbitmq.registerMailHelloQueue}")
    public void sendHello(RegisterMailHelloModel registerMailHelloModel){
        mailService.sendHelloMail(registerMailHelloModel);
    }
}
