package jee.droneit.type.controller;

import jee.droneit.type.dto.TypeDto;
import jee.droneit.type.dto.TypesDto;
import jee.droneit.type.entity.Type;
import jee.droneit.type.service.TypeService;
import jee.droneit.user.entity.UserRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("")
@RolesAllowed(UserRoles.USER)
public class TypeController
{
    private TypeService typeService;
    public TypeController(){};

    @EJB
    public void setService(TypeService typeService) {
        this.typeService = typeService;
    }

    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypes()
    {
        return Response.ok(TypesDto.entityToDtoMapper().apply((this.typeService.findAll()))).build();
    }

    @GET
    @Path("/types/{typeID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType(@PathParam("typeID") Long typeID)
    {
        Optional<Type> foundType = this.typeService.find(typeID);
        if(foundType.isPresent())
        {
            return Response.ok(TypeDto.entityToDtoMapper().apply(foundType.get())).build();
        }
        else
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/types")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response postType(TypeDto ctRequest)
    {
        Type newType = TypeDto.dtoToEntityMapper().apply(ctRequest);
        Optional<Type> existingType =  this.typeService.find(newType.getTypeID());
        if( existingType.isEmpty() )
        {
            this.typeService.create(newType);
            return Response.created(UriBuilder.fromMethod(TypeController.class, "getType")
                    .build(newType.getTypeID())).build();
        }
        else
        { //When cannot create
            return Response.status(Response.Status.BAD_REQUEST).build();
        }



    }

    @PUT
    @Path("/types/{typeID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response putType(@PathParam("typeID") Long typeID, TypeDto utRequest)
    {
        Optional<Type> originalType = this.typeService.find(typeID);
        if(originalType.isPresent())
        { //Update
            TypeDto.dtoToEntityBiMapper().apply(originalType.get(),utRequest);
            this.typeService.update(originalType.get());
            return Response.ok().build();
        }
        else //Not Found
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/types/{typeID}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response deleteType(@PathParam("typeID") Long typeID)
    {
        Optional<Type> todeleteType = this.typeService.find(typeID);
        if(todeleteType.isPresent())
        {
            this.typeService.delete(typeID);
            return Response.ok().build();
        }
        else
        { //Not Found
            return Response.ok().build();
        }
    }

}
