package com.medichain.emergency_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic emergencyCreatedTopic() {
        return TopicBuilder.name("emergency.created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic bedUpdatedTopic() {
        return TopicBuilder.name("bed.updated")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder.name("notification.sent")
                .partitions(3)
                .replicas(1)
                .build();
    }
}