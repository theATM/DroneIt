package jee.droneit.configuration;


import jee.droneit.drone.entity.Drone;
import jee.droneit.drone.service.DroneService;
import jee.droneit.type.entity.Type;
import jee.droneit.type.service.TypeService;
import jee.droneit.user.entity.Sex;
import jee.droneit.user.entity.User;
import jee.droneit.user.entity.UserRoles;
import jee.droneit.user.service.UserService;
import lombok.SneakyThrows;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;


/**
 * Listener started automatically on CDI application context initialized. Injects proxy to the services and fills
 * database with default content. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
public class InitializedData  // implements ServletContextListener
{

    /**
     * Get Entity Manager (with setter)
     */
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    /**
     * Password hashing algorithm.
     */
    private Pbkdf2PasswordHash pbkdf;

    /**
     * Constrictors
     */
    @Inject
    public InitializedData(Pbkdf2PasswordHash pbkdf)
    {
        this.pbkdf = pbkdf;
    }
    public InitializedData() {}


    /**
     * Initializes database with some example values. Should be called after creating this object. This object should
     * be created only once.
     */
    @PostConstruct
    private synchronized void init()
    {
        //Create Data:
        //Default Users:

        User admin = User.builder()
                .login("admin")
                .email("admin@simplerpg.example.com")
                .pass(pbkdf.generate("adminadmin".toCharArray()))
                .sex(Sex.FEMALE)
                .roles(List.of(UserRoles.ADMIN, UserRoles.USER))
                .build();

        User adam = User.builder()
                .login("adam")
                .email("adam@simplerpg.example.com")
                .pass(pbkdf.generate("adam1234".toCharArray()))
                .sex(Sex.MALE)
                .roles(List.of(UserRoles.USER))
                .build();

        User ewa = User.builder()
                .login("ewa")
                .email("ewa@simplerpg.example.com")
                .pass(pbkdf.generate("ewa1234".toCharArray()))
                .sex(Sex.FEMALE)
                .roles(List.of(UserRoles.USER))
                .build();

        User stefan = User.builder()
                .login("stefan")
                .email("stefan@simplerpg.example.com")
                .pass(pbkdf.generate("stefan1234".toCharArray()))
                .sex(Sex.MALE)
                .roles(List.of(UserRoles.USER))
                .build();

        User alice = User.builder()
                .login("alice")
                .email("alice@example.com")
                .pass(pbkdf.generate("useruser".toCharArray()))
                .sex(Sex.FEMALE)
                .roles(List.of(UserRoles.USER))
                .build();

        entityManager.persist(admin);
        entityManager.persist(adam);
        entityManager.persist(ewa);
        entityManager.persist(stefan);
        entityManager.persist(alice);

        System.out.print("Users Initialized");

        Type waterDrone = Type.builder()
                        .typeID(7l)
                        .name("Wodny")
                        .brand("GADA")
                        .manufacturer("PG")
                        .build();

        Type airDrone = Type.builder()
                .typeID(1l)
                .name("Powietrzny")
                .brand("VQS")
                .manufacturer("PG")
                .build();


        Type militaryDrone = Type.builder()
                .typeID(2l)
                .name("Wojskowy")
                .brand("Bodo")
                .manufacturer("UW")
                .build();

        Type pictureDrone = Type.builder()
                .typeID(3l)
                .name("Fotograficzny")
                .brand("Bodo")
                .manufacturer("UW")
                .build();

        Type spaceDrone = Type.builder()
                .typeID(4l)
                .name("Kosmiczny")
                .brand("Bodo")
                .manufacturer("UW")
                .build();

        entityManager.persist(waterDrone);
        entityManager.persist(airDrone);
        entityManager.persist(militaryDrone);
        entityManager.persist(pictureDrone);
        entityManager.persist(spaceDrone);
        System.out.print("Types Initialized");


        Drone atmDrone = Drone.builder()
                .droneID(0l)
                .licenceNumber("RATS1YS79R")
                .productionDate(LocalDate.of(2021,2,12))
                .droneType(waterDrone)
                .ownUser(admin)
                .build();

        Drone tamDrone = Drone.builder()
                .droneID(1l)
                .licenceNumber("AMACAAQDPH")
                .productionDate(LocalDate.of(2020,12,5))
                .droneType(waterDrone)
                .ownUser(adam)
                .build();

        Drone babDrone = Drone.builder()
                .droneID(2l)
                .licenceNumber("6ZEA1A2E42")
                .productionDate(LocalDate.of(2019,7,24))
                .droneType(airDrone)
                .ownUser(adam)
                .build();

        Drone polDrone = Drone.builder()
                .droneID(3l)
                .licenceNumber("GYJSZEJYYB")
                .productionDate(LocalDate.of(2016,3,15))
                .droneType(airDrone)
                .ownUser(adam)
                .build();

        Drone valDrone = Drone.builder()
                .droneID(4l)
                .licenceNumber("OKWNF6R9OZ")
                .productionDate(LocalDate.of(2000,3,15))
                .droneType(airDrone)
                .ownUser(ewa)
                .build();

        Drone belDrone = Drone.builder()
                .droneID(5l)
                .licenceNumber("E4LZ4KYFL6")
                .productionDate(LocalDate.of(2016,6,15))
                .droneType(militaryDrone)
                .ownUser(adam)
                .build();

        Drone kasDrone = Drone.builder()
                .droneID(6l)
                .licenceNumber("DNGFUHYMUH")
                .productionDate(LocalDate.of(2006,3,1))
                .droneType(militaryDrone)
                .ownUser(stefan)
                .build();

        Drone dasDrone = Drone.builder()
                .droneID(7l)
                .licenceNumber("3S6EATMW88")
                .productionDate(LocalDate.of(2016,7,4))
                .droneType(pictureDrone)
                .ownUser(stefan)
                .build();

        Drone rasDrone = Drone.builder()
                .droneID(8l)
                .licenceNumber("QZ8PH5QMAR")
                .productionDate(LocalDate.of(2011,4,14))
                .droneType(spaceDrone)
                .ownUser(stefan)
                .build();

        Drone kosDrone = Drone.builder()
                .droneID(9l)
                .licenceNumber("00UOG8DPOF")
                .productionDate(LocalDate.of(2004,3,18))
                .droneType(spaceDrone)
                .ownUser(ewa)
                .build();

        entityManager.persist(atmDrone);
        entityManager.persist(tamDrone);
        entityManager.persist(babDrone);
        entityManager.persist(polDrone);
        entityManager.persist(valDrone);
        entityManager.persist(belDrone);
        entityManager.persist(kasDrone);
        entityManager.persist(dasDrone);
        entityManager.persist(rasDrone);
        entityManager.persist(kosDrone);
        System.out.print("Drones Initialized");



        waterDrone.setTypedDrones(List.of(atmDrone, tamDrone));
        airDrone.setTypedDrones(List.of(babDrone, polDrone, valDrone));
        militaryDrone.setTypedDrones(List.of(belDrone, kasDrone));
        pictureDrone.setTypedDrones(List.of(dasDrone));
        spaceDrone.setTypedDrones(List.of(rasDrone,kosDrone));

        System.out.print("Data Initialized");
    }


    @SneakyThrows
    private byte[] getResourceAsByteArray(String name)
    {
        try (InputStream is = this.getClass().getResourceAsStream(name))
        {
            return is.readAllBytes();
        }
    }
}
