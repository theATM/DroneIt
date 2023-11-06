package jee.droneit.type.model;


import jee.droneit.type.entity.Type;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes.
 */

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class TypeModel
{
    private Long typeID; //Key
    private String name;
    private String brand;
    private String manufacturer;
    
    public static Function<Type, jee.droneit.type.model.TypeModel> entityToModelMapper()
    {
        return t -> TypeModel.builder()
                .typeID(t.getTypeID())
                .name(t.getName())
                .brand(t.getBrand())
                .manufacturer(t.getManufacturer())
                .build();
    }



    public Long getTypeID()
    {
        return typeID;
    }


}

