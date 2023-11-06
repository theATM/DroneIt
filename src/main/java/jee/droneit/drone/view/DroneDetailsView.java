package jee.droneit.drone.view;


import jee.droneit.drone.entity.Drone;
import jee.droneit.drone.model.DroneModel;
import jee.droneit.drone.service.DroneService;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@RequestScoped
@Named
public class DroneDetailsView implements Serializable
{

    private DroneService droneService;



    @Getter
    @Setter
    private Long id;

    @Getter
    private DroneModel droneModel;


    public DroneDetailsView(){}

    @EJB
    public void setDroneService(DroneService droneService){this.droneService = droneService;}


    public void init() throws IOException
    {
        Optional<Drone> drone = this.droneService.find(id);
        if (drone.isPresent())
        {
            this.droneModel = DroneModel.entityToModelMapper().apply(drone.get());
        }
        else
        {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "This Drone is not found");
        }
    }

}
