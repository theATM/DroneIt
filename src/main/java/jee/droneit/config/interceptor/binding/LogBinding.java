package jee.droneit.config.interceptor.binding;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;


@InterceptorBinding
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LogBinding
{

}
