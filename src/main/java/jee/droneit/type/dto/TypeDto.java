package jee.droneit.type.dto;


import jee.droneit.type.entity.Type;
import lombok.*;

import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@Builder(builderClassName="TypeDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class TypeDto
{
    private Long typeID;
    private String name;
    private String brand;
    private String manufacturer;


    public static Function<Type, TypeDto> entityToDtoMapper()
    {
        return type ->
        {
            TypeDto.TypeDtoBuilder response = TypeDto.builder();

            response.typeID(type.getTypeID());
            response.name(type.getName());
            response.brand(type.getBrand());
            response.manufacturer(type.getManufacturer());
            return response.build();
        };
    }

    public static Function<TypeDto, Type> dtoToEntityMapper()
    {
        return request -> Type.builder()
                .typeID(request.getTypeID())
                .name(request.getName())
                .brand(request.getBrand())
                .manufacturer(request.getManufacturer())
                .build();
    }

    public static BiFunction<Type, TypeDto, Type> dtoToEntityBiMapper() //From update
    {
        return (type, request) ->
        {
            type.setTypeID(request.getTypeID());
            type.setName(request.getName());
            type.setBrand(request.getBrand());
            type.setManufacturer(request.getManufacturer());
            return type;
        };
    }


}
