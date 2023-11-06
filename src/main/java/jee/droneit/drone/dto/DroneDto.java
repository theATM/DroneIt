package jee.droneit.drone.dto;


import jee.droneit.drone.entity.Drone;
import jee.droneit.type.dto.TypeDto;
import jee.droneit.type.entity.Type;
import jee.droneit.type.model.TypeModel;
import jee.droneit.user.dto.UserDto;
import jee.droneit.user.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class DroneDto
{
    private Long droneID;
    private String licenceNumber;
    private LocalDate productionDate;
    private TypeDto droneType;
    private UserDto ownUser;

    public static Function<DroneDto, Drone> dtoToEntityMapper(Type type, User user)
    {
        return d -> Drone.builder()
                .droneID(d.getDroneID())
                .licenceNumber(d.getLicenceNumber())
                .productionDate(d.getProductionDate())
                .droneType(type)
                .ownUser(user)
                .build();
    }


    public static Function<Drone, DroneDto> entityToDtoMapper()
    {

        return dm -> DroneDto.builder()
                .droneID(dm.getDroneID())
                .licenceNumber(dm.getLicenceNumber())
                .productionDate(dm.getProductionDate())
                .droneType(TypeDto.entityToDtoMapper().apply(dm.getDroneType()))
                .ownUser(UserDto.entityToDtoMapper().apply(dm.getOwnUser()))
                .build();
    }

    public static BiFunction<Drone, DroneDto, Drone> dtoToEntityUpdater()
    {
        return ( drone, request) ->
        {
            drone.setDroneID(request.getDroneID());
            drone.setLicenceNumber(request.getLicenceNumber());
            drone.setProductionDate(request.getProductionDate());
            return drone;
        };
    }



}
