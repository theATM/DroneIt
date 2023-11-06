package jee.droneit.drone.view;

import jee.droneit.drone.model.DroneModel;
import jee.droneit.drone.model.DronesModel;
import jee.droneit.drone.service.DroneService;
import jee.droneit.type.model.TypesModel;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named
public class DronesWithTypeView implements Serializable
{
    private DroneService droneService;

    @Setter
    @Getter
    private Long droneTypeID;

    private DronesModel dronesModel;

    public DronesWithTypeView(){}

    @EJB
    public void setDorneService(DroneService droneService) {
        this.droneService = droneService;
    }

    public DronesModel getDronesModel()
    {
        if (dronesModel == null)
        {
            dronesModel = DronesModel.entityToModelMapper().apply(droneService.findAllWithType(this.droneTypeID));
        }
        return dronesModel;
    }

    public String deleteAction(DronesModel.DroneModel droneModel)
    {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        String answerString  = viewId + "?id="+this.droneTypeID+"&faces-redirect=true";
        this.droneService.delete(droneModel.getDroneID());
        return answerString;
    }

}
