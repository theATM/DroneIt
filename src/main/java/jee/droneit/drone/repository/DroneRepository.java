package jee.droneit.drone.repository;


import jee.droneit.drone.entity.Drone;
import jee.droneit.drone.entity.Drone_;
import jee.droneit.repository.Repository;
import jee.droneit.serialisation.CloningUtility;
import jee.droneit.type.entity.Type;
import jee.droneit.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Max;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
public class DroneRepository implements Repository<Drone, Long>
{
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager EntityManager) {
        this.entityManager = EntityManager;
    }


    @Override
    public Optional<Drone> find(Long droneID)
    {
        return Optional.ofNullable(this.entityManager.find(Drone.class, droneID));
    }

    public Optional<Drone> findWithType(Long droneID, Type type)
    {
        try
        {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Drone> query = cb.createQuery(Drone.class);
            Root<Drone> root = query.from(Drone.class);

            query.select(root).where(
                    cb.and(
                            cb.equal( root.get(Drone_.droneID), droneID),
                            cb.equal( root.get(Drone_.droneType), type)
                    )
            );

            return Optional.of(entityManager.createQuery(query).getSingleResult());
            //return Optional.of(
            //            this.entityManager
            //            .createQuery(
            //                    "select d from Drone d where d.droneID = :droneID and d.droneType = :type",
            //                    Drone.class)
            //                .setParameter("droneID",droneID)
            //                .setParameter("type",type)
            //                .getSingleResult()
            //        );
        }
        catch(NoResultException ex)
        {
            return Optional.empty();
        }
    }

    public Optional<Drone> findWithUser(Long droneID, User user)
    {
        try
        {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Drone> query = cb.createQuery(Drone.class);
            Root<Drone> root = query.from(Drone.class);

            query.select(root).where(
                    cb.and(
                            cb.equal( root.get(Drone_.droneID), droneID),
                            cb.equal( root.get(Drone_.ownUser), user)
                    )
            );

            return Optional.of(entityManager.createQuery(query).getSingleResult());



           // return Optional.of(
           //         this.entityManager
           //                 .createQuery(
           //                         "select d from Drone d where d.droneID = :droneID and d.ownUser = :user",
           //                         Drone.class)
           //                 .setParameter("droneID",droneID)
           //                 .setParameter("user",user)
           //                 .getSingleResult()
           // );
        }
        catch(NoResultException ex)
        {
            return Optional.empty();
        }
    }

    public Optional<Drone> findWithTypeWithUser(Long droneID, Type type, User user)
    {
        try
        {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Drone> query = cb.createQuery(Drone.class);
            Root<Drone> root = query.from(Drone.class);

            query.select(root).where(cb.and(
                    cb.equal( root.get(Drone_.droneID), droneID),
                    cb.equal( root.get(Drone_.ownUser), user),
                    cb.equal( root.get(Drone_.droneType), type)
            ));

            return Optional.of(entityManager.createQuery(query).getSingleResult());


           // return Optional.of(
           //         this.entityManager
           //                 .createQuery(
           //                         "select d from Drone d where d.droneID = :droneID and d.droneType = :type and d.ownUser = :user",
           //                         Drone.class)
           //                 .setParameter("droneID",droneID)
           //                 .setParameter("type",type)
           //                 .setParameter("user",user)
           //                 .getSingleResult()
           // );
        }
        catch(NoResultException ex)
        {
            return Optional.empty();
        }
    }

    public  List<Drone>  findResembling(Long ID, String licence, LocalDate prodDate, Optional<User> user, Optional<Type> type)
    {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Drone> query = cb.createQuery(Drone.class);
            Root<Drone> root = query.from(Drone.class);

            List<Predicate> predicates = new ArrayList<Predicate>();
            if(ID != null)
            {
                Predicate idSearch = cb.equal( root.get(Drone_.droneID), ID);
                predicates.add(idSearch);
            }
            if(!licence.equals(null) && !licence.equals(""))//Error???
            {
                Predicate licenceSearch = cb.equal( root.get(Drone_.licenceNumber), licence);
                predicates.add(licenceSearch);
            }
            if(prodDate != null)
            {
                Predicate prodDateSearch = cb.equal( root.get(Drone_.productionDate), prodDate);
                predicates.add(prodDateSearch);
            }
            if(!user.isEmpty())
            {
                Predicate loginSearch = cb.equal( root.get(Drone_.ownUser), user.get());
                predicates.add(loginSearch);
            }
            if(!type.isEmpty())
            {
                Predicate typeSearch = cb.equal( root.get(Drone_.droneType), type.get());
                predicates.add(typeSearch);
            }

           // Predicate idSearch = cb.equal( root.get(Drone_.ownUser), user)

            query.select(root).where(cb.and(predicates.toArray(new Predicate[] { })));
                  //  cb.and(

                   // cb.equal( root.get(Drone_.ownUser), user),
                    //cb.equal( root.get(Drone_.droneType), type)
            //)


         List<Drone> rlist = entityManager.createQuery(query).getResultList();
         return rlist;

    }


    @Override
    public List<Drone> findAll()
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Drone> query = cb.createQuery(Drone.class);
        Root<Drone> root = query.from(Drone.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();

        //return this.entityManager.createQuery(
        //        "select d from Drone d", Drone.class).getResultList();

    }

    public List<Drone> findAllWithType(Type type)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Drone> query = cb.createQuery(Drone.class);
        Root<Drone> root = query.from(Drone.class);

        query.select(root).where( cb.equal( root.get(Drone_.droneType), type) );

        return this.entityManager.createQuery(query).getResultList();


        //return this.entityManager
        //        .createQuery(
        //    "select d from Drone d where d.droneType = :type",
        //            Drone.class)
        //                .setParameter("type",type)
        //    .getResultList();
    }

    public List<Drone> findAllWithUser(User user)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Drone> query = cb.createQuery(Drone.class);
        Root<Drone> root = query.from(Drone.class);

        query.select(root).where( cb.equal( root.get(Drone_.ownUser), user) );

        return this.entityManager.createQuery(query).getResultList();

        // return this.entityManager
        //        .createQuery(
        //                "select d from Drone d where d.ownUser = :user",
        //                Drone.class)
        //        .setParameter("user",user)
        //        .getResultList();
    }

    public List<Drone> findAllWithTypeWithUser(Type type, User user)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Drone> query = cb.createQuery(Drone.class);
        Root<Drone> root = query.from(Drone.class);

        query.select(root).where(cb.and(
                cb.equal( root.get(Drone_.ownUser), user),
                cb.equal( root.get(Drone_.droneType), type)
        ));

        return this.entityManager.createQuery(query).getResultList();

       // return this.entityManager
       //         .createQuery(
       //                 "select d from Drone d where d.ownUser = :user and d.droneType = :type",
       //                 Drone.class)
       //         .setParameter("type",type)
       //         .setParameter("user",user)
       //         .getResultList();
    }

    @Override
    public void create(Drone drone)
    {
        this.entityManager.persist(drone);
    }

    @Override
    public void delete(Drone drone)
    {
        this.entityManager.remove(this.entityManager.find(Drone.class, drone.getDroneID()));

    }

    @Override
    public void update(Drone drone)
    {
        this.entityManager.merge(drone);
    }

    @Override
    public void detach(Drone drone)
    {
        this.entityManager.detach(drone);
    }


}
