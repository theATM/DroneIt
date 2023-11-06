package jee.droneit.push.view;


import jee.droneit.push.context.PushMessageContext;
import jee.droneit.push.dto.Message;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.EJB;
import javax.faces.annotation.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.SecurityContext;
import java.io.Serializable;

@ViewScoped
@Named
public class MessageView  implements Serializable
{
    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private String sender;

    @Getter
    @Setter
    private String target;



    @Getter
    @Inject
    private PushMessageContext pushMessageContext;

    @Getter
    private Message message;

    private SecurityContext securityContext;

    @EJB
    public void setSecurityContext(SecurityContext securityContext){securityContext=securityContext;}


    public void sendAction()
    {
        String username = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();


        if(this.target != null && this.target.equals("") == false)
        {
            message = new Message(username,msg ,target);
            pushMessageContext.notifyUser(message,target);
        }
        else
        {
            message = new Message(username,msg ,"");
            pushMessageContext.notifyAll(message);
        }

    }
}
