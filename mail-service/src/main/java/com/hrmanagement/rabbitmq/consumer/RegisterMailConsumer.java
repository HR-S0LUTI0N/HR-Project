package com.hrmanagement.rabbitmq.consumer;


import com.hrmanagement.rabbitmq.model.RegisterMailModel;
import com.hrmanagement.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterMailConsumer {
    private final MailService mailService;

    @RabbitListener(queues = "${rabbitmq.registerMailQueue}")
    public void sendActivationLink(RegisterMailModel registerMailModel){
        mailService.sendMail(registerMailModel);
    }

}
