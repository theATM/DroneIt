package jee.droneit.type.service;


import jee.droneit.type.entity.Type;
import jee.droneit.type.repository.TypeRepository;
import jee.droneit.user.entity.UserRoles;
import lombok.NoArgsConstructor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


/**
 * Service layer for all business actions regarding type entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor
@RolesAllowed(UserRoles.USER)
public class TypeService
{

    private TypeRepository typeRepository;

    @Inject
    public TypeService(TypeRepository typeRepository)
    {
        this.typeRepository = typeRepository;
    }

    public Optional<Type> find(Long typeID) {return this.typeRepository.find(typeID);}

    public List<Type> findAll() {return this.typeRepository.findAll();}

    @RolesAllowed(UserRoles.ADMIN)
    public void create(Type type) {this.typeRepository.create(type);}

    @RolesAllowed(UserRoles.ADMIN)
    public void update(Type type) {this.typeRepository.update(type);}

    @RolesAllowed(UserRoles.ADMIN)
    public void delete(Long typeID)
    {
        this.typeRepository.delete(this.typeRepository.find(typeID).orElseThrow());
    }

}
