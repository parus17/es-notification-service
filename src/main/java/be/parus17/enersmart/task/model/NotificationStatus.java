package be.parus17.enersmart.task.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class NotificationStatus {
    private LocalDate treshold90;
    private LocalDate treshold30;
}
