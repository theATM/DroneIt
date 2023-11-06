package jee.droneit.drone.model;

import jee.droneit.drone.entity.Drone;
import jee.droneit.type.model.TypeModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class DronesModel  implements Serializable
{

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class DroneModel
    {
        private Long droneID;
        private String licenceNumber;
        private LocalDate productionDate;
        private LocalDateTime creationDateTime;
        private LocalDateTime lastEditionDateTime;
        private Long version;
        private TypeModel droneType;
    }

    @Singular
    private List<DroneModel> drones;

    public static Function<Collection<Drone>, DronesModel> entityToModelMapper()
    {
        return drones -> {
            DronesModel.DronesModelBuilder model = DronesModel.builder();
            drones.stream()
                    .map(d -> DroneModel.builder()
                            .droneID(d.getDroneID())
                            .licenceNumber(d.getLicenceNumber())
                            .productionDate(d.getProductionDate())
                            .version(d.getVersion())
                            .creationDateTime(d.getCreationDateTime())
                            .lastEditionDateTime(d.getLastEditionDateTime())
                            .droneType(TypeModel.entityToModelMapper().apply(d.getDroneType()))
                            .build())
                    .forEach(model::drone);
            return model.build();
        };
    }


}
