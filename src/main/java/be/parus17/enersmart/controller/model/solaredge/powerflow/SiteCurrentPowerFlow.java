package be.parus17.enersmart.controller.model.solaredge.powerflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteCurrentPowerFlow {
    private float updateRefreshRate;
    private String unit;
    Connection[] connections;
    @JsonProperty("GRID")
    Grid grid;
    @JsonProperty("LOAD")
    Load load;
    @JsonProperty("PV")
    Pv pv;
    @JsonProperty("STORAGE")
    Storage storage;
}
