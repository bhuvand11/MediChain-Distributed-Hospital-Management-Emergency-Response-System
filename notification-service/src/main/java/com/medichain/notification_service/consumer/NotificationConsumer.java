package com.medichain.notification_service.consumer;

import com.medichain.notification_service.dto.BedAssignmentEvent;
import com.medichain.notification_service.dto.DoctorAssignmentEvent;
import com.medichain.notification_service.dto.EmergencyCreatedEvent;
import com.medichain.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "emergency.created",
            groupId = "notification-emergency-group",
            containerFactory = "emergencyListenerFactory")
    public void handleEmergencyCreated(EmergencyCreatedEvent event) {
        log.info("Notification Consumer received emergency.created for ID: {}",
                event.getEmergencyId());
        notificationService.sendEmergencyCreatedNotification(event);
    }

    @KafkaListener(
            topics = "bed.updated",
            groupId = "notification-bed-group",
            containerFactory = "bedListenerFactory")
    public void handleBedAssigned(BedAssignmentEvent event) {
        log.info("Notification Consumer received bed.updated for emergency: {}",
                event.getEmergencyId());
        notificationService.sendBedAssignedNotification(event);
    }

    @KafkaListener(
            topics = "notification.sent",
            groupId = "notification-doctor-group",
            containerFactory = "doctorListenerFactory")
    public void handleDoctorAssigned(DoctorAssignmentEvent event) {
        log.info("Notification Consumer received doctor assignment for emergency: {}",
                event.getEmergencyId());
        notificationService.sendDoctorAssignedNotification(event);
    }
}