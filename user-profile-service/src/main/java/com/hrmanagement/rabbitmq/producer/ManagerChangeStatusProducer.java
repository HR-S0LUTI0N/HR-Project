package com.hrmanagement.rabbitmq.producer;

import com.hrmanagement.rabbitmq.model.ManagerChangeStatusModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerChangeStatusProducer {
    @Value("${rabbitmq.user-exchange}")
    private String exchange;
    @Value("${rabbitmq.managerChangeStatusKey}")
    private String managerChangeStatusBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendInfoMailForManager(ManagerChangeStatusModel model) {
        rabbitTemplate.convertAndSend(exchange,managerChangeStatusBindingKey,model);
    }
}
