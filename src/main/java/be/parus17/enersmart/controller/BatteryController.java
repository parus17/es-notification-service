package be.parus17.enersmart.controller;

import be.parus17.enersmart.controller.model.BatteryInfo;
import be.parus17.enersmart.controller.model.InverterInfo;
import be.parus17.enersmart.controller.model.solaredge.Battery;
import be.parus17.enersmart.controller.model.solaredge.InventoryResponse;
import be.parus17.enersmart.controller.model.solaredge.Inverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/battery")
@AllArgsConstructor
@Slf4j
public class BatteryController {
    private ObjectMapper objectMapper;

    @GetMapping("/info")
    public Mono<BatteryInfo> info() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://monitoringapi.solaredge.com/site/1453155")
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/inventory")
                        .queryParam("api_key", "9U5TH6DR27F7YMPNX0BCYTG58TC9CIK3")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(InventoryResponse.class).map(response -> {
                    // TODO improve
                    Battery battery = response.getInventory().getBatteries()[0];
                    Inverter inverter = response.getInventory().getInverters()[0];

                    return BatteryInfo.builder()
                            .manufacturer(battery.getManufacturer())
                            .model(battery.getModel())
                            .firmwareVersion(battery.getFirmwareVersion())
                            .nameplateCapacity(battery.getNameplateCapacity())
                            .sn(battery.getSn())
                            .inverter(InverterInfo.builder()
                                    .manufacturer(inverter.getManufacturer())
                                    .model(inverter.getModel())
                                    .communicationMethod(inverter.getCommunicationMethod())
                                    .cpuVersion(inverter.getCpuVersion())
                                    .sn(inverter.getSn())
                                    .connectedOptimizers(inverter.getConnectedOptimizers())
                                    .build())
                            .build();
                });
    }
}
