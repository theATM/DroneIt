package jee.droneit.type.view;


import jee.droneit.type.model.TypesModel;
import jee.droneit.type.service.TypeService;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;


/**
 * View bean for rendering list of types.
 */
@RequestScoped
@Named
public class TypeListView
{
    private TypeService typeService;
    private TypesModel typesModel;

    public TypeListView() {}

    @EJB
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypesModel getTypes()
    {
        if (typesModel == null)
        {
            typesModel = TypesModel.entityToModelMapper().apply(typeService.findAll());
        }
        return typesModel;
    }

    public String deleteAction(TypesModel.TypeModel type)
    {
        typeService.delete(type.getTypeID());
        return  FacesContext.getCurrentInstance().getViewRoot().getViewId() +  "?faces-redirect=true";
    }


}
