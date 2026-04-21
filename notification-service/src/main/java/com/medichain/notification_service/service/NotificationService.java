package com.medichain.notification_service.service;

import com.medichain.notification_service.dto.BedAssignmentEvent;
import com.medichain.notification_service.dto.DoctorAssignmentEvent;
import com.medichain.notification_service.dto.EmergencyCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
//KAFKA producer-consumer workflow
@Service
@Slf4j
public class NotificationService {

    public void sendEmergencyCreatedNotification(EmergencyCreatedEvent event) {
        // SMS notification to patient
        sendSMS(event.getPatientPhone(),
                "MediChain Alert: Your SOS (ID: " + event.getEmergencyId() +
                        ") has been received. Help is on the way! " +
                        "Incident: " + event.getIncidentType() +
                        " at " + event.getLocation());

        // Log notification
        log.info("Emergency notification sent to patient: {}",
                event.getPatientPhone());
    }

    public void sendBedAssignedNotification(BedAssignmentEvent event) {
        if (event.isSuccess()) {
            log.info("Bed assignment notification: Emergency {} assigned to " +
                            "bed {} at {}", event.getEmergencyId(),
                    event.getBedId(), event.getHospitalName());

            sendSMS("HOSPITAL-ADMIN",
                    "MediChain: Emergency patient incoming to " +
                            event.getHospitalName() + ". " +
                            event.getWardType() + " bed " + event.getBedId() +
                            " reserved.");
        } else {
            log.warn("Bed assignment failed for emergency {}: {}",
                    event.getEmergencyId(), event.getFailureReason());
        }
    }

    public void sendDoctorAssignedNotification(DoctorAssignmentEvent event) {
        if (event.isSuccess()) {
            log.info("Doctor assignment notification: Dr. {} assigned to " +
                            "emergency {}", event.getDoctorName(),
                    event.getEmergencyId());

            sendSMS("DOCTOR-" + event.getDoctorId(),
                    "MediChain Alert: You have been assigned to Emergency ID: " +
                            event.getEmergencyId() +
                            ". Please prepare immediately.");
        } else {
            log.warn("Doctor assignment failed for emergency {}: {}",
                    event.getEmergencyId(), event.getFailureReason());
        }
    }

    private void sendSMS(String recipient, String message) {
        // In production this would call Twilio/MSG91 API
        // For now we log it as a mock
        log.info("SMS to [{}]: {}", recipient, message);
    }

    private void sendEmail(String recipient, String subject, String body) {
        // In production this would use JavaMailSender
        log.info("Email to [{}] - Subject: {} | Body: {}", recipient, subject, body);
    }

    private void sendPushNotification(String userId, String message) {
        // In production this would call Firebase FCM
        log.info("Push to [{}]: {}", userId, message);
    }
}
