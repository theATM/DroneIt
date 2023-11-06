package jee.droneit.user.dto;


import jee.droneit.user.entity.Sex;
import jee.droneit.user.entity.User;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * GET users response. Contains list of available users.
 */
@Getter
@Setter
@Builder(builderClassName="UsersDtoBuilder")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UsersDto
{
    /**
     * Represents user character in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class InnerUser
    {

        /**
         * User's login
         */
        private String login;

        /**
         * User's email
         */
        private String email;

        /**
         * User's pass
         */
        private String pass;

        /**
         * User's sex (gender)
         */
        //private Sex sex;
    }

    /**
     * Name of the selected users.
     */
    @Singular("innerUserElem")
    private List<InnerUser> innerUserList;


    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<User>, UsersDto> entityToDtoMapper()
    {
        return users ->
        {
            UsersDto.UsersDtoBuilder response = UsersDto.builder();
            users.stream()
                    .map(user -> UsersDto.InnerUser.builder()
                            .login(user.getLogin())
                            .email(user.getEmail())
                            .pass(user.getPass())
                            //.sex(user.getSex())
                            .build()
                    ).forEach(response::innerUserElem);
            return response.build();
        };
    }

}
