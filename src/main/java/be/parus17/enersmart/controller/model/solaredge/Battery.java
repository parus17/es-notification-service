package be.parus17.enersmart.controller.model.solaredge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter public class Battery {
    private String name;
    private String manufacturer;
    private String model;
    private String firmwareVersion;
    private String connectedTo;
    private String connectedInverterSn;
    private double nameplateCapacity;
    @JsonProperty("SN")
    private String sn;
}
