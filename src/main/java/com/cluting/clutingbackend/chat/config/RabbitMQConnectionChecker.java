package com.cluting.clutingbackend.chat.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.ApplicationRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class RabbitMQConnectionChecker {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConnectionChecker.class);

    public ApplicationRunner checkConnection(ConnectionFactory connectionFactory) {
        return args -> {
            try {
                connectionFactory.createConnection().close();
                logger.info("RabbitMQ connection successful.");
            } catch (Exception e) {
                logger.error("RabbitMQ connection failed: {}", e.getMessage());
                throw new IllegalStateException("Failed to connect to RabbitMQ", e);
            }
        };
    }
}

