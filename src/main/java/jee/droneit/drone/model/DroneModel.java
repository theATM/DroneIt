package jee.droneit.drone.model;


import jee.droneit.drone.entity.Drone;
import jee.droneit.drone.validator.CreateGroup;
import jee.droneit.drone.validator.ToLongId;
import jee.droneit.type.entity.Type;
import jee.droneit.type.model.TypeModel;
import jee.droneit.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class DroneModel
{
    private Long droneID;
    private String licenceNumber;
    private LocalDate productionDate;
    private TypeModel droneType;
    private Long version;

    public static Function<Drone, DroneModel> entityToModelMapper()
    {
        return dm -> DroneModel.builder()
                .droneID(dm.getDroneID())
                .licenceNumber(dm.getLicenceNumber())
                .productionDate(dm.getProductionDate())
                .droneType(TypeModel.entityToModelMapper().apply(dm.getDroneType()))
                .version(dm.getVersion())
                .build();
    }

    public static Function<DroneModel, Drone> modelToEntityMapper(Function<Long, Type> typeFunction, Supplier<User> userSupplier)
    {
        return d -> Drone.builder()
                .droneID(d.getDroneID())
                .licenceNumber(d.getLicenceNumber())
                .productionDate(d.getProductionDate())
                .droneType(typeFunction.apply(d.getDroneID()))
                .ownUser(userSupplier.get())
                .build();
    }


    public static BiFunction<Drone,DroneModel,Drone> modelToEntityUpdater()
    {
        return ( drone, request) ->
        {
            drone.setDroneID(request.getDroneID());
            drone.setLicenceNumber(request.getLicenceNumber());
            drone.setProductionDate(request.getProductionDate());
            drone.setVersion(request.getVersion());
            return drone;
        };
    }

}

