package be.parus17.enersmart.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NotificationTask {
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void executeNotificationTask(){
        log.debug("notification");
    }
}
