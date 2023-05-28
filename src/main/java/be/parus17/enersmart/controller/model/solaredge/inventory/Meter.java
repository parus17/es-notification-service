package be.parus17.enersmart.controller.model.solaredge.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meter {
    private String name;
    private String manufacturer;
    private String model;
    private String firmwareVersion;
    private String connectedTo;
    private String connectedSolaredgeDeviceSN;
    private String type;
    private String form;
    private String sn;
}
