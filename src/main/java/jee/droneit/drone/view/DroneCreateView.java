package jee.droneit.drone.view;

import jee.droneit.drone.model.DroneCreateModel;
import jee.droneit.drone.model.DroneModel;
import jee.droneit.drone.service.DroneService;


import jee.droneit.type.model.TypeModel;
import jee.droneit.type.service.TypeService;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class DroneCreateView implements Serializable
{

    private DroneService droneService;
    private TypeService typeService;


    @Getter
    @Setter
    private long id;


    @Getter
    private DroneCreateModel droneCreateModel;

    @Getter
    private List<TypeModel> typeModels;

    @Inject
    public DroneCreateView(DroneCreateModel droneCreateModel)
    {
        this.droneCreateModel=droneCreateModel;
    }

    @EJB
    public void setDroneService(DroneService droneService)
    {
        this.droneService = droneService;
    }
    @EJB
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached wihitn
     * field and initialized during init of the view.
     */
    @PostConstruct
    public void init()
    {
        //droneCreateModel = DroneCreateModel.builder().build();
        typeModels = typeService.findAll().stream()
                .map(TypeModel.entityToModelMapper())
                .collect(Collectors.toList());
    }

    public String saveAction()
    {

        this.droneService.createForCurrentUser(
                DroneCreateModel.modelToEntityMapper(
                    x -> typeService.find(droneCreateModel.getDroneType().getTypeID()).orElseThrow(),() -> null
                )
                .apply(droneCreateModel)
        );

        return "/types/typeList.xhtml?faces-redirect=true";
    }

    public String cancelAction()
    {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return "/types/typeList.xhtml?faces-redirect=true";
    }

}
