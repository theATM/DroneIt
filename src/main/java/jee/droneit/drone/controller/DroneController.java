package jee.droneit.drone.controller;


import jee.droneit.drone.dto.DroneDto;
import jee.droneit.drone.dto.DronesDto;
import jee.droneit.drone.entity.Drone;
import jee.droneit.drone.service.DroneService;
import jee.droneit.type.entity.Type;
import jee.droneit.type.service.TypeService;
import jee.droneit.user.entity.User;
import jee.droneit.user.entity.UserRoles;
import jee.droneit.user.service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Optional;

@Path("")
@RolesAllowed(UserRoles.USER)
public class DroneController
{

    private DroneService droneService;
    private TypeService typeService;
    private UserService userService;

    public DroneController(){}

    @EJB
    public void setService(DroneService droneService)
    {
        this.droneService = droneService;
    }

    @EJB
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    @EJB
    public void setUserService(UserService userService){this.userService = userService;}



    @GET
    @Path("/drones")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDrones()
    {
        return Response.ok(DronesDto.entityToDtoMapper().apply((this.droneService.findAll()))).build();
    }

    @GET
    @Path("/types/{typeID}/drones")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrones(@PathParam("typeID") Long typeID)
    {
        Optional <Type> type = this.typeService.find(typeID);
        if(type.isEmpty())
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(DronesDto.entityToDtoMapper().apply((this.droneService.findAllWithType(typeID)))).build();
    }



    @GET
    @Path("/types/{typeID}/drones/{droneID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrone(@PathParam("typeID") Long typeID, @PathParam("droneID") Long droneID)
    {
        Optional <Type> type = this.typeService.find(typeID);
        if(type.isEmpty())
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Drone> foundDrone = this.droneService.findWithType(droneID,typeID);
        if(foundDrone.isPresent())
        {
            return Response.ok(DroneDto.entityToDtoMapper().apply(foundDrone.get())).build();
        }
        else
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Path("/types/{typeID}/drones")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postDrone(@PathParam("typeID") Long typeID, DroneDto ctRequest)
    {
        Optional<User> currentUser = this.userService.findCurrentUser();
        if(currentUser.isEmpty())
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        Optional <Type> type = this.typeService.find(typeID);
        if(type.isEmpty())
        {
            //Gdy chcę dodać element do listy co nie istnieje
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Drone newDrone = DroneDto.dtoToEntityMapper(type.get(),currentUser.get()).apply(ctRequest);

        Optional<Drone> existingDrone = this.droneService.findWithType(newDrone.getDroneID(),typeID);

        if(existingDrone.isEmpty())
        {
            this.droneService.create(newDrone);
            //Gdy dodałem element poprawnie
            return Response.created(UriBuilder.fromMethod(DroneController.class, "getDrone")
                    .build(typeID, newDrone.getDroneID())).build();
        }
        else
        { //When cannot create
            //Gdy gdy nie mogę stworzyć elementu
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


    }



    @PUT
    @Path("/types/{typeID}/drones/{droneID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putDrone(@PathParam("typeID") Long typeID, @PathParam("droneID") Long droneID, DroneDto utRequest)
    {
        Optional <Type> type = this.typeService.find(typeID);
        if(type.isEmpty())
        {
            //Gdy chcę dodać element do listy co nie istnieje
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Drone> originalDrone = this.droneService.findWithType(droneID,typeID);
        if(originalDrone.isPresent())
        { //Update
            //Element istnieje
            DroneDto.dtoToEntityUpdater().apply(originalDrone.get(), utRequest);
            this.droneService.update(originalDrone.get());
            //Gdy edytowałem pomyślnie element
            return Response.ok().build();
        }
        else //Element does not exist
        {
            //Element nie istnieje
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("/types/{typeID}/drones/{droneID}")
    public Response deleteDrone(@PathParam("typeID") Long typeID, @PathParam("droneID") Long droneID)
    {
        Optional <Type> type = this.typeService.find(typeID);
        if(type.isEmpty())
        {
            //return Response.status(Response.Status.BAD_REQUEST).build();
            //Gdy chcę usunąć element z listy co nie istnieje
            return Response.ok().build();
        }

        Optional<Drone> todeleteDrone = this.droneService.findWithType(droneID,typeID);
        if(todeleteDrone.isPresent())
        {
            this.droneService.delete(droneID);
            //Gdy usuwam elemennt pomyślnie
            return Response.ok().build();
        }
        else
        { //Not Found
            //gdy element co chiałem usunąć nie istnieje
            return Response.ok().build();
        }
    }


}
