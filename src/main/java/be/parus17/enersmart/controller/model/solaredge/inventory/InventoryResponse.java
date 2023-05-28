package be.parus17.enersmart.controller.model.solaredge.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponse {
    @JsonProperty("Inventory")
    private Inventory inventory;
}
