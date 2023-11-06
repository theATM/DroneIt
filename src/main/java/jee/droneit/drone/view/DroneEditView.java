package jee.droneit.drone.view;


import jee.droneit.drone.entity.Drone;
import jee.droneit.drone.model.DroneModel;
import jee.droneit.drone.service.DroneService;


import jee.droneit.type.model.TypeModel;
import jee.droneit.type.service.TypeService;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class DroneEditView implements Serializable
{

    private DroneService droneService;
    private TypeService typeService;


    @Getter
    @Setter
    private Long id;

    @Getter
    private DroneModel droneEditModel;

    @Getter
    private List<TypeModel> typeModels;

    public DroneEditView(){}

    @EJB
    public void setDroneService(DroneService droneService) {
        this.droneService = droneService;
    }

    @EJB
    public void setTypeService(TypeService typeService) {this.typeService = typeService;}

    /**
     * NO!
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached with it
     * field and initialized during init of the view.
     */
    //@PostConstruct //spooke (created bug - first init with id set to null)
    public void init() throws IOException
    {
        if(id == null) return;
        Optional<Drone> drone = droneService.find(id);
        if(drone.isPresent())
        {
            droneEditModel = DroneModel.entityToModelMapper().apply(drone.get());

        }
        else
        {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Drone not found");
        }
        typeModels = typeService.findAll().stream()
                .map(TypeModel.entityToModelMapper())
                .sorted(Comparator.comparingLong(TypeModel::getTypeID))
                .collect(Collectors.toList());


        for(int i =0; i< typeModels.size();i++)
        {
            TypeModel typeM = typeModels.get(i);
            if(typeM.getTypeID().equals(drone.get().getDroneType().getTypeID()))
            {
                typeModels.add(0,typeM);
                typeModels.remove(i+1);
                break;
            }
        }


    }

    public String updateAction() throws IOException
    {
        try
        {
            if ( !droneEditModel.getDroneID().equals(id)) throw new IllegalArgumentException("You cannot change Drone ID");
            Drone drone = DroneModel.modelToEntityUpdater().apply(droneService.find(droneEditModel.getDroneID()).orElseThrow(),droneEditModel );
            drone.setDroneType(typeService.find(droneEditModel.getDroneType().getTypeID()).orElseThrow());
            droneService.update(drone);

            String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
            return "/types/typeList.xhtml" + "&faces-redirect=true&includeViewParams=true";
        }
        catch(EJBException ex)
        {
            if (ex.getCause() instanceof OptimisticLockException)
            {
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String Responce = "Nastąpiła kolizja wersji \n"
                                + "Dron który próbowano zapisać = \n"
                                + "Dron ID - " + droneEditModel.getDroneID().toString() + "\n"
                                + "Licencja - " + droneEditModel.getLicenceNumber() + "\n"
                                + "Data Produkcji - " + droneEditModel.getProductionDate().format(formatter).toString() + "\n"
                                + "Typ - " + droneEditModel.getDroneType().getName() + "\n";

                //Reload from DB
                this.init();

                //Insert Response Bellow
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Responce));
            }
            return null;
        }
    }




}
