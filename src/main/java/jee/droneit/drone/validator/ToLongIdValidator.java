package jee.droneit.drone.validator;

import lombok.extern.java.Log;
import jee.droneit.drone.model.DroneModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.logging.Level;

@Log
public class ToLongIdValidator implements ConstraintValidator<ToLongId, Object>
{

    /**
     * Upper limit value for character basic stats values sum.
     */
    private int limit;

    @Override
    public void initialize(ToLongId constraintAnnotation)
    {
        ConstraintValidator.super.initialize(constraintAnnotation);
        limit = constraintAnnotation.limit();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context)
    {
        log.log(Level.INFO, "Bean Validation - ATM");
        if(value.getClass().isInstance(DroneModel.class))
        {
            return ((DroneModel)value).getDroneID() <= limit;
        }
        return false;
    }
}
