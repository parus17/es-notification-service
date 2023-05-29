package be.parus17.enersmart.controller.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class InverterInfo {
    private String manufacturer;
    private String model;
    private String communicationMethod;
    private String cpuVersion;
    private String sn;
    private long connectedOptimizers;
}
