package be.parus17.enersmart.controller.model.solaredge.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Inverter {
    private String name;
    private String manufacturer;
    private String model;
    private String communicationMethod;
    private String cpuVersion;
    @JsonProperty("SN")
    private String sn;
    private long connectedOptimizers;
}
