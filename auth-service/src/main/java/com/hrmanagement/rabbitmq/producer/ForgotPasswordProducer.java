package com.hrmanagement.rabbitmq.producer;

import com.hrmanagement.rabbitmq.model.ForgotPasswordMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordProducer {
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;
    @Value("${rabbitmq.forgotPasswordMailBindingKey}")
    private String forgotPasswordMailBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendForgotPassword(ForgotPasswordMailModel model){
        rabbitTemplate.convertAndSend(exchange,forgotPasswordMailBindingKey,model);
    }
}
