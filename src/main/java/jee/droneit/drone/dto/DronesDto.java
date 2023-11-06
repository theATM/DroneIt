package jee.droneit.drone.dto;

import jee.droneit.drone.entity.Drone;
import jee.droneit.type.dto.TypeDto;
import jee.droneit.user.dto.UserDto;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder(builderClassName = "DronesDtoBuilder",  builderMethodName="droneConstruction")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class DronesDto
{

    @Getter
    @Setter
    @Builder(builderMethodName="innerDroneConstruction")
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class InnerDrone
    {
        private Long droneID;
        private String licenceNumber;
        private LocalDate productionDate;
        private TypeDto droneType;
        private UserDto ownUser;
    }


    @Singular("innerDroneElem")
    private List<InnerDrone> innerDroneList;

    public static Function<Collection<Drone>, DronesDto> entityToDtoMapper()
    {
        return drones ->
        {
            DronesDto.DronesDtoBuilder response = DronesDto.droneConstruction();
            drones.stream()
                    .map(d -> DronesDto.InnerDrone.innerDroneConstruction()
                            .droneID(d.getDroneID())
                            .licenceNumber(d.getLicenceNumber())
                            .productionDate(d.getProductionDate())
                            .droneType(TypeDto.entityToDtoMapper().apply(d.getDroneType()))
                            .ownUser(UserDto.entityToDtoMapper().apply(d.getOwnUser()))
                            .build())
                    .forEach(response::innerDroneElem);
            return response.build();
        };
    }

}