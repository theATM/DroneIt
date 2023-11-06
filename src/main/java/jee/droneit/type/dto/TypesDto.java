package jee.droneit.type.dto;


import jee.droneit.type.entity.Type;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter @Setter @ToString @EqualsAndHashCode
@Builder(builderClassName = "TypesDtoBuilder", builderMethodName="buildingRome")
@NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TypesDto
{

    @Getter @Setter @ToString @EqualsAndHashCode
    @Builder(builderClassName = "InnerTypeBuilder")
    @NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class InnerType
    {
        private Long typeID;
        private String name;
        private String brand;
        private String manufacturer;
    }

    @Singular("innerTypeElem")
    private List<TypesDto.InnerType> innerTypeList;


    public static Function<Collection<Type>, TypesDto> entityToDtoMapper()
    {
        return types ->
        {
            TypesDto.TypesDtoBuilder response = TypesDto.buildingRome();
            types.stream()
                    .map( type -> TypesDto.InnerType.builder()
                            .typeID(type.getTypeID())
                            .name(type.getName())
                            .brand(type.getBrand())
                            .manufacturer(type.getManufacturer())
                            .build()
                    ).forEach( response::innerTypeElem );
            return response.build();
        };
    }
}
