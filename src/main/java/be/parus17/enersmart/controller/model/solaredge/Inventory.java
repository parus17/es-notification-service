package be.parus17.enersmart.controller.model.solaredge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Inventory {
    private Meter[] meters;
    private Sensor[] sensors;
    private Gateway[] gateways;
    private Battery[] batteries;
    private Inverter[] inverters;
}
