package be.parus17.enersmart.task;

import be.parus17.enersmart.task.model.NotificationStatus;
import be.parus17.enersmart.task.model.NotificationTaskContent;
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
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationTask {
    private ObjectMapper objectMapper;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://maker.ifttt.com/trigger/battery_treshold_reached/with/key/cUkiWNvA9P7AqdwmyjzWET")
            .build();
    private final String notificationStatusPath = "notification_status.json";

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void executeNotificationTask() {
        NotificationStatus notificationStatus = retrieveNotificationStatus();

        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(NotificationTaskContent.builder().value1("Hello").build()))
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();

        storeNotificationStatus(notificationStatus);
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
