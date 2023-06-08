package com.hrmanagement.rabbitmq.consumer;

import com.hrmanagement.rabbitmq.model.ForgotPasswordMailModel;
import com.hrmanagement.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordConsumer {
    private final MailService mailService;

    @RabbitListener(queues = ("${rabbitmq.queueForgotPassword}"))
    public void sendMailForgotPasswordRequest(ForgotPasswordMailModel model){
        mailService.forgotPasswordRequestMail(model);
    }






}
