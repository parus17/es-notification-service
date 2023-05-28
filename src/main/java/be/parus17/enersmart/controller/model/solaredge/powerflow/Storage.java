package be.parus17.enersmart.controller.model.solaredge.powerflow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Storage {
    private String status;
    private float currentPower;
    private float chargeLevel;
    private boolean critical;
}
