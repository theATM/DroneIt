package jee.droneit.user.service;



import jee.droneit.user.entity.User;
import jee.droneit.user.entity.UserRoles;
import jee.droneit.user.repository.UserRepository;

import lombok.NoArgsConstructor;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding user entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor
@RolesAllowed(UserRoles.USER)
public class UserService
{
    /**
     * Repository for user entity.
     */
    private UserRepository userRepository;

    /**
     * Build in security context.
     */
    private SecurityContext securityContext;

    /**
     * Password hashing algorithm.
     */
    private Pbkdf2PasswordHash pbkdf;


    /**
     * UserRepository Constructor
     * @param userRepository this is repository for user entity
     */
    @Inject
    public UserService(UserRepository userRepository,  SecurityContext securityContext, Pbkdf2PasswordHash pbkdf)
    {
        this.userRepository = userRepository;
        this.securityContext = securityContext;
        this.pbkdf = pbkdf;
    }

    /**
     *
     * @param login this is key differentiating users
     * @return container with user or empty container
     */
    public Optional<User> find(String login)
    {
        return userRepository.find(login);
    }

    /**
     * Seeks for single user using login and password. Can be use in authentication module.
     *
     * @param login    user's login
     * @param password user's password (hash)
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login, String password)
    {
        return userRepository.findByLoginAndPassword(login, password);
    }

    /**
     * @return logged user entity
     */
    public Optional<User> findCurrentUser()
    {
        if (securityContext.getCallerPrincipal() != null)
        {
            return find(securityContext.getCallerPrincipal().getName());
        }
        else
        {
            return Optional.empty();
        }
    }


    /**
     * @return all available users
     */
    public List<User> findAll()
    {
        return userRepository.findAll();
    }

    /**
     * Creates new user
     * Stores new user in the storage. Everyone can call it. If caller principal is not admin roles narrowed to
     * {@link UserRoles#USER}.
     *
     * @param user new user
     */
    @PermitAll
    public void create(User user)
    {
        //Set up right role //TODO
        if (!securityContext.isCallerInRole(UserRoles.ADMIN))
        {
            user.setRoles(List.of(UserRoles.USER));
        }
        user.setPass(pbkdf.generate(user.getPass().toCharArray()));
        userRepository.create(user);
    }

    /**
     * @param user user to be removed
     */
    @RolesAllowed(UserRoles.ADMIN)
    public void delete(User user) {
        userRepository.delete(user);
    }

    /*

    public byte[] findAvatar(User  user) throws IOException
    {
        String avatarPathStr = user.getAvatar();
        if ((avatarPathStr == null )|| (avatarPathStr == "")) return null;
        Path avatarPath = java.nio.file.Paths.get(avatarPathStr);
        if (avatarPath != null && Files.exists(avatarPath))
        {
            return Files.readAllBytes(avatarPath);
        }
        return null;
    }

    public void addAvatar(User user, InputStream avatar, String avatarPath) throws IOException
    {
        String oldAvatarPathStr = user.getAvatar();
        if (oldAvatarPathStr != null && oldAvatarPathStr != "")
        {
            Path oldAvatarPath = java.nio.file.Paths.get(oldAvatarPathStr);
            if (oldAvatarPath != null && Files.exists(oldAvatarPath)) {
                Files.delete(oldAvatarPath);
            }
        }

        Path targetFile = java.nio.file.Paths.get(avatarPath);
        Files.copy(avatar, targetFile);
        user.setAvatar(avatarPath);
        userRepository.update(user);
    }


    public Boolean deleteAvatar(User user) throws IOException
    {
        String avatarPathStr = user.getAvatar();
        if ((avatarPathStr == null )|| (avatarPathStr == "")) return false;
        Path oldAvatarPath = java.nio.file.Paths.get(avatarPathStr);
        user.setAvatar(null);
        userRepository.update(user);
        if (oldAvatarPath != null && Files.exists(oldAvatarPath))
        {
            Files.delete(oldAvatarPath);
        }
        else return false;

        return true;
    }

    */


}
