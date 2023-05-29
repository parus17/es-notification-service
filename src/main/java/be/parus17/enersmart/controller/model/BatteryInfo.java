package be.parus17.enersmart.controller.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BatteryInfo {
    private String manufacturer;
    private String model;
    private String firmwareVersion;
    private double nameplateCapacity;
    private String sn;
    private InverterInfo inverter;
}
