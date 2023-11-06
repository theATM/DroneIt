package jee.droneit.drone.service;


import jee.droneit.config.interceptor.binding.LogBinding;
import jee.droneit.drone.entity.Drone;
import jee.droneit.drone.repository.DroneRepository;
import jee.droneit.type.entity.Type;
import jee.droneit.type.repository.TypeRepository;
import jee.droneit.user.entity.User;
import jee.droneit.user.entity.UserRoles;
import jee.droneit.user.repository.UserRepository;
import lombok.NoArgsConstructor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
@NoArgsConstructor
@RolesAllowed(UserRoles.USER)
public class DroneService
{

    private DroneRepository droneRepository;
    private TypeRepository typeRepository;
    private UserRepository userRepository;

   private SecurityContext securityContext;


    @Inject
    public DroneService(DroneRepository droneRepository,TypeRepository typeRepository,UserRepository userRepository, SecurityContext securityContext)
    {
        this.droneRepository = droneRepository;
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    public Optional<Drone> find(Long droneID)
    {
        Optional<User> user = this.userRepository.find(this.securityContext.getCallerPrincipal().getName());
        if (user.isEmpty())
        {
            return Optional.empty();
        }

        if (this.securityContext.isCallerInRole(UserRoles.ADMIN))
        {
            return this.droneRepository.find(droneID);
        }
        else
        {
            return this.droneRepository.findWithUser(droneID, user.get());
        }

    }



    public Optional<Drone> findWithType(Long droneID,Long typeID)
    {
        Optional<User> user = this.userRepository.find(this.securityContext.getCallerPrincipal().getName());
        if (user.isEmpty())
        {
            return Optional.empty();
        }

        if(this.securityContext.isCallerInRole(UserRoles.ADMIN))
        {

            Optional<Type> type = this.typeRepository.find(typeID);
            if (type.isEmpty()) {
                return Optional.empty();
            } else {
                return this.droneRepository.findWithType(droneID, type.get());
            }
        }
        else
        {
            Optional<Type> type = this.typeRepository.find(typeID);
            if (type.isEmpty()) {
                return Optional.empty();
            } else {
                return this.droneRepository.findWithTypeWithUser(droneID, type.get(),user.get());
            }
        }

    }

    public Optional<Drone> findWithUser(Long droneID,String login)
    {
        Optional<User> user = this.userRepository.find(login);
        if(user.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            return this.droneRepository.findWithUser(droneID, user.get());
        }
    }

    public  List<Drone> findResembling(Long ID, String licence, LocalDate prodDate, String typeName)
    {
        Optional<User> user = this.userRepository.find(this.securityContext.getCallerPrincipal().getName());
        if (user.isEmpty())
        {
            return Collections.emptyList();
        }

        Optional<Type> type = Optional.empty();
        if(typeName!= null && !typeName.equals(""))
        {
            type = this.typeRepository.findByName(typeName);
        }


        if (this.securityContext.isCallerInRole(UserRoles.ADMIN))
        {
            return this.droneRepository.findResembling( ID, licence, prodDate, Optional.empty(), type);
        }

        return this.droneRepository.findResembling(ID, licence, prodDate, user,  type);


    }




    public List<Drone> findAll()
    {
        Optional<User> user = this.userRepository.find(this.securityContext.getCallerPrincipal().getName());
        if (user.isEmpty())
        {
            return Collections.emptyList();
        }

        if(this.securityContext.isCallerInRole(UserRoles.ADMIN))
        {
            return this.droneRepository.findAll();
        }
        else
        {

            return this.droneRepository.findAllWithUser(user.get());
        }

    }

    public List<Drone> findAllWithType(Long typeID)
    {
        Optional<User> user = this.userRepository.find(this.securityContext.getCallerPrincipal().getName());
        if (user.isEmpty())
        {
            return Collections.emptyList();
        }

        if(this.securityContext.isCallerInRole(UserRoles.ADMIN))
        {
            Optional<Type> type = this.typeRepository.find(typeID);
            if (type.isEmpty())
            {
                return Collections.emptyList();
            }
            else
            {
                return this.droneRepository.findAllWithType(type.get());
            }
        }
        else
        {
            Optional<Type> type = this.typeRepository.find(typeID);
            if (type.isEmpty())
            {
                return Collections.emptyList();
            }
            else
            {
                return this.droneRepository.findAllWithTypeWithUser(type.get(),user.get());
            }
        }


    }

    public List<Drone> findAllWithUser(String login)
    {
        Optional<User> user = this.userRepository.find(login);
        if (user.isEmpty())
        {
            return Collections.emptyList();
        }
        else
        {
            return this.droneRepository.findAllWithUser(user.get());
        }
    }

    @LogBinding
    public void create(Drone drone)
    {
        this.droneRepository.create(drone);
        this.typeRepository.find(drone.getDroneType().getTypeID()).ifPresent(type -> type.getTypedDrones().add(drone));
        this.userRepository.find(drone.getOwnUser().getLogin()).ifPresent(user -> user.getOwnedDrones().add(drone));
    }

    @LogBinding
    public void createForCurrentUser(Drone drone)
    {
        User user =  this.userRepository.find( this.securityContext.getCallerPrincipal().getName()).orElseThrow();
        drone.setOwnUser(user);
        user.getOwnedDrones().add(drone);
        this.droneRepository.create(drone);
    }

    @LogBinding
    public void update(Drone drone)
    {
        Optional<User> user = this.userRepository.find(this.securityContext.getCallerPrincipal().getName());
        if (user.isEmpty())
        {
            return;
        }

        if(this.securityContext.isCallerInRole(UserRoles.ADMIN))
        {
            Drone original = droneRepository.find(drone.getDroneID()).orElseThrow();
            droneRepository.detach(original);
            if (!original.getDroneType().getTypeID().equals(drone.getDroneType().getTypeID()))
            { //not Equal
                original.getDroneType().getTypedDrones().removeIf(droneToRemove -> droneToRemove.getDroneID().equals(drone.getDroneID()));
                typeRepository.find(drone.getDroneType().getTypeID()).ifPresent(type -> type.getTypedDrones().add(drone));
            }
            this.droneRepository.update(drone);
        }
        else
        {
            Drone original = droneRepository.find(drone.getDroneID()).orElseThrow();
            droneRepository.detach(original);
            if (!original.getOwnUser().getLogin().equals(user.get().getLogin()))
            {
                return;//Not my user
            }
            if (!original.getDroneType().getTypeID().equals(drone.getDroneType().getTypeID()))
            { //not Equal
                original.getDroneType().getTypedDrones().removeIf(droneToRemove -> droneToRemove.getDroneID().equals(drone.getDroneID()));
                typeRepository.find(drone.getDroneType().getTypeID()).ifPresent(type -> type.getTypedDrones().add(drone));
            }
            this.droneRepository.update(drone);
        }

    }

    @LogBinding
    @RolesAllowed(UserRoles.ADMIN)
    public void delete(Long droneID)
    {
        Optional<User> user = this.userRepository.find(this.securityContext.getCallerPrincipal().getName());
        if (user.isEmpty())
        {
            return;
        }


        Drone drone = droneRepository.find(droneID).orElseThrow();

        if (drone.getOwnUser().getLogin().equals(drone.getOwnUser().getLogin())  || this.securityContext.isCallerInRole(UserRoles.ADMIN))
        {
            drone.getDroneType().getTypedDrones().remove(drone);
            drone.getOwnUser().getOwnedDrones().remove(drone);
            this.droneRepository.delete(this.droneRepository.find(droneID).orElseThrow());
        }


    }

}
