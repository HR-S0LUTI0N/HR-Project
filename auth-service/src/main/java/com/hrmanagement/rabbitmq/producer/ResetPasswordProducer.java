package com.hrmanagement.rabbitmq.producer;

import com.hrmanagement.rabbitmq.model.ResetPasswordModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordProducer {
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;
    @Value("${rabbitmq.resetPasswordMailBindingKey}")
    private String resetPasswordMailBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendResetPassword(ResetPasswordModel model){
        System.out.println(model);
        rabbitTemplate.convertAndSend(exchange,resetPasswordMailBindingKey,model);
    }
}
