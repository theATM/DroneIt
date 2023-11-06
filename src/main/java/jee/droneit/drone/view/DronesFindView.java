package jee.droneit.drone.view;

import jee.droneit.drone.entity.Drone;
import jee.droneit.drone.model.DroneModel;
import jee.droneit.drone.model.DronesModel;
import jee.droneit.drone.service.DroneService;
import jee.droneit.type.model.TypesModel;
import jee.droneit.user.entity.UserRoles;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestScoped
@Named
public class DronesFindView implements Serializable
{
    @Getter
    @Setter
    private Long ID;
    @Getter
    @Setter
    private String licence;
    @Getter
    @Setter
    private LocalDate prodDate;
    @Getter
    @Setter
    private String typeName;

    private DroneService droneService;
    @Getter
    private DronesModel dronesModel;

    @Getter
    private DroneModel droneFindModel;

    public DronesFindView(){}
    @EJB
    public void setDroneService(DroneService droneService){this.droneService = droneService;}


    public void init() throws IOException
    {
       // droneFindModel = DroneModel.builder().build();
        List<Drone> drones;

        drones = this.droneService.findAll();


        this.dronesModel = DronesModel.entityToModelMapper().apply(drones);


    }

    public void findAction()
    {
        //Drone droneTemp = DroneModel.modelToEntityMapper(null,() -> null).apply(droneFindModel);
        List<Drone> fdrones =  droneService.findResembling(ID,licence,prodDate,typeName);
        this.dronesModel = DronesModel.entityToModelMapper().apply(fdrones);
        //return  FacesContext.getCurrentInstance().getViewRoot().getViewId() +  "?faces-redirect=true";
    }

}
