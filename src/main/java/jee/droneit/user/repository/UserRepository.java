package jee.droneit.user.repository;

import jee.droneit.repository.Repository;
import jee.droneit.type.entity.Type;
import jee.droneit.user.entity.User;
import jee.droneit.user.entity.User_;
import lombok.extern.java.Log;

import javax.enterprise.context.Dependent;
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

//TODO:
@Log
@Dependent
public class UserRepository implements Repository<User, String>
{
    /**
     * Underlying data store. In future should be replaced with database connection.
     */

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }


    @Override
    public Optional<User> find(String login)
    {
        //log.info(String.format("EntityManager for %s %s find", this.getClass(), this.entityManager));
        return Optional.ofNullable(this.entityManager.find(User.class, login));
    }

    @Override
    public List<User> findAll()
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();

        //return this.entityManager
        //        .createQuery("select u from User u", User.class)
        //            .getResultList();
    }

    @Override
    public void create(User userEntity)
    {
        //log.info(String.format("EntityManager for %s %s create", this.getClass(), this.entityManager));
        this.entityManager.persist(userEntity);
    }

    @Override
    public void delete(User userEntity)
    {
        this.entityManager.remove(this.entityManager.find(User.class, userEntity.getLogin()));
    }

    @Override
    public void update(User userEntity)
    {
        this.entityManager.merge(userEntity);
    }

    @Override
    public void detach(User userEntity)
    {
        this.entityManager.detach(userEntity);
    }


    public Optional<User> findByLoginAndPassword(String login, String pass)
    {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(
                cb.and(
                        cb.equal( root.get(User_.login),login),
                        cb.equal( root.get(User_.pass),pass)
                )
        );


        try
        {
            return Optional.of(entityManager.createQuery(query).getSingleResult());
           // return Optional.of(this.entityManager
           //         .createQuery("select u from User u where u.login = :login and u.pass = :pass", User.class)
           //         .setParameter("login", login)
           //         .setParameter("pass", pass)
           //         .getSingleResult());
        }
        catch (NoResultException ex)
        {
            return Optional.empty();
        }
        catch(NonUniqueResultException ex)
        {
            return Optional.empty();
        }
    }


}
