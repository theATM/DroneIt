package jee.droneit.drone.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Binding annotation for BeansValidation custom validator. Checks if sum of character's statistics (strength,
 * constitution and charisma) is equal or lower than 54.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ToLongIdValidator.class)
@Documented
public @interface ToLongId
{

    /**
     * @return error message
     */
    String message() default "ID to large!!!";

    /**
     * @return validation groups
     */
    Class<?>[] groups() default {};

    /**
     * @return additional payload for programmer, not used by BeansValidation
     */
    Class<? extends Payload>[] payload() default {};


    int limit() default 1000;

}
