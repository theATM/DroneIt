package jee.droneit.user.controller;


import jee.droneit.user.dto.UserDto;
import jee.droneit.user.dto.UsersDto;
import jee.droneit.user.entity.User;
import jee.droneit.user.entity.UserRoles;
import jee.droneit.user.service.UserService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Optional;

/**
 * REST controller for {@link jee.droneit.user.entity.User} entity.
 */
@Path("")
@RolesAllowed(UserRoles.USER)
public class UserController
{
    /**
     * Service for managing users.
     */
    private UserService userService;

    /**
     * JAX-RS requires no-args constructor.
     */
    public UserController() {}

    /**
     * @param userService service for managing users
     */
    @EJB
    public void setService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return response with available users
     */
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers()
    {
        return Response
                .ok(UsersDto.entityToDtoMapper().apply(userService.findAll()))
                .build();
    }

    /**
     * Get one user info
     * @param login user's username
     * @return response with selected user
     */
    @GET
    @Path("/users/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("login") String login)
    {
        Optional<User> user = userService.find(login);
        if (user.isPresent())
        {
            return Response
                    .ok(UserDto.entityToDtoMapper().apply(user.get()))
                    .build();
        }
        else
        {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    /**
     * @return response with logged user or empty object
     */
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser()
    {
        Optional<User> user = userService.findCurrentUser();
        if (user.isPresent())
        {
            return Response
                    .ok(UserDto.entityToDtoMapper().apply(user.get()))
                    .build();
        }
        else
        {
            return Response
                    .ok(new Object())
                    .build();
        }
    }

    /**
     * Create new user
     * @param request  - new user info
     * @return - Response (if successful)
     */
    @Path("/users")
    @POST
    @PermitAll
    public Response createUser(UserDto request)
    {
        userService.create(UserDto.dtoToEntityMapper().apply(request));
        return Response.created(UriBuilder.fromPath("/users/{login}")
                .build(request.getLogin())).build();
    }

}
