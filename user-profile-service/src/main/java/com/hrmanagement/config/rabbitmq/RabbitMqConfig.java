package com.hrmanagement.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.user-exchange}")
    private String exchange;

    @Bean
    DirectExchange directExchange(){return new DirectExchange(exchange);}

    //Personnel Password

    @Value("${rabbitmq.queuePersonnelPassword}")
    private String personnelPasswordQueue;
    @Value("${rabbitmq.personnelPasswordKey}")
    private String personnelPasswordBindingKey;

    @Bean
    Queue personnelPasswordQueue(){return new Queue(personnelPasswordQueue);}

    @Bean
    public Binding bindingPersonnelPassword(final Queue personnelPasswordQueue,final DirectExchange exchange){
        return BindingBuilder.bind(personnelPasswordQueue).to(exchange).with(personnelPasswordBindingKey);
    }


}
