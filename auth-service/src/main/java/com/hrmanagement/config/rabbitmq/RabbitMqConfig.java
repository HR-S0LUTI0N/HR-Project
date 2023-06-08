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
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;

    @Bean
    DirectExchange directExchange() {return new DirectExchange(exchange);}

    //Queue
    @Value("${rabbitmq.queueForgotPassword}")
    private String forgotPasswordQueue;
    @Value("${rabbitmq.forgotPasswordMailBindingKey}")
    private String forgotPasswordMailBindingKey;

    @Bean
    Queue forgotPasswordQueue(){return new Queue(forgotPasswordQueue);}

    @Bean
    public Binding bindingForgotPasswordQueue(final Queue forgotPasswordQueue,final DirectExchange exchange){
        return BindingBuilder.bind(forgotPasswordQueue).to(exchange).with(forgotPasswordMailBindingKey);
    }

    //Mail işlemi için
    @Value("${rabbitmq.registerMailQueue}")
    private String registerMailQueue;
    @Value("${rabbitmq.registerMailBindingKey}")
    private String registerMailBindingKey;

    @Bean
    Queue registerMailQueue(){
        return new Queue(registerMailQueue);
    }

    @Bean
    public Binding bindingRegisterMail(final Queue registerMailQueue, final DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerMailQueue).to(exchangeAuth).with(registerMailBindingKey);
    }

    //ResetPassword
    @Value("${rabbitmq.queueResetPassword}")
    private String queueResetPassword;
    @Value("${rabbitmq.resetPasswordMailBindingKey}")
    private String resetPasswordMailBindingKey;

    @Bean
    Queue queueResetPassword(){return new Queue(queueResetPassword);}

    @Bean
    public Binding bindingResetPasswordMail(final Queue queueResetPassword, final DirectExchange exchange){
        return BindingBuilder.bind(queueResetPassword).to(exchange).with(resetPasswordMailBindingKey);
    }


}
