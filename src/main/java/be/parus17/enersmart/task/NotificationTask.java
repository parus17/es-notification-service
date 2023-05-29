package be.parus17.enersmart.task;

import be.parus17.enersmart.controller.model.StorageInfo;
import be.parus17.enersmart.controller.model.solaredge.powerflow.PowerFlowResponse;
import be.parus17.enersmart.controller.model.solaredge.powerflow.Storage;
import be.parus17.enersmart.task.model.Notification;
import be.parus17.enersmart.task.model.NotificationStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationTask {
    private ObjectMapper objectMapper;
    private final String notificationStatusPath = "notification_status.json";
    private final List<String> interestedDeviceUrls = Arrays.asList(
            "https://maker.ifttt.com/trigger/battery_treshold_reached/with/key/cUkiWNvA9P7AqdwmyjzWET",
            "https://maker.ifttt.com/trigger/battery_treshold_reached/with/key/bMWxig56Wvd4J4LEBLUdgQ",
            "https://maker.ifttt.com/trigger/battery_treshold_reached/with/key/0hRdNZLaQeJod_nhOHcqw"
    );


    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void executeNotificationTask() {
        log.debug("Execute notification task");

        WebClient batteryWebClient = WebClient.builder()
                .baseUrl("https://monitoringapi.solaredge.com/site/1453155")
                .build();

        batteryWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/currentPowerFlow")
                        .queryParam("api_key", "9U5TH6DR27F7YMPNX0BCYTG58TC9CIK3")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PowerFlowResponse.class)
                .map(NotificationTask::mapToStorageInfo)
                .subscribe(this::processStorageInfo);
    }

    private void processStorageInfo(StorageInfo storageInfo) {
        log.debug(storageInfo.toString());
        NotificationStatus notificationStatus = retrieveNotificationStatus();

        Notification notification = null;

        if (storageInfo.getChargeLevel() > 90) {
            if (Objects.isNull(notificationStatus.getTreshold90())) {
                notificationStatus.setTreshold90(LocalDate.now());
                notification = Notification.builder()
                        .value1("Battery charge level > 90%")
                        .build();
            }
        } else {
            notificationStatus.setTreshold90(null);
        }

        if (storageInfo.getChargeLevel() < 30) {
            if (Objects.isNull(notificationStatus.getTreshold30())) {
                notificationStatus.setTreshold30(LocalDate.now());
                notification = Notification.builder()
                        .value1("Battery charge level < 30%")
                        .build();
            }
        } else {
            notificationStatus.setTreshold30(null);
        }

        if (Objects.nonNull(notification)) {
            sendNotification(notification);
        }

        storeNotificationStatus(notificationStatus);
    }

    private void sendNotification(Notification notification) {
        interestedDeviceUrls.parallelStream().forEach(interestedDeviceUrl -> {
            WebClient notificationWebClient = WebClient.builder()
                    .baseUrl(interestedDeviceUrl)
                    .build();

            notificationWebClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(notification))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();
        });
    }

    private static StorageInfo mapToStorageInfo(PowerFlowResponse response) {
        // TODO improve
        Storage storage = response.getSiteCurrentPowerFlow().getStorage();

        return StorageInfo.builder()
                .status(storage.getStatus())
                .currentPower(storage.getCurrentPower())
                .chargeLevel(storage.getChargeLevel())
                .build();
    }

    private NotificationStatus retrieveNotificationStatus() {
        try {
            log.debug("NotificationStatus retrieve");
            return objectMapper.readValue(new File(notificationStatusPath), NotificationStatus.class);
        } catch (IOException e) {
            log.debug("NotificationStatus init");
            return NotificationStatus.builder().build();
        }
    }

    private void storeNotificationStatus(NotificationStatus notificationStatus) {
        try {
            log.debug("NotificationStatus store");
            objectMapper.writeValue(new File(notificationStatusPath), notificationStatus);
        } catch (IOException e) {
            log.error("NotificationStatus store failed");
            throw new RuntimeException(e);
        }
    }
}
