package be.parus17.enersmart.controller.model.solaredge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InventoryResponse {
    @JsonProperty("Inventory")
    private Inventory inventory;
}
