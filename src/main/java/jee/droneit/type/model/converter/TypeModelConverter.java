package jee.droneit.type.model.converter;

import jee.droneit.type.entity.Type;
import jee.droneit.type.model.TypeModel;
import jee.droneit.type.service.TypeService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.Optional;

@FacesConverter(value = "TypeModelConverter", managed = true)
public class TypeModelConverter  implements Converter<TypeModel>
{
    public final TypeService typeService;

    @Inject
    public TypeModelConverter(TypeService typeService )
    {
        this.typeService = typeService;
    }

    @Override
    public TypeModel getAsObject(FacesContext facesContext, UIComponent uiComponent, String value)
    {
        if (value == null || value.isBlank())
        {
            return null;
        }
        Long typeModelID = Long.parseLong(value);
        Optional<Type> type = typeService.find(typeModelID);
        return type.isEmpty() ? null : TypeModel.entityToModelMapper().apply(type.get());
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, TypeModel typeModel) {
        return typeModel == null ? "" : String.valueOf(typeModel.getTypeID());
    }

}
