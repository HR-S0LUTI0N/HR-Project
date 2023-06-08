package com.hrmanagement.rabbitmq.consumer;

import com.hrmanagement.rabbitmq.model.ResetPasswordModel;
import com.hrmanagement.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordConsumer {
    private final MailService mailService;

    @RabbitListener(queues = ("${rabbitmq.queueResetPassword}"))
    public void sendMailResetPassword(ResetPasswordModel model){
        mailService.resetPasswordMail(model);
    }



}
