/*
package jee.droneit.type.servlet;

import jee.droneit.servlet.ServletUtility;
import jee.droneit.type.entity.Type;
import jee.droneit.type.model.TypeModel;
import jee.droneit.type.model.TypesModel;
import jee.droneit.type.service.TypeService;
import jee.droneit.user.dto.UserDto;
import jee.droneit.user.dto.UsersDto;
import jee.droneit.user.entity.User;
import jee.droneit.user.service.UserService;
import jee.droneit.user.servlet.UserServlet;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//For Testing purposes
@WebServlet(urlPatterns = TypeServlet.Paths.TYPES + "/*")
public class TypeServlet extends HttpServlet
{

    private final TypeService typeService;
    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    public TypeServlet(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public static class Paths
    {
        */
/**
         * Get all DroneIt users
         *//*

        public static final String TYPES = "/api/types";

    }

    public static class Patterns
    {
        */
/**
         * All DroneIt users
         *//*

        public static final String ALL_TYPES = "^/?$";

        */
/**
         * Specified DroneIt user
         *//*

        public static final String SINGLE_TYPE = "^/[A-Za-z0-9]+/?$";
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String path = ServletUtility.parseRequestPath(request);
        if (path.matches(TypeServlet.Patterns.SINGLE_TYPE))
        {
            getSingleType(request, response);
            return;
        }
        else if (path.matches(TypeServlet.Patterns.ALL_TYPES))
        {
            getAllTypes(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);

    }

    private void getAllTypes(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        response.getWriter()
                .write(jsonb.toJson(TypesModel.entityToModelMapper()
                        .apply(typeService.findAll())));

    }

    private void getSingleType(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        //Parsed request path is valid with character pattern and can contain starting and ending '/'.
        Long typeID = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));

        Optional<Type> type = typeService.find(typeID);

        if (type.isPresent())
        {
            response.getWriter()
                    .write(jsonb.toJson(TypeModel.entityToModelMapper().apply(type.get())));
        }
        else
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
*/
