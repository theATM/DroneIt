package jee.droneit.type.repository;

import jee.droneit.drone.entity.Drone;
import jee.droneit.drone.entity.Drone_;
import jee.droneit.repository.Repository;
import jee.droneit.type.entity.Type;
import jee.droneit.type.entity.Type_;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class TypeRepository implements Repository<Type, Long>
{
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }


    @Override
    public Optional<Type> find(Long typeID)
    {
        return Optional.ofNullable(this.entityManager.find(Type.class, typeID));
    }


    public Optional<Type> findByName(String typeName)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Type> query = cb.createQuery(Type.class);
        Root<Type> root = query.from(Type.class);

        query.select(root).where( cb.equal( root.get(Type_.NAME), typeName) );

        try
        {
            return Optional.of(this.entityManager.createQuery(query).getSingleResult());
        }
        catch(NoResultException ex)
        {
            return Optional.empty();
        }
        catch (NonUniqueResultException ex)
        {
            return Optional.of(this.entityManager.createQuery(query).getResultList().get(0));
        }
    }

    @Override
    public List<Type> findAll()
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Type> query = cb.createQuery(Type.class);
        Root<Type> root = query.from(Type.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();


        //return this.entityManager
        //        .createQuery("select t from Type t", Type.class)
        //            .getResultList();
    }

    @Override
    public void create(Type type)
    {
        this.entityManager.persist(type);
    }

    @Override
    public void delete(Type type)
    {
        this.entityManager
                .remove(this.entityManager
                        .find(Type.class, type.getTypeID())
                );
    }
    @Override
    public void update(Type type)
    {
        this.entityManager.merge(type);
    }

    @Override
    public void detach(Type type)
    {
        this.entityManager.detach(type);
    }

}
