package com.hrmanagement.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //forgot Password Mail
    @Value("${rabbitmq.queueForgotPassword}")
    private String forgotPasswordQueue;

    @Bean
    Queue forgotMailQueue(){
        return new Queue(forgotPasswordQueue);
    }

    //register mail
    @Value("${rabbitmq.registerMailQueue}")
    private String registerMailQueue;

    @Bean
    Queue registerMailQueue(){
        return new Queue(registerMailQueue);
    }

    //Visitor hello mail
    @Value("${rabbitmq.registerMailHelloQueue}")
    private String registerMailHelloQueue;

    @Bean
    Queue registerMailHelloQueue(){
        return new Queue(registerMailHelloQueue);
    }

    //reset Password Mail
    @Value("${rabbitmq.queueResetPassword}")
    private String queueResetPassword;

    @Bean
    Queue queueResetPassword(){return new Queue(queueResetPassword);}

    //personnel Password Mail

    @Value("${rabbitmq.queuePersonnelPassword}")
    private String personnelPasswordQueue;

    @Bean
    Queue personnelPasswordQueue(){return new Queue(personnelPasswordQueue);}




}
