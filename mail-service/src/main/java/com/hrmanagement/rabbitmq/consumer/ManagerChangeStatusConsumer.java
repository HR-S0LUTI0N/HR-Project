package com.hrmanagement.rabbitmq.consumer;

import com.hrmanagement.rabbitmq.model.ManagerChangeStatusModel;
import com.hrmanagement.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerChangeStatusConsumer {
    private final MailService mailService;

    @RabbitListener(queues = "${rabbitmq.queueManagerChangeStatus}")
    public void sendInfoMailForManager(ManagerChangeStatusModel model) {
        mailService.sendInfoMailForManager(model);
    }
}
