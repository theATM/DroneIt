package jee.droneit.user.dto;


import jee.droneit.type.dto.TypeDto;
import jee.droneit.type.entity.Type;
import jee.droneit.user.entity.User;
import jee.droneit.user.entity.Sex;
import lombok.*;

import java.util.function.Function;

/**
 * GET user response. It contains all field that can be presented (but not necessarily changed) to the used. How
 * user is described is defined in {@link jee.droneit.user.entity.User} classe.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UserDto
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

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<User, UserDto> entityToDtoMapper()
    {
        return user -> UserDto.builder()
                .login(user.getLogin())
                .email(user.getEmail())
                .pass(user.getPass())
                //.sex(user.getSex())
                .build();
    }

    /**
     * @return mapper for convenient converting dto object to entity object
     */
    public static Function<UserDto, User> dtoToEntityMapper()
    {
        return request -> User.builder()
                .login(request.getLogin())
                .pass(request.getPass())
                .email(request.getEmail())
                .build();
    }

}
