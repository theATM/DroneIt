package jee.droneit.config.interceptor;

import jee.droneit.config.interceptor.binding.LogBinding;
import jee.droneit.drone.entity.Drone;

import javax.annotation.Priority;
import javax.ejb.EJBAccessException;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.core.Response;


@Interceptor
@LogBinding
@Priority(10)
public class LogInterceptor
{
    private SecurityContext securityContext;

    @Inject
    public void setSecurityContext(SecurityContext securityContext) {this.securityContext = securityContext;}


    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception
    {
        System.out.println("===========================================================================================================================");
        System.out.println("Akcja: " + context.getMethod().getName());
        if(context.getMethod().getName()=="delete")
        {
            Long droneID = (Long) context.getParameters()[0];
            System.out.println("ID Drona: "+ droneID);
        }
        else
        {
            Drone drone = (Drone) context.getParameters()[0];
            System.out.println("ID Drona: "+ drone.getDroneID());
        }
        System.out.println("User:" +securityContext.getCallerPrincipal().getName());
        System.out.println("===========================================================================================================================");
        //before
        return context.proceed();
        //after
    }
}

