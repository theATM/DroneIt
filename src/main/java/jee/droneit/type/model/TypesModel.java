package jee.droneit.type.model;

import jee.droneit.type.entity.Type;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class TypesModel implements Serializable
{
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class TypeModel
    {
        private Long typeID; //Key
        private String name;
        private String brand;
        private String manufacturer;
    }

    @Singular
    private List<TypeModel> types;

    public static Function<Collection<Type>, TypesModel> entityToModelMapper()
    {
        return types ->
        {
            TypesModel.TypesModelBuilder model = TypesModel.builder();
            types.stream().map(t -> TypeModel.builder()
                                    .typeID(t.getTypeID())
                                    .name(t.getName())
                                    .brand(t.getBrand())
                                    .manufacturer(t.getManufacturer())
                                    .build()
                    ).forEach(model::type);
            return model.build();
        };
    }
}
