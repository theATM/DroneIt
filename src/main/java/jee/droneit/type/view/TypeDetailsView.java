package jee.droneit.type.view;


import jee.droneit.type.entity.Type;
import jee.droneit.type.model.TypeModel;
import jee.droneit.type.service.TypeService;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequestScoped
@Named
public class TypeDetailsView
{

    private TypeService typeService;



    @Getter
    @Setter
    private long id;

    @Getter
    private TypeModel typeModel;

    public TypeDetailsView() {}

    @EJB
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void init() throws IOException
    {
        Optional<Type> type = this.typeService.find(id);
        if (type.isPresent())
        {
            this.typeModel = TypeModel.entityToModelMapper().apply(type.get());
        }
        else
        {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "This Type not found");
        }
    }

}
